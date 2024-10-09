package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RequestRepository;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    //P3: Solicitud de Credito
    //Entrada: String rut (de client), String requestType, Integer maxPayTerm, Float annualInterest, Float maxFinanceAmount, List<DocumentEntity> documentEntityList
    //Salida: String "Solicitud ingresada con exito" =  se registra Solicitud en estado 'E3' asociado al cliente de id 'clientId' ...
    //Salida: String "Solicitud ingresada, documentos pendientes para poder avanzar" = se registra Solicitud en estado 'E2' asociado al cliente de id 'clienteId'
    //... String "Error en Solicitud:" + razon de error (aca se manejan errores segun las reglas de negocio)
    public String makeRequest(String clientRut, String requestType, Integer maxPayTerm, Float annualInterest, Float financeAmount, List<DocumentEntity> documentEntityList)
    {
        /**
        ClientEntity clientEntity = clientRepository.findByRut(clientRut);
        if(clientEntity != null)
        {
            if (requestType.equals("vivienda1"))
            {
                if(maxPayTerm <30)
                {
                    if(annualInterest>3.5f && annualInterest<5.0f)
                    {
                        if(financeAmount <= 0.8f)
                        {
                            if(!documentEntityList.isEmpty())
                            {
                                //ingresar solicitud con estado 'E2'

                            }
                            else
                            {
                                //ingresar solicitud con estado 'E3'
                            }
                        }
                        else
                        {
                            return "Error en solicitud: financiamiento solicitado supera el monto admitido por este tipo de credito (80% del valor de la propiedad)";
                        }
                    }
                    else
                    {
                        return "Error en solicitud: Tasa de interes esta fuera del rango admitido para este tipo de credito (3.5% - 5.0%)";
                    }
                }
                else
                {
                    return "Error en solicitud: Plazo solicitado es mayor al maximo para este tipo de credito (para primera vivienda)";
                }
            }
            else
            {

            }
        }
        else
        {
            return "Error en Solicitud: Cliente no esta registrado";
        }
         **/
        return null;
    };

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
}
