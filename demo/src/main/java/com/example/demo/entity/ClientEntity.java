package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.List;

@Entity
@Table(name ="clients")
@Data
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
    private boolean hasValidDocuments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DocumentEntity> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentEntity> documents) {
        this.documents = documents;
    }

    public boolean isHasValidDocuments() {
        return hasValidDocuments;
    }

    public void setHasValidDocuments(boolean hasValidDocuments) {
        this.hasValidDocuments = hasValidDocuments;
    }

    public ClientEntity() {
    }

    public ClientEntity(Long id, String rut, String firstName, String lastName, String birthday, String status, List<DocumentEntity> documents, boolean hasValidDocuments) {
        this.id = id;
        this.rut = rut;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.status = status;
        this.documents = documents;
        this.hasValidDocuments = hasValidDocuments;
    }
}
