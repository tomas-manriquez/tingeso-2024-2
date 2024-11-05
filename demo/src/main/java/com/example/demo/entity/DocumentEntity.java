package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name="documents")
@Data

public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable = false)
    private Long id;
    private String name;
    private String type;
    @Lob
    private byte[] file;
    //para Client
    @ManyToOne(fetch = FetchType.LAZY)
    private ClientEntity client;
    //para Request
    @ManyToOne(fetch = FetchType.LAZY)
    private RequestEntity request;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
    }

    public RequestEntity getRequest() {
        return request;
    }

    public void setRequest(RequestEntity request) {
        this.request = request;
    }

    public DocumentEntity() {
    }

    public DocumentEntity(Long id, String name, String type, byte[] file, ClientEntity client, RequestEntity request) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.file = file;
        this.client = client;
        this.request = request;
    }
}