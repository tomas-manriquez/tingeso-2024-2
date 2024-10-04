package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public String signIn(String rut, String firstName, String lastName, Integer age, DocumentEntity idFile, DocumentEntity incomeFile)
    {
     if((rut.length() ==9 || rut.length() ==10) && rut.contains("-") && !rut.contains("."))
     {
         if (age >=18)
         {
             if(idFile != null && incomeFile != null)
             {
                 ClientEntity client = new ClientEntity(null,
                         rut,
                         firstName,
                         lastName,
                         age,
                         "espera",
                         idFile,
                         incomeFile);
                 clientRepository.save(client);

                 return "Registro en espera de validacion de ejecutivo";
             }
             else
             {
                 return "Registro invalido: usuario debe adjuntar archivos de: Identificacion, Ingresos";
             }

         }
         else
         {
             return "Registro invalido: usuario debe ser mayor de 18";
         }
     }
     else
     {
         return "Registro invalido: rut incorrecto (debe ser digitos sin puntos y con guion)";
     }
    };
}
