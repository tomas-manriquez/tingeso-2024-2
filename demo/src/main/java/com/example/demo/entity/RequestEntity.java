package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="requests")
@Data
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientRut() {
        return clientRut;
    }

    public void setClientRut(String clientRut) {
        this.clientRut = clientRut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getMaxPayTerm() {
        return maxPayTerm;
    }

    public void setMaxPayTerm(Integer maxPayTerm) {
        this.maxPayTerm = maxPayTerm;
    }

    public Float getAnnualInterest() {
        return annualInterest;
    }

    public void setAnnualInterest(Float annualInterest) {
        this.annualInterest = annualInterest;
    }

    public Float getMaxFinanceAmount() {
        return maxFinanceAmount;
    }

    public void setMaxFinanceAmount(Float maxFinanceAmount) {
        this.maxFinanceAmount = maxFinanceAmount;
    }

    public Long getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(Long propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentEntity> documents) {
        this.documents = documents;
    }

    public boolean isHasSufficientDocuments() {
        return hasSufficientDocuments;
    }

    public void setHasSufficientDocuments(boolean hasSufficientDocuments) {
        this.hasSufficientDocuments = hasSufficientDocuments;
    }

    public Long getMonthlyCreditFee() {
        return monthlyCreditFee;
    }

    public void setMonthlyCreditFee(Long monthlyCreditFee) {
        this.monthlyCreditFee = monthlyCreditFee;
    }

    public Long getMonthlyClientIncome() {
        return monthlyClientIncome;
    }

    public void setMonthlyClientIncome(Long monthlyClientIncome) {
        this.monthlyClientIncome = monthlyClientIncome;
    }

    public Boolean getHasGoodCreditHistory() {
        return hasGoodCreditHistory;
    }

    public void setHasGoodCreditHistory(Boolean hasGoodCreditHistory) {
        this.hasGoodCreditHistory = hasGoodCreditHistory;
    }

    public Integer getCurrentJobAntiquity() {
        return currentJobAntiquity;
    }

    public void setCurrentJobAntiquity(Integer currentJobAntiquity) {
        this.currentJobAntiquity = currentJobAntiquity;
    }

    public Boolean getSelfEmployed() {
        return isSelfEmployed;
    }

    public void setSelfEmployed(Boolean selfEmployed) {
        isSelfEmployed = selfEmployed;
    }

    public Boolean getHasGoodIncomeHistory() {
        return hasGoodIncomeHistory;
    }

    public void setHasGoodIncomeHistory(Boolean hasGoodIncomeHistory) {
        this.hasGoodIncomeHistory = hasGoodIncomeHistory;
    }

    public Long getMonthlyDebt() {
        return monthlyDebt;
    }

    public void setMonthlyDebt(Long monthlyDebt) {
        this.monthlyDebt = monthlyDebt;
    }

    public Long getBankAccountBalance() {
        return bankAccountBalance;
    }

    public void setBankAccountBalance(Long bankAccountBalance) {
        this.bankAccountBalance = bankAccountBalance;
    }

    public Long getBiggestWithdrawalInLastYear() {
        return biggestWithdrawalInLastYear;
    }

    public void setBiggestWithdrawalInLastYear(Long biggestWithdrawalInLastYear) {
        this.biggestWithdrawalInLastYear = biggestWithdrawalInLastYear;
    }

    public Long getTotalDepositsInLastYear() {
        return totalDepositsInLastYear;
    }

    public void setTotalDepositsInLastYear(Long totalDepositsInLastYear) {
        this.totalDepositsInLastYear = totalDepositsInLastYear;
    }

    public Integer getBankAccountAge() {
        return bankAccountAge;
    }

    public void setBankAccountAge(Integer bankAccountAge) {
        this.bankAccountAge = bankAccountAge;
    }

    public Long getBiggestWithdrawalInLastSemester() {
        return biggestWithdrawalInLastSemester;
    }

    public void setBiggestWithdrawalInLastSemester(Long biggestWithdrawalInLastSemester) {
        this.biggestWithdrawalInLastSemester = biggestWithdrawalInLastSemester;
    }

    public ArrayList<Float> getExtraFees() {
        return extraFees;
    }

    public void setExtraFees(ArrayList<Float> extraFees) {
        this.extraFees = extraFees;
    }

    public RequestEntity() {
    }

    public RequestEntity(Long id, String clientRut, String type, Integer maxPayTerm, Float annualInterest, Float maxFinanceAmount, Long propertyValue, String status, List<DocumentEntity> documents, boolean hasSufficientDocuments, Long monthlyCreditFee, Long monthlyClientIncome, Boolean hasGoodCreditHistory, Integer currentJobAntiquity, Boolean isSelfEmployed, Boolean hasGoodIncomeHistory, Long monthlyDebt, Long bankAccountBalance, Long biggestWithdrawalInLastYear, Long totalDepositsInLastYear, Integer bankAccountAge, Long biggestWithdrawalInLastSemester, ArrayList<Float> extraFees) {
        this.id = id;
        this.clientRut = clientRut;
        this.type = type;
        this.maxPayTerm = maxPayTerm;
        this.annualInterest = annualInterest;
        this.maxFinanceAmount = maxFinanceAmount;
        this.propertyValue = propertyValue;
        this.status = status;
        this.documents = documents;
        this.hasSufficientDocuments = hasSufficientDocuments;
        this.monthlyCreditFee = monthlyCreditFee;
        this.monthlyClientIncome = monthlyClientIncome;
        this.hasGoodCreditHistory = hasGoodCreditHistory;
        this.currentJobAntiquity = currentJobAntiquity;
        this.isSelfEmployed = isSelfEmployed;
        this.hasGoodIncomeHistory = hasGoodIncomeHistory;
        this.monthlyDebt = monthlyDebt;
        this.bankAccountBalance = bankAccountBalance;
        this.biggestWithdrawalInLastYear = biggestWithdrawalInLastYear;
        this.totalDepositsInLastYear = totalDepositsInLastYear;
        this.bankAccountAge = bankAccountAge;
        this.biggestWithdrawalInLastSemester = biggestWithdrawalInLastSemester;
        this.extraFees = extraFees;
    }
}
