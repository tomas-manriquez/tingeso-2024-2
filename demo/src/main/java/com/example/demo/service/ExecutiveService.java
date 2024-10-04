package com.example.demo.service;

import ch.qos.logback.core.net.server.Client;
import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.ExecutiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExecutiveService {
    @Autowired
    ExecutiveRepository executiveRepository;

    @Autowired
    ClientRepository clientRepository;

    //TODO validateClientSignIn
    //Valida documentos entregados para P2: Registro de Usuario
    //Input: rutClient, idFile, incomeFile
    //Output: true si se aprueba nuevo cliente, false en otro caso
    public boolean validateClientSignIn (String rutClient, String creditType, DocumentEntity idFile, DocumentEntity incomeFile)
    {
        return true;
    }

    //P1: Simulacion de Credito Hipotecario
    //Calcula cuota mensual de credito segun datos entregados por usuario y formula de enunciado
    //Entrada: String rutClient, Long totalAmount (monto del prestamo, capital)...
    //... Float annualFee, Integer payTerm (plazo en años de pago)
    //Salida: Long simulation, =0L si algun input es incorrecto o usuario no es cliente
    public Double creditSimulation(String rutClient, String creditType, Long totalAmount, Float annualFee, Integer payTerm)
    {
        double simulation = 0d;
        ClientEntity found = clientRepository.findByRut(rutClient);
        if(found != null)
        {
            if (totalAmount > 0L)
            {
                if (annualFee > 0L)
                {
                    float monthlyFee = (annualFee/12)/100;
                    if(creditType.equals("vivienda1"))
                    {
                        if(payTerm <=30)
                        {
                            double totalPayTerms = payTerm*12;
                            simulation = totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1);
                        }
                        return simulation;
                    }
                    else
                    {
                        if(creditType.equals("vivienda2"))
                        {
                            if(payTerm <=20)
                            {
                                double totalPayTerms = payTerm*12;
                                simulation = totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1);
                            }
                            return simulation;
                        }
                        else
                        {
                            if(creditType.equals("comercial"))
                            {
                                if(payTerm<=25)
                                {
                                    double totalPayTerms = payTerm*12;
                                    simulation = totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1);
                                }
                                return simulation;
                            }
                            else
                            {
                                if(creditType.equals("remodelacion"))
                                {
                                    if(payTerm<=15)
                                    {
                                        double totalPayTerms = payTerm*12;
                                        simulation = totalAmount * (monthlyFee*Math.pow(1+monthlyFee, totalPayTerms)/Math.pow(1+monthlyFee,totalPayTerms)-1);
                                    }
                                    return simulation;
                                }
                                else
                                {
                                    return simulation;
                                }
                            }
                        }
                    }
                }
                else
                {
                    return simulation;
                }
            }
            else
            {
                return simulation;
            }
        }
        else
        {
            return simulation;
        }
    };
}
