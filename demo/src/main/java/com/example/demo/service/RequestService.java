package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ClientRepository clientRepository;

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
}
