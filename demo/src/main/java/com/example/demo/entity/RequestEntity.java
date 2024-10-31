package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String clientRut;
    //types:
    //"vivienda1" = para primera vivienda
    //"vivienda2" = para segunda vivienda
    //"comercial" = para propiedades comerciales
    //"remodelacion" = para remodelacion
    private String type;
    //'maxPayTerm' = plazo maximo
    private Integer maxPayTerm;
    private Float annualInterest;
    private Float maxFinanceAmount;
    private Long propertyValue;
    //State: "E1" hasta "E9" segun enunciado
    private String status;
    //"id" es el id de DocumentEntity
    @OneToMany(mappedBy = "request")
    private List<DocumentEntity> documents;
    private boolean hasSufficientDocuments;
    private Long monthlyCreditFee;
    private Long monthlyClientIncome;
    private Boolean hasGoodCreditHistory;
    //en meses (!)
    private Integer currentJobAntiquity;
    private Boolean isSelfEmployed;
    private Boolean hasGoodIncomeHistory;
    private Long monthlyDebt;
    //Para P4.R7.R74
    private Long bankAccountBalance;
    //Para P4.R7.R72
    private Long biggestWithdrawalInLastYear;
    //Para P4.R7.R73
    private Long totalDepositsInLastYear;
    //Para P4.R7.R74
    private Integer bankAccountAge;                     //en años(!)
    //Para P4.R7.R75
    private Long biggestWithdrawalInLastSemester;
    @ElementCollection
    private ArrayList<Float> extraFees;
}
