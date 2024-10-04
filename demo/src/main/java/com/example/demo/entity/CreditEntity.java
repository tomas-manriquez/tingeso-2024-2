package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Float annualFee;
    private Float monthlyFee;
    private Float admFee;
    //TODO agregar seguros
}
