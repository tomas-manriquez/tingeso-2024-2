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
    //Salida: Estado OK. 'true' si se borra con exito, Exception en otro caso
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteRequestById(@PathVariable("id") Long id) throws Exception
    {
        var isDeleted = requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }

    //METODOS PARA REGLAS DE NEGOCIO

    //P1: Simulacion de Credito Hipotecario
    //Calcula cuota mensual de credito segun datos entregados por usuario y formula de enunciado
    //Entrada: String rutClient, Long totalAmount (monto del prestamo, capital)...
    //... Float annualFee, Integer payTerm (plazo en años de pago)
    //Salida: estado OK, Long simulation, =0L si algun input es incorrecto o usuario no es cliente
    @GetMapping("/simulation")
    public ResponseEntity<Long> creditSimulation(@RequestParam String rutClient,
                                                 @RequestParam String creditType,
                                                 @RequestParam Long totalAmount,
                                                 @RequestParam Float annualFee,
                                                 @RequestParam Integer payTerm)
    {
        Long result = requestService.creditSimulation(rutClient, creditType, totalAmount, annualFee, payTerm);
        return ResponseEntity.ok(result);
    }

    //P3: Solicitud de Credito
    //Entrada: RequestEntity request
    //Salida: Estado OK. 'true' si se registro la solicitud con exito, 'false' en otro caso
    //casos 'false' se generar por cliente solicitante que no esta registrado
    @PostMapping("/makeRequest")
    public ResponseEntity<Boolean> makeRequest(RequestEntity requestNew)
    {
        Boolean success = requestService.makeRequest(requestNew);
        return ResponseEntity.ok(success);
    }

    //P4: Evaluacion de Credito
    //Entrada: RequestEntity request
    //Salida: Estado OK. Como efecto secundario, se altera el 'status' del RequestEntity de entrada segun reglas de negocio
    @PutMapping("/evaluation")
    public ResponseEntity<RequestEntity> requestEvaluation(RequestEntity request)
    {
        RequestEntity updatedRequest = requestService.requestEvaluation(request);
        return ResponseEntity.ok(updatedRequest);
    }

    //P5: Seguimiento de Solicitudes
    //Retorna el estado de una solicitud
    //Entrada: id de solicitud (Long)
    //Salida: Estado OK. estado de la solicitud (String)
    @GetMapping("/status/{id}")
    public ResponseEntity<String> requestTracking (@PathVariable Long id)
    {
        return ResponseEntity.ok(requestService.requestTracking(id));
    }

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

    //P6: Calculo de Costos Totales
    //Calcula el costo total (base por credito + seguros + comisiones) de cierto credito
    //Entrada: RequestEntity
    //Salida: Estado OK.Long costo total del credito
    public ResponseEntity<Long> totalCost(RequestEntity request)
    {
        return ResponseEntity.ok(requestService.totalCost(request));
    }
}
