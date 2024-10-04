package com.example.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceEntity {
    private Float amount;
    /**
     * type: tipo de monto
     * perc: % del monto del prestamo
     * percMonthly: % del monto del prestamo mensual
     * abs: $ directo
     * absMonthly: $ mensual
     *
     */
    private String type;
}
