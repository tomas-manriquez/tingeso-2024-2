package com.example.demo.service;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.repository.ClientRepository;
import com.example.demo.repository.DocumentRepository;
import com.example.demo.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class DocumentService{
    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RequestRepository requestRepository;

    //METODOS PARA ACCEDER A CRUD

    //CREATE documento
    //Entrada: objeto DocumentEntity
    //Salida: el mismo objeto de entrada. Como efecto secundario, se guarda tal objeto en la base de datos
    public DocumentEntity saveDocument(DocumentEntity document)
    {
        return documentRepository.save(document);
    }

    //READ todos los documentos
    //Entrada: nada
    //Salida: ArrayList de objetos DocumentEntity, puede estar vacio
    public ArrayList<DocumentEntity> getAll()
    {
        return (ArrayList<DocumentEntity>) documentRepository.findAll();
    }

    //READ documento por su id en la base de datos
    //Entrada: Long id
    //Salida: objeto DocumentEntity si se encuentra en la base de datos, en otro caso null
    public DocumentEntity getDocumentById(Long id)
    {
        Optional<DocumentEntity> document = documentRepository.findById(id);
        return  document.orElse(null);
    }

    //UPDATE documento en cualquiera de sus atributos
    //Entrada: objeto DocumentEntity con cambios
    //el mismo objeto. Como efecto secundario, se actualiza el objeto con este id en la base de datos
    //Por naturaleza de DocumentEntity y del negocio, se recomienda no usar.
    public DocumentEntity updateDocument(DocumentEntity document)
    {
        return documentRepository.save(document);
    }

    //DELETE de documento por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    public boolean deleteDocument(Long id) throws Exception
    {
        try
        {
            documentRepository.deleteById(id);
            return true;
        }
        catch (Exception e)
        {
            throw new Exception(e.getMessage());
        }
    }

    //METODOS PARA REGLAS DE NEGOCIO

    //Recibe un MultipartFile (pdf) y lo guarda en la base de datos
    //Entrada: MultipartFile, clientId, requestId
    //Salida: DocumentEntity
    public DocumentEntity store(MultipartFile file, Long clientId, Long requestId) throws IOException
    {
        if(clientRepository.findById(clientId).isPresent() && requestRepository.findById(requestId).isPresent() )
        {
            //documento no puede pertenecer a un cliente y a un request a la vez, retorna nulo
            return null;
        }
        if(file != null)
        {
            DocumentEntity document = new DocumentEntity();
            document.setName(file.getOriginalFilename());
            document.setType(file.getContentType());
            document.setFile(file.getBytes());
            if(clientRepository.findById(clientId).isPresent())
            {
                ClientEntity client = clientRepository.findById(clientId).get();
                document.setClient(client);
            }
            if(requestRepository.findById(requestId).isPresent())
            {
                RequestEntity request = requestRepository.findById(requestId).get();
                document.setRequest(request);
            }
            return documentRepository.save(document);
        }
        else
        {
            return  null;
        }
    };

}
