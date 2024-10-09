package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @ElementCollection
    private List<Long> incomeHistory;
    private Long monthlyDebt;
    private Long bankAccountBalance;
    @ElementCollection
    private List<Long> bankAccountBalanceHistory;
}
