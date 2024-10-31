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
@NoArgsConstructor
@AllArgsConstructor
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

}
