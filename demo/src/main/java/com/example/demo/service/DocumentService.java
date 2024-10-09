package com.example.demo.service;

import com.example.demo.entity.DocumentEntity;
import com.example.demo.entity.RequestEntity;
import com.example.demo.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService{
    @Autowired
    DocumentRepository documentRepository;

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

    public DocumentEntity store(MultipartFile file) throws IOException
    {
        if(file != null)
        {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            /**
            DocumentEntity newFile = new DocumentEntity(
                    fileName,
                    file.getContentType(),
                    file.getBytes(),
                    null,
                    null);
            return documentRepository.save(newFile);
             **/
            return null;
        }
        else
        {
            return  null;
        }
    };



}
