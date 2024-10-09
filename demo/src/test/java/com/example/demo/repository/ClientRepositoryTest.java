package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    /**
    @Test
    public void whenFindByRut_thenReturnClient()
    {
        //given
        ClientEntity clientEntity = new ClientEntity(
                null,"12345678-9", "Alex", "Campos", "2000-06-20", "aprobado");
        entityManager.persistAndFlush(clientEntity);

        //when
        ClientEntity found = clientRepository.findByRut(clientEntity.getRut());

        //then
        assertThat(found.getRut()).isEqualTo(clientEntity.getRut());
    }
    **/
}
