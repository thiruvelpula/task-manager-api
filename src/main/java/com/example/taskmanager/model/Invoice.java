package com.example.taskmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    // [DOM-01] VIOLATION: monetary field uses primitive double
    @Column(nullable = false)
    private double unitPrice;

    // [DOM-01] VIOLATION: quantity used in money math — should be int or BigDecimal
    @Column(nullable = false)
    private double quantity;

    // [DOM-01] VIOLATION: tax rate as float introduces compounding rounding
    @Column(nullable = false)
    private float taxRate;

    // [DOM-01] VIOLATION: discount amount as double
    @Column(nullable = false)
    private double discountAmount;

    // [DOM-01] VIOLATION: total amount as Double wrapper — same problem
    @Column(nullable = false)
    private Double totalAmount;

    // [DOM-01] VIOLATION: account balance as double
    @Column(nullable = false)
    private double accountBalance;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
