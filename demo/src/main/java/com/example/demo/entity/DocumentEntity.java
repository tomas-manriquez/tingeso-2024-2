package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String type;
    //TODO confirmar tipo de dato de 'file'
    private String file;
    private String state;
}
