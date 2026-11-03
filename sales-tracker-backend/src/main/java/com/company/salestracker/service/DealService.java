package com.company.salestracker.service;

import com.company.salestracker.dto.request.DealRequest;
import com.company.salestracker.dto.response.DealResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DealService {
    DealResponse convertLeadToDeal(DealRequest request);
    DealResponse updateDeal(Integer id, DealRequest request);
    DealResponse closeDeal(Integer id, String outcome); 
    Page<DealResponse> listDeals(String stage, Pageable pageable);
}
