package com.example.demo.controller;

import com.example.demo.entity.ClientEntity;
import com.example.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientEntity>> listClients()
    {
      List<ClientEntity> clientEntities = clientService.getClients();
      return ResponseEntity.ok(clientEntities);
    };
}
