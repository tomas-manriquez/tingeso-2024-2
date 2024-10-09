package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.List;

@Entity
@Table(name ="clients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private String rut;
    private String firstName;
    private String lastName;
    //birthday YYYY-MM-DD
    private String birthday;
    //'state' marca si esta en 'espera', 'validado'
    // o 'rechazado' como usuario
    private String status;

    @OneToMany(mappedBy = "client")
    private List<DocumentEntity> documents;

}
