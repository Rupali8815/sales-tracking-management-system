package com.company.salestracker.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportService {
    Map<String, BigDecimal> salesByUser(int month, int year);
    List<Map<String, Object>> salesByTimePeriod(int year);
    double leadToDealConversionRatio();
    List<Map<String, Object>> lostDealAnalysis();
}
