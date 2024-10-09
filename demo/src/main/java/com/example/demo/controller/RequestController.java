package com.example.demo.controller;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin("*")
public class RequestController {
    @Autowired
    RequestService requestService;

    //METODOS PARA CRUD

    //CREATE solicitud
    //Entrada: objeto RequestEntity
    //Salida: estado OK
    @PostMapping("/")
    public ResponseEntity<RequestEntity> saveRequest(@RequestBody RequestEntity request)
    {
        RequestEntity requestNew = requestService.saveRequest(request);
        return ResponseEntity.ok(requestNew);
    }

    //READ para todas las solicitudes
    //Entrada: nada
    //Salida: estado OK
    @GetMapping("/")
    public ResponseEntity<List<RequestEntity>> listRequests()
    {
        List<RequestEntity> requestEntities = requestService.getRequests();
        return ResponseEntity.ok(requestEntities);
    }

    //READ cliente por su id de base de datos
    //Entrada: Long id
    //Salida: estado OK si se encuentra el cliente en la base de datos, estado NOT_FOUND en otro caso
    @GetMapping("/{id}")
    public ResponseEntity<RequestEntity> getRequestById(@PathVariable("id") Long id)
    {
        RequestEntity request = requestService.getRequestById(id);
        return ResponseEntity.ofNullable(request);
    }

    //READ solicitud por el rut del cliente dueño en base de datos
    //Entrada: String rut
    //Salida: estado OK si se encuentra el cliente en la base de datos, estado NOT_FOUND en otro caso
    @GetMapping("/rut")
    public ResponseEntity<RequestEntity> getClientByRut(@RequestParam String rut)
    {
        RequestEntity request = requestService.getRequestByClientRut(rut);
        return ResponseEntity.ofNullable(request);
    }

    //UPDATE solicitud en cualquiera de sus atributos
    //Entrada: objeto RequestEntity con cambios
    //Salida: estado OK
    @PutMapping("/update")
    public ResponseEntity<RequestEntity> updateClient(RequestEntity request)
    {
        RequestEntity requestUpdated = requestService.updateRequest(request);
        return ResponseEntity.ok(requestUpdated);
    }

    //DELETE de solicitud por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRequestById(@PathVariable("id") Long id) throws Exception
    {
        var isDeleted = requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    //METODOS PARA REGLAS DE NEGOCIO

    //P3: Solicitud de Credito
    //Entrada: String rut (de client), String requestType, Integer maxPayTerm, Float annualInterest, Float maxFinanceAmount, List<DocumentEntity> documentEntityList
    //Salida: String "Solicitud ingresada con exito" =  se registra Solicitud en estado 'E3' asociado al cliente de id 'clientId' ...
    //Salida: String "Solicitud ingresada, documentos pendientes para poder avanzar" = se registra Solicitud en estado 'E2' asociado al cliente de id 'clienteId'
    //... String "Error en Solicitud:" + razon de error (aca se manejan errores segun las reglas de negocio)
    /**
    @PostMapping("")
    private String makeRequest (String clientRut, String requestType, Integer payTerm, Float annualInterest, Float financeAmount, List<DocumentEntity> documentEntityList)
    {
        //TODO clientRut contiene solo 1 "-"
        if ((clientRut.length()==10 || clientRut.length()==9) && !clientRut.contains(".") && clientRut.contains("-"))
        {
            if(requestType.equals("vivienda1") || requestType.contains("vivienda2") || requestType.contains("comercial") || requestType.contains("remodelacion"))
            {
                //verificacion de montos por tipo de solicitud
                switch (requestType)
                {
                    case "vivienda1":
                        if(payTerm <=30)
                        {
                            if(annualInterest>=3.5f && annualInterest<=5.0f)
                            {
                                if(financeAmount> 0f &&  financeAmount<= 0.8f)
                                {
                                    if(!documentEntityList.isEmpty())
                                    {
                                        RequestEntity newRequest = new RequestEntity(
                                                null,
                                                "vivienda1",
                                                payTerm,
                                                annualInterest,
                                                financeAmount,
                                                "E2",
                                                documentEntityList
                                        );
                                        return null;
                                    }
                                    else
                                    {
                                        return null;
                                    }
                                }
                                else
                                {
                                    return "Error en Solicitud: monto de financiamiento no posible para tipo de solicitud (primera vivienda: maximo 80%)";
                                }
                            }
                            else
                            {
                                return "Error en Solicitud: tasa de interes no posible para tipo de solicitud (primera vivienda: entre 3.5% y 5.0%)";
                            }
                        }
                        else
                        {
                            return "Error en Solicitud: plazo no posible para tipo de solicitud (primera vivienda: maximo 30 años)";
                        }
                    default:
                        return null;
                }
            }
            else
            {
                return "Error en Solicitud: tipo de solicitud invalido";
            }
        }
        else
        {
            return "Error en Solicitud: rut invalido (digitos sin puntos y con guion)";
        }
    };

    //P5: Seguimiento de solicitudes
    //Entrada: Long id de solicitud
    //Salida: String estado de la solicitud, o "Error: input invalido" en caso de error
    @GetMapping("/status/{id}")
    public String requestTracking(@PathVariable("id") Long requestId)
    {
        if(requestId >= 0L)
        {
            return requestService.requestTracking(requestId);
        }
        else
        {
            return "Error: input invalido";
        }
    };
    **/

    //P5: Seguimiento de solicitudes
    //Entrada: Long id de solicitud, String estado nuevo
    //Salida: "Actualizado" si se actualiza la solicitud exitosamente, "Cancelado: " + razon de error
    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable("id") Long requestId, String newStatus)
    {
        if(requestId >= 0L)
        {
            List<String> validStatuses = Arrays.asList(
                    "E1: En Revision Inicial",
                    "E2: Pendiente de Documentacion",
                    "E3: En Evaluacion",
                    "E4: Pre-Aprobada",
                    "E5: En Aprobacion Final",
                    "E6: Aprobada",
                    "E7: Rechazada",
                    "E8: Cancelada por el Cliente",
                    "E9: En Desembolso");
            if(validStatuses.contains(newStatus))
            {
                return requestService.updateStatus(requestId, newStatus);
            }
            else
            {
                return "Cancelado: nuevo estado invalido";
            }
        }
        else
        {
            return "Cancelado: nuevo estado invalido";
        }
    };
}
