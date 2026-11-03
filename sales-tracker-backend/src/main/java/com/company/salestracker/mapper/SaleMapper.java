package com.company.salestracker.mapper;

import com.company.salestracker.dto.response.SaleResponse;
import com.company.salestracker.entity.Deal;
import com.company.salestracker.entity.Sale;
import com.company.salestracker.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class SaleMapper {

    public Sale toEntity(Deal deal, BigDecimal amount, String invoiceNumber, LocalDate saleDate) {
        return Sale.builder()
                .deal(deal)
                .saleAmount(amount)
                .invoiceNumber(invoiceNumber)
                .saleDate(saleDate != null ? saleDate : LocalDate.now())
                .paymentStatus(PaymentStatus.PENDING)
                .build();
    }

      public SaleResponse toResponse(Sale sale) {
        if (sale == null) return null;

        return SaleResponse.builder()
                .id(sale.getId())
                .dealId(sale.getDeal().getId())
                .leadName(sale.getDeal().getLead().getName())
                .assignedToName(sale.getDeal().getAssignedTo() != null
                        ? sale.getDeal().getAssignedTo().getName() : null)
                .saleAmount(sale.getSaleAmount())
                .paymentStatus(sale.getPaymentStatus())
                .invoiceNumber(sale.getInvoiceNumber())
                .saleDate(sale.getSaleDate())
                .build();
    }
}
