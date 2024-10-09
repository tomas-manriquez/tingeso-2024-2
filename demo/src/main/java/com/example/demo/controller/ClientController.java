package com.example.demo.controller;

import ch.qos.logback.core.net.server.Client;
import com.example.demo.entity.ClientEntity;
import com.example.demo.service.ClientService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;

    //METODOS PARA CRUD

    //CREATE cliente
    //Entrada: objeto ClienteEntity
    //Salida: estado OK
    @PostMapping("/")
    public ResponseEntity<ClientEntity> saveClient(@RequestBody ClientEntity client)
    {
        ClientEntity clientNew = clientService.saveClient(client);
        return ResponseEntity.ok(clientNew);
    }

    //READ para todos los clientes
    //Entrada: nada
    //Salida: estado OK
    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> listClients()
    {
      List<ClientEntity> clientEntities = clientService.getClients();
      return ResponseEntity.ok(clientEntities);
    }

    //READ cliente por su id de base de datos
    //Entrada: Long id
    //Salida: estado OK si se encuentra el cliente en la base de datos, estado NOT_FOUND en otro caso
    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable("id") Long id)
    {
        ClientEntity client = clientService.getClientById(id);
        return ResponseEntity.ofNullable(client);
    }

    //READ cliente por su rut en base de datos
    //Entrada: String rut
    //Salida: estado OK si se encuentra el cliente en la base de datos, estado NOT_FOUND en otro caso
    @GetMapping("/rut")
    public ResponseEntity<ClientEntity> getClientByRut(@RequestParam String rut)
    {
        ClientEntity client = clientService.getClientByRut(rut);
        return ResponseEntity.ofNullable(client);
    }

    //UPDATE cliente en cualquiera de sus atributos
    //Entrada: objeto ClientEntity con cambios
    //Salida: estado OK
    @PutMapping("/update")
    public ResponseEntity<ClientEntity> updateClient(ClientEntity client)
    {
        ClientEntity clientUpdated = clientService.updateClient(client);
        return ResponseEntity.ok(clientUpdated);
    }

    //DELETE de cliente por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteClientById(@PathVariable("id") Long id) throws Exception
    {
        var isDeleted = clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    //METODOS PARA REGLAS DE NEGOCIO
}
