package com.company.salestracker.service.impl;

import com.company.salestracker.enums.AuditAction;
import com.company.salestracker.service.AuditService;
import com.company.salestracker.dto.response.SaleResponse;
import com.company.salestracker.entity.Deal;
import com.company.salestracker.entity.Sale;
import com.company.salestracker.enums.DealStage;
import com.company.salestracker.enums.PaymentStatus;
import com.company.salestracker.exception.ConflictException;
import com.company.salestracker.exception.ForbiddenOperationException;
import com.company.salestracker.exception.ResourceNotFoundException;
import com.company.salestracker.mapper.SaleMapper;
import com.company.salestracker.repository.DealRepository;
import com.company.salestracker.repository.SalesRepository;
import com.company.salestracker.service.SalesService;
import com.company.salestracker.util.CurrentUser;
import com.company.salestracker.util.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {

    private final SalesRepository salesRepository;
    private final DealRepository dealRepository;
    private final SaleMapper saleMapper;
    private final CurrentUser currentUser;
    private final AuditService auditService;

    @Override
    @Transactional
    public SaleResponse recordSale(Integer dealId, BigDecimal amount, String invoiceNumber, LocalDate saleDate) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new ResourceNotFoundException("Deal not found: " + dealId));

        boolean owner = deal.getAssignedTo() != null
                && deal.getAssignedTo().getId().equals(currentUser.getId());
        if (!currentUser.isAdminOrManager() && !owner) {
            throw new ForbiddenOperationException("You can only record a sale for your own deal");
        }
        if (salesRepository.existsByDealId(dealId)) {
            throw new ConflictException("A sale is already recorded for this deal");
        }
        if (deal.getDealStage() == DealStage.CLOSED_LOST) {
            throw new ConflictException("Deal is CLOSED_LOST, a sale cannot be recorded");
        }

        deal.setDealStage(DealStage.CLOSED_WON);
        dealRepository.save(deal);

        Sale sale = saleMapper.toEntity(deal, amount, invoiceNumber, saleDate);

        Sale saved = salesRepository.save(sale);
        auditService.log(AuditAction.CREATE, "Sale", saved.getId());
        return saleMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleResponse> listSales(Pageable pageable) {
         Page<Sale> page = currentUser.isOwnerScoped()
                ? salesRepository.findByDealAssignedToId(currentUser.getId(), pageable)
                : salesRepository.findAll(pageable);
        return page.map(saleMapper::toResponse);
    }

    @Override
    @Transactional
    public SaleResponse updatePaymentStatus(Integer saleId, String paymentStatus) {
        Sale sale = salesRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found: " + saleId));

        Deal deal = sale.getDeal();
        boolean owner = deal.getAssignedTo() != null
                && deal.getAssignedTo().getId().equals(currentUser.getId());
        if (!currentUser.isAdminOrManager() && !owner) {
            throw new ForbiddenOperationException("You can only change payment status of your own sales");
        }
        sale.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));

        Sale saved = salesRepository.save(sale);
        auditService.log(AuditAction.UPDATE, "Sale", saved.getId());
        return saleMapper.toResponse(saved);
    }

    @Override
    public BigDecimal getMonthlySalesTotal(int month, int year) {
        LocalDate start = DateUtil.firstDayOfMonth(month, year);
        LocalDate end = DateUtil.lastDayOfMonth(month, year);
        return salesRepository.findBetweenDates(start, end).stream()
                .map(Sale::getSaleAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getYearlySalesTotal(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return salesRepository.findBetweenDates(start, end).stream()
                .map(Sale::getSaleAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
