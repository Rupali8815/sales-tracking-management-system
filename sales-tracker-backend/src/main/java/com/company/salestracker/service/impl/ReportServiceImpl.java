package com.company.salestracker.service.impl;

import com.company.salestracker.entity.Deal;
import com.company.salestracker.entity.Sale;
import com.company.salestracker.enums.DealStage;
import com.company.salestracker.repository.DealRepository;
import com.company.salestracker.repository.LeadRepository;
import com.company.salestracker.repository.SalesRepository;
import com.company.salestracker.service.ReportService;
import com.company.salestracker.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final SalesRepository salesRepository;
    private final LeadRepository leadRepository;
    private final DealRepository dealRepository;

    @Override
    public Map<String, BigDecimal> salesByUser(int month, int year) {
        LocalDate start = DateUtil.firstDayOfMonth(month, year);
        LocalDate end = DateUtil.lastDayOfMonth(month, year);

        return salesRepository.findBetweenDates(start, end).stream()
                .filter(sale -> sale.getDeal().getAssignedTo() != null)
                .collect(Collectors.groupingBy(
                        sale -> sale.getDeal().getAssignedTo().getName(),
                        LinkedHashMap::new,
                        Collectors.reducing(BigDecimal.ZERO, Sale::getSaleAmount, BigDecimal::add)
                ));
    }

    @Override
    public List<Map<String, Object>> salesByTimePeriod(int year) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            LocalDate start = DateUtil.firstDayOfMonth(month, year);
            LocalDate end = DateUtil.lastDayOfMonth(month, year);

            BigDecimal total = salesRepository.findBetweenDates(start, end).stream()
                    .map(Sale::getSaleAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("month", month);
            row.put("year", year);
            row.put("total", total);
            results.add(row);
        }
        return results;
    }

    @Override
    public double leadToDealConversionRatio() {
        long totalLeads = leadRepository.count();
        long totalDeals = dealRepository.count();
        if (totalLeads == 0) return 0.0;
        return (double) totalDeals / totalLeads * 100.0;
    }

    @Override
    public List<Map<String, Object>> lostDealAnalysis() {
        return dealRepository.findAll().stream()
                .filter(deal -> deal.getDealStage() == DealStage.CLOSED_LOST)
                .map(this::toLostDealRow)
                .collect(Collectors.toList());
    }

    private Map<String, Object> toLostDealRow(Deal deal) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("dealId", deal.getId());
        row.put("leadName", deal.getLead().getName());
        row.put("expectedAmount", deal.getExpectedAmount());
        row.put("assignedTo", deal.getAssignedTo() != null ? deal.getAssignedTo().getName() : null);
        row.put("closingDate", deal.getClosingDate());
        return row;
    }
}
