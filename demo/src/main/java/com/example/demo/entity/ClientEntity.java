package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String first_name;
    private String last_name;
    private Integer age;
    //'state' marca si esta en 'espera', 'validado'
    // o 'rechazado' como usuario
    private String state;
    @OneToOne
    private DocumentEntity idFile;
    @OneToOne
    private DocumentEntity incomeFile;

}
