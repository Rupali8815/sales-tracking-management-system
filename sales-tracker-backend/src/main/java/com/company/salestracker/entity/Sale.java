package com.company.salestracker.entity;

import com.company.salestracker.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id", nullable = false, unique = true)
    private Deal deal;

    @Column(name = "sale_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal saleAmount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "invoice_number", length = 100)
    private String invoiceNumber;

    @Builder.Default
    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate = LocalDate.now();
}
