package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class ClientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findByRut'
    @Test
    public void whenFindByRut_thenReturnClient()
    {
        //given
        ClientEntity clientEntity = new ClientEntity(
                null,
                "12345678-9",
                "Alex",
                "Campos",
                "2000-06-20",
                "aprobado",
                null,
                true);
        entityManager.persistAndFlush(clientEntity);

        //when
        ClientEntity found = clientRepository.findByRut(clientEntity.getRut()).get();

        //then
        assertThat(found.getRut()).isEqualTo(clientEntity.getRut());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findById'

    //Test 1
    @Test
    public void whenFindById_thenReturnClient() {
        // Given: 1 cliente en DB
        ClientEntity client = new ClientEntity();
        //aun con constructor sin argumentos, id no es nulo
        client.setRut("12.345.678-9");
        client.setFirstName("Alex");
        client.setLastName("Campos");
        client.setBirthday("1990-01-01");
        client.setStatus("validado");
        client = clientRepository.save(client);
        Long clientId = client.getId();

        // When: se busca el cliente en DB
        Optional<ClientEntity> foundClient = clientRepository.findById(clientId);

        // Then: se encuentra cliente en DB y datos calzan con el del recien creado
        assertTrue(foundClient.isPresent(), "Cliente debe estar presente");
        assertEquals(client.getId(), foundClient.get().getId(), "id de cliente creado debe coincidir con encontrado");
        assertEquals(client.getRut(), foundClient.get().getRut(), "rut de cliente creado debe coincidir con encontrado");
        assertEquals(client.getFirstName(), foundClient.get().getFirstName(), "nombre de cliente creado debe coincidir con encontrado");
        assertEquals(client.getLastName(), foundClient.get().getLastName(), "apellido de cliente creado debe coincidir con encontrado");
        assertEquals(client.getBirthday(), foundClient.get().getBirthday(), "cumpleaños de cliente creado debe coincidir con encontrado");
        assertEquals(client.getStatus(), foundClient.get().getStatus(), "status de cliente creado debe coincidir con encontrado");
    }

    //Test 2
    @Test
    void whenInvalidId_returnEmpty() {
        // Given: ID que se sabe a priori no existe en DB
        Long invalidId = 999L;  // Assuming this ID does not exist in the database

        // When: se intenta buscar cliente con tal ID en DB
        Optional<ClientEntity> result = clientRepository.findById(invalidId);

        // Then: retorna Optional.empty()
        assertTrue(result.isEmpty(), "se espera Optional.empty() si ID no se encuentra en DB");
    }

    //Test 3
    @Test
    void whenNullId_returnInvalidDataAccessApiUsageException() {
        // Given: ID nulo
        Long nullId = null;

        // When & Then: se intenta buscar en DB, retorna Exception
        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            clientRepository.findById(nullId);
        }, "Se espera InvalidDataAccessApiUsageException cuando findById se usa con ID = null");
    }

    //Test 4
    @Test
    void whenMultipleClients_returnCorrectId() {
        // Given: 2+ clientes en BD
        ClientEntity client1 = new ClientEntity(null, "12.345.678-9", "John", "Doe", "1980-01-01", "validado", null, true);
        ClientEntity client2 = new ClientEntity(null, "87.654.321-0", "Jane", "Smith", "1990-02-02", "espera", null, true);

        clientRepository.save(client1);
        clientRepository.save(client2);

        Long expectedClientId = client1.getId(); //en cualquier caso, client1.id != client2.id

        // When
        ClientEntity foundClient = clientRepository.findById(expectedClientId).orElse(null);

        // Then
        assertNotNull(foundClient, "se espera encontrar un cliente");
        assertEquals(expectedClientId, foundClient.getId(), "se espera que el cliente encontrado tenga el ID dado");
    }

    //Test 5
    @Test
    void whenCalled_repositoryIsIsolated() {
        // Given: Create and save a client
        ClientEntity client = new ClientEntity(null, "12345678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        clientRepository.save(client);

        Long clientId = client.getId();

        // When: Retrieve the client using the repository
        ClientEntity foundClient = clientRepository.findById(clientId).orElse(null);

        // Then: Validate that the retrieved client matches the expected one
        assertNotNull(foundClient, "se espera encontrar al cliente creado");
        assertEquals(clientId, foundClient.getId(), "se espera que cliente encontrado tenga mismo ID que cliente creado");

        //Crear otro cliente
        ClientEntity anotherClient = new ClientEntity(null, "98765432-1", "Bob", "White", "1995-04-04", "rechazado", null, false);
        clientRepository.save(anotherClient);

        // Validate that the original client is still retrievable and has not been affected
        // Validar que el 1er cliente aun se puede recuperar y no fue afectado
        ClientEntity retrievedClient = clientRepository.findById(clientId).orElse(null);
        assertNotNull(retrievedClient, "se espera que 1er cliente aun se pueda recuperar");
        assertEquals("Alice", retrievedClient.getFirstName(), "se espera que datos de 1er cliente no hayan cambiado");
    }
}
