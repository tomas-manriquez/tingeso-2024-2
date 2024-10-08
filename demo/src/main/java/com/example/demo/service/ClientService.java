package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    //getClients
    //saveClient
    //getClientById
    public ArrayList<ClientEntity> getClients()
    {
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    //P2: Registro de usuario
    //Verifica datos basicos de usuario, y si son validos, habilita documentos a ejecutivos para validacion
    //Entrada: rut(numeros seguidos con guion), firstName (String), lastName (String), age (Int), 2 documentos (id + ingresos)
    //Salida: String indicando estado actual de registro de usuario:
    //'registro invalido' + razon de rechazo por input basico
    //'registro en espera de validacion de ejecutivo' cuando se habilita para validacion de ejecutivos
    public String signIn(String rut, String firstName, String lastName, String birthday, DocumentEntity idFile, DocumentEntity incomeFile)
    {
        return null;
    };
}
