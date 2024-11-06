package com.example.demo.controller;

import com.example.demo.entity.DocumentEntity;
import com.example.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin("*")
public class DocumentController {
    @Autowired
    DocumentService documentService;

    //METODOS PARA CRUD

    //CREATE documento
    //Entrada: objeto DocumentEntity
    //Salida: estado OK
    @PostMapping("/")
    public ResponseEntity<DocumentEntity> saveDocument(@RequestBody DocumentEntity document)
    {
        DocumentEntity documentNew = documentService.saveDocument(document);
        return ResponseEntity.ok(documentNew);
    }

    //READ para todos los documentos
    //Entrada: nada
    //Salida: estado OK
    @GetMapping("/")
    public ResponseEntity<List<DocumentEntity>> listDocuments()
    {
        List<DocumentEntity> documentEntityList = documentService.getAll();
        return ResponseEntity.ok(documentEntityList);
    }

    //READ documento por su id de base de datos
    //Entrada: Long id
    //Salida: estado OK si se encuentra el documento en la base de datos, estado NOT_FOUND en otro caso
    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getDocumentById(@PathVariable("id") Long id)
    {
        DocumentEntity document = documentService.getDocumentById(id);
        return ResponseEntity.ofNullable(document);
    }

    //UPDATE cliente en cualquiera de sus atributos
    //Entrada: objeto DocumentEntity con cambios
    //Salida: estado OK
    @PutMapping("/update")
    public ResponseEntity<DocumentEntity> updateDocument(DocumentEntity document)
    {
        DocumentEntity documentUpdated = documentService.updateDocument(document);
        return ResponseEntity.ok(documentUpdated);
    }

    //DELETE de documento por su id en la base de datos
    //Entrada: Long id
    //Salida: 'true' si se borra con exito, Exception en otro caso
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteDocumentById(@PathVariable("id") Long id) throws Exception
    {
        var isDeleted = documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }

    //METODOS PARA REGLAS DE NEGOCIO

    //Recibe un MultipartFile (pdf) y lo guarda en la base de datos
    //Entrada: MultipartFile, clientId, requestId
    //Salida: Estado OK. DocumentEntity
    @PostMapping("/upload")
    public ResponseEntity<DocumentEntity> store(@RequestBody MultipartFile file,
                                                @RequestParam Long clientId,
                                                @RequestParam Long requestId) throws IOException
    {
        DocumentEntity document = documentService.store(file, clientId, requestId);
        return ResponseEntity.ok(document);
    }

    //READ documento por su id en la base de datos
    //Entrada: Long id
    //Salida: Estado OK. Archivo pdf si se encuentra en base de datos, en otro caso null
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id)
    {
            DocumentEntity document = documentService.getDocumentById(id);


            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(document.getType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getName() + "\"")
                    .body(document.getFile());
    }
}
