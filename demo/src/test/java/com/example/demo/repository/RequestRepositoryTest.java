package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.RequestEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RequestRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RequestRepository requestRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findByClientRut'
    @Test
    public void whenExistingClientRut_thenReturnRequest() {
        // given
        RequestEntity request = new RequestEntity();
        request.setClientRut("12.345.678-9");
        request.setType("vivienda1");
        request.setMaxPayTerm(30);
        request.setAnnualInterest(3.5F);
        request.setMaxFinanceAmount(1000000F);
        requestRepository.save(request);

        // when
        Optional<RequestEntity> foundRequest = requestRepository.findByClientRut("12.345.678-9");

        // then
        assertTrue(foundRequest.isPresent(), "espera Optional.isPresent()");
        assertEquals("12.345.678-9", foundRequest.get().getClientRut(), "espera que Request encontrado tenga rut original");
    }

    //Test 2
    @Test
    public void whenNonExistingClientRut_thenReturnEmpty() {
        // when
        Optional<RequestEntity> foundRequest = requestRepository.findByClientRut("non-existing-rut");

        // then
        assertTrue(foundRequest.isEmpty(), "espera Optional.isEmpty");
    }

    //Test 3
    @Test
    public void whenNullClientRut_thenThrowException() {
        // When: aplicar 'findByClientRut' con rut = null
        Optional<RequestEntity> foundClient = requestRepository.findByClientRut(null);

        //Then: validar que retorna Optional.isEmpty()
        assertTrue(foundClient.isEmpty(), "espera Optional.isEmpty()");
    }

    //Test 4
    @Test
    void whenMultipleClientsHaveSameRut_returnsFirst() {
        // Given: guardar 2 solicitud con mismo rut en DB
        RequestEntity request1 = new RequestEntity();
        request1.setClientRut("12.345.678-9");
        request1.setType("vivienda1");
        request1.setMaxPayTerm(30);
        request1.setAnnualInterest(3.5F);
        request1.setMaxFinanceAmount(1000000F);
        requestRepository.save(request1);

        RequestEntity request2 = new RequestEntity();
        request1.setClientRut("12.345.678-9");
        request1.setType("vivienda2");
        request1.setMaxPayTerm(20);
        request1.setAnnualInterest(5.0F);
        request1.setMaxFinanceAmount(1000000F);
        requestRepository.save(request2);

        // When: aplicar 'findByRut'
        Optional<RequestEntity> foundRequest = requestRepository.findByClientRut("12.345.678-9");

        // Then: Validate that the first client is returned
        assertEquals(request1.getId(), foundRequest.get().getId(), "id de request creado debe coincidir con encontrado");
    }

    //Test 5
    @Test
    void whenRutIsEmpty_returnOptionalIsEmpty() {
        // When: aplicar 'findByRut' con rut = ""
        Optional<RequestEntity> foundClient = requestRepository.findByClientRut("");

        //Then: validar que retorna Optional.isEmpty()
        assertTrue(foundClient.isEmpty(), "espera Optional.isEmpty()");
    }
}
