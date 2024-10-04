package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="executives")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private String rut;
    private String first_name;
    private String last_name;
    private String password;

}
