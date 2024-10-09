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
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    ClientRepository clientRepository;

    //METODOS PARA ACCEDER A CRUD

    //READ todos los clientes
    //Entrada: nada
    //Salida: ArrayList de objetos ClientEntity, puede estar vacio
    public ArrayList<ClientEntity> getClients()
    {
        return (ArrayList<ClientEntity>) clientRepository.findAll();
    }

    //READ cliente por su id de base de datos
    //Entrada: Long id
    //Salida: objeto ClienteEntity si se encuentra en la base de datos, en otro caso null
    public ClientEntity getClientById(Long id)
    {
        Optional<ClientEntity> client = clientRepository.findById(id);
        return client.orElse(null);
    }

    //READ cliente por su rut en base de datos
    //Entrada: String rut
    //Salida: objeto ClienteEntity si se encuentra en la base de datos, en otro caso null
    public ClientEntity getClientByRut(String rut)
    {
        Optional<ClientEntity> client = clientRepository.findByRut(rut);
        return client.orElse(null);
    }

    //CREATE cliente
    //Entrada: objeto ClienteEntity
    //Salida: el mismo objeto de entrada. Como efecto secundario, se guarda tal objeto en la base de datos
    public ClientEntity saveClient(ClientEntity client)
    {
        return clientRepository.save(client);
    }

    //UPDATE cliente en cualquiera de sus atributos
    //Entrada: objeto ClientEntity con cambios
    //Salida: el mismo objeto. Como efecto secundario, se actualiza el objeto con este id en la base de datos
    public ClientEntity updateClient(ClientEntity client)
    {
        return clientRepository.save(client);
    }

    //DELETE de cliente por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    public boolean deleteClient(Long id) throws Exception
    {
        try
        {
            clientRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    //METODOS PARA REGLAS DE NEGOCIO


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
