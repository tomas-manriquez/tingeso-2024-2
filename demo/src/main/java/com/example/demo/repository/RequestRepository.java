package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    public Optional<RequestEntity> findByClientRut(String rut);
}
