package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RequestRepository;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ClientRepository clientRepository;

    //METODOS PARA ACCEDER A CRUD

    //CREATE solicitud
    //Entrada: objeto RequestEntity
    //Salida: el mismo objeto de entrada. Como efecto secundario, se guarda tal objeto en la base de datos
    public RequestEntity saveRequest(RequestEntity request)
    {
        return requestRepository.save(request);
    }

    //READ todas las solicitudes
    //Entrada: nada
    //Salida: ArrayList de objetos RequestEntity, puede estar vacio
    public ArrayList<RequestEntity> getRequests()
    {
        return (ArrayList<RequestEntity>) requestRepository.findAll();
    }

    //READ solicitud por su id en la base de datos
    //Entrada: Long id
    //Salida: objeto DocumentEntity si se encuentra en la base de datos, en otro caso null
    public RequestEntity getRequestById(Long id)
    {
        Optional<RequestEntity> request = requestRepository.findById(id);
        return  request.orElse(null);
    }

    //READ solicitud por el rut del cliente dueño en base de datos
    //Entrada: String rut
    //Salida: objeto RequestEntity si se encuentra en la base de datos, en otro caso null
    public RequestEntity getRequestByClientRut(String rut)
    {
        Optional<RequestEntity> request = requestRepository.findByClientRut(rut);
        return request.orElse(null);
    }

    //UPDATE solicitud en cualquiera de sus atributos
    //Entrada: objeto RequestEntity con cambios
    //el mismo objeto. Como efecto secundario, se actualiza el objeto con este id en la base de datos
    //Por naturaleza de DocumentEntity y del negocio, se recomienda no usar.
    public RequestEntity updateRequest(RequestEntity request)
    {
        return requestRepository.save(request);
    }

    //DELETE de solicitud por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    public boolean deleteRequest(Long id) throws Exception
    {
        try
        {
            requestRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    //METODOS PARA REGLAS DE NEGOCIO


    //P1: Simulacion de Credito Hipotecario
    //Calcula cuota mensual de credito segun datos entregados por usuario y formula de enunciado
    //Entrada: String rutClient, Long totalAmount (monto del prestamo, capital)...
    //... Float annualFee, Integer payTerm (plazo en años de pago)
    //Salida: Long simulation, =0L si algun input es incorrecto o usuario no es cliente
    public Double creditSimulation(String rutClient, String creditType, Long totalAmount, Float annualFee, Integer payTerm)
    {
        double simulation = 0d;
        Optional<ClientEntity> found = clientRepository.findByRut(rutClient);
        if(found.isPresent())
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

    //P3: Solicitud de Credito
    //Entrada: RequestEntity request
    //Salida: 'true' si se registro la solicitud con exito, 'false' en otro caso
    //casos 'false' se generar por cliente solicitante que no esta registrado
    public boolean makeRequest(RequestEntity requestNew)
    {
        if (clientRepository.findByRut(requestNew.getClientRut()).isPresent())
        {
            if (requestNew.getType()!= null && requestNew.getMaxPayTerm() != null
            && requestNew.getAnnualInterest() != null && requestNew.getMaxFinanceAmount() != null
            && requestNew.getIsSelfEmployed() != null && requestNew.getMonthlyDebt() != null)
            {
                if (requestNew.getDocuments().isEmpty())
                {
                    //Se ingresa request en estado 'E2'
                    requestNew.setStatus("E2");
                    saveRequest(requestNew);
                    return true;
                }
                else
                {
                    //Se ingresa request en estado 'E3'
                    requestNew.setStatus("E3");
                    saveRequest(requestNew);
                    return true;
                }
            }
            else
            {
                //Se ingresa request en estado 'E1'
                requestNew.setStatus("E1");
                saveRequest(requestNew);
                return true;
            }
        }
        else
        {
            return false;
        }
    }



    //P5: Seguimiento de Solicitudes
    //Retorna el estado de una solicitud
    //Entrada: id de solicitud (Long)
    //Salida: estado de la solicitud (String)
    public String requestTracking (Long requestId)
    {
        Optional<RequestEntity> request = requestRepository.findById(requestId);
        if(request.isPresent())
        {
            if(request.get().getStatus() != null)
            {
                return request.get().getStatus();
            }
            else
            {
                return null;
            }
        }
        else
        {
            return  null;
        }
    };

    //P5: Seguimiento de solicitudes
    //Actualiza el estado de una solicitud
    //Entrada: Long id de solicitud, String estado nuevo
    //Salida: "Actualizado" si se actualiza la solicitud exitosamente, "Cancelado: " + razon de error
    public String updateStatus(Long requestId, String newStatus)
    {
        if(requestRepository.findById(requestId).isPresent())
        {
            RequestEntity request = requestRepository.findById(requestId).get();
            request.setStatus(newStatus);
            requestRepository.save(request);

            return "Actualizado";
        }
        else
        {
            return "Cancelado: Solicitud no existe / no se encontro";
        }
    };

    //P6:Calculo de Costos Totales
    public Double totalMonthlyFee(RequestEntity request, ArrayList<Object> extraFees)
    {
        Double totalCreditAmount = (request.getPropertyValue() * request.getMaxFinanceAmount());
        Double result = creditSimulation(
                request.getClientRut(),
                request.getType(),
                totalCreditAmount.longValue(),
                request.getAnnualInterest(),
                request.getMaxPayTerm());

        for(int i=0; i<extraFees.size(); i++)
        {
            if(extraFees.get(i) instanceof Double)
            {
                Double aux = totalCreditAmount *  ((Double) extraFees.get(i)).floatValue();
                result = result + aux;
            }
            else
            {
                result = result + (Double) extraFees.get(i);
            }
        }
        return result;
    }

    //P6: Calculo de costos totales
    public Double totalFee(RequestEntity request, ArrayList<Object> extraFees)
    {
        Double totalCreditAmount = (request.getPropertyValue() * request.getMaxFinanceAmount());
        Double result = creditSimulation(
                request.getClientRut(),
                request.getType(),
                totalCreditAmount.longValue(),
                request.getAnnualInterest(),
                request.getMaxPayTerm());

        for(int i=0; i<extraFees.size(); i++)
        {
            if(extraFees.get(i) instanceof Double)
            {
                Double aux = totalCreditAmount *  ((Double) extraFees.get(i)).floatValue();
                result = result + aux;
            }
            else
            {
                result = result + (Double) extraFees.get(i);
            }
        }
        return result * 12 * request.getMaxPayTerm();
    }



}
