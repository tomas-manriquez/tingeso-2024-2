package com.example.demo.repository;

import com.example.demo.entity.ClientEntity;
import com.example.demo.entity.DocumentEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
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

    @Autowired
    private DocumentRepository documentRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findByRut'

    //Test 1
    @Test
    public void whenFindByRut_thenReturnClient() {
        //given
        ClientEntity clientEntity = new ClientEntity(
                null,
                "12.345.678-9",
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

    //Test 2
    @Test
    void whenNoClientExistsWithGivenRut_returnEmpty() {
        // When: buscar cliente con Rut que no se encuentra en DB
        Optional<ClientEntity> foundClient = clientRepository.findByRut("nonexistent-rut");

        // Then: asegurar que retorna Null
        assertTrue(foundClient.isEmpty(), "espera retornar Optional.isEmpty()");
    }

    //Test 3
    @Test
    void whenMultipleClientsHaveSameRut_throwException() {
        // Given: guardar 2 clientes con mismo rut en DB
        ClientEntity client1 = new ClientEntity(null, "12.345.678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        ClientEntity client2 = new ClientEntity(null, "12.345.678-9", "Bob", "Smith", "1990-04-04", "espera", null, false);
        clientRepository.save(client1);
        clientRepository.save(client2);

        // When: aplicar 'findByRut'
        Exception exception = assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            clientRepository.findByRut("12.345.678-9");
        });

        // Then: Validate that the first client is returned
        assertNotNull(exception, "espera IncorrectResultSizeDataAccessException ");
        assertEquals("Query did not return a unique result: 2 results were returned", exception.getMessage(), "espera mensaje de IncorrectResultSizeDataAccessException");
    }

    //Test 4
    @Test
    void whenRutIsNull_returnOptionalIsEmpty() {
        // When: aplicar 'findByRut' con rut = null
        Optional<ClientEntity> foundClient = clientRepository.findByRut(null);

        //Then: validar que retorna Optional.isEmpty()
        assertTrue(foundClient.isEmpty(), "espera Optional.isEmpty()");
    }

    //Test 5
    @Test
    void whenRutIsEmpty_returnOptionalIsEmpty() {
        // When: aplicar 'findByRut' con rut = ""
        Optional<ClientEntity> foundClient = clientRepository.findByRut("");

        //Then: validar que retorna Optional.isEmpty()
        assertTrue(foundClient.isEmpty(), "espera Optional.isEmpty()");
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
        ClientEntity client = new ClientEntity(null, "12.345.678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        clientRepository.save(client);

        Long clientId = client.getId();

        // When: Retrieve the client using the repository
        ClientEntity foundClient = clientRepository.findById(clientId).orElse(null);

        // Then: Validate that the retrieved client matches the expected one
        assertNotNull(foundClient, "se espera encontrar al cliente creado");
        assertEquals(clientId, foundClient.getId(), "se espera que cliente encontrado tenga mismo ID que cliente creado");

        //Crear otro cliente
        ClientEntity anotherClient = new ClientEntity(null, "98.765.432-1", "Bob", "White", "1995-04-04", "rechazado", null, false);
        clientRepository.save(anotherClient);

        // Validate that the original client is still retrievable and has not been affected
        // Validar que el 1er cliente aun se puede recuperar y no fue afectado
        ClientEntity retrievedClient = clientRepository.findById(clientId).orElse(null);
        assertNotNull(retrievedClient, "se espera que 1er cliente aun se pueda recuperar");
        assertEquals("Alice", retrievedClient.getFirstName(), "se espera que datos de 1er cliente no hayan cambiado");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findAll'

    //Test 1
    @Test
    void whenClientsExist_returnAllClients() {
        // Given: Guardar solo 2 clientes en DB
        ClientEntity client1 = new ClientEntity(null, "12.345.678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        ClientEntity client2 = new ClientEntity(null, "87.654.321-0", "Bob", "Smith", "1990-04-04", "espera", null, false);
        clientRepository.save(client1);
        clientRepository.save(client2);

        // When: aplicar 'findAll'
        List<ClientEntity> clients = clientRepository.findAll();

        // Then: asegurar que se encontro exactamente 2 clientes
        assertEquals(2, clients.size(), "espera retornar 2 clientes");
    }

    //Test 2
    @Test
    void whenNoClientsExist_returnEmptyList() {
        // When: aplicar 'findAll'
        List<ClientEntity> clients = clientRepository.findAll();

        // Then: retornar lista vacia
        assertTrue(clients.isEmpty(), "espera retornar lista vacia");
    }

    //Test 3
    @Test
    void whenClientsExist_checkCorrectness() {
        // Given: guardar 2 clientes en DB
        ClientEntity client1 = new ClientEntity(null, "12345678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        ClientEntity client2 = new ClientEntity(null, "87654321-0", "Bob", "Smith", "1990-04-04", "espera", null, false);
        clientRepository.save(client1);
        clientRepository.save(client2);

        // When: aplicar 'findAll'
        List<ClientEntity> clients = clientRepository.findAll();

        // Then: Validar que se retorna lista de 2 clientes, y c/u calza con datos ingresados
        assertEquals(2, clients.size(), "Se espera lista de 2 clientes");
        assertEquals("Alice", clients.get(0).getFirstName(), "espera 1er cliente de nombre Alice");
        assertEquals("Bob", clients.get(1).getFirstName(), "espera 2do cliente de nombre Bob");
    }

    //Test 4
    @Test
    void whenAddingNewClient_checkThatFindAllIncludesIt() {
        // Given: guardar 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "12.345.678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        clientRepository.save(client);

        // When: aplicar 'findAll'
        List<ClientEntity> clients = clientRepository.findAll();

        // Then: Asegurar que lista incluye cliente agregado
        assertEquals(1, clients.size(), "espera lista de 1 cliente");
        assertEquals("Alice", clients.get(0).getFirstName(), "espera cliente de nombre Alice");
    }

    //Test 5
    @Test
    void whenRemovingClient_checkThatFindAllExcludesIt() {
        // Given: guardar 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "12.345.678-9", "Alice", "Brown", "1985-03-03", "validado", null, false);
        clientRepository.save(client);

        // When: eliminar el cliente
        clientRepository.delete(client);

        // Then: asegurar que 'findAll' retorna lista sin el cliente
        List<ClientEntity> clients = clientRepository.findAll();
        assertTrue(clients.isEmpty(), "espera lista vacia");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'save'

    //Test 1
    @Test
    void whenValidClient_returnSavedClient() {
        // Given: cliente valido
        ClientEntity client = new ClientEntity(null, "12.345.678-9", "John", "Doe", "1985-01-01", "validado", null, true);


        // When: guardar cliente via 'save'
        ClientEntity savedClient = clientRepository.save(client);

        // Then: asegurar que se guarda en DB con ID valido, y datos de cliente se mantienen
        assertNotNull(savedClient.getId(), "ID de cliente guardado debe ser no nulo");
        assertEquals("12.345.678-9", savedClient.getRut(), "cliente guardado en DB debe tener mismo rut que antes de guardarse");
    }

    //Test 2
    @Test
    void whenUpdateClient_returnUpdatedClient() {
        // Given: crear y guardar 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "55.555.555-5", "George", "Jetson", "1980-01-01", "validado", null, false);
        ClientEntity savedClient = clientRepository.save(client);

        // When: cambiar apellido y actualizar via 'save'
        savedClient.setLastName("UpdatedJetson");
        ClientEntity updatedClient = clientRepository.save(savedClient);

        // Then: asegurar que en DB se guarda el apellido nuevo
        assertEquals("UpdatedJetson", updatedClient.getLastName(), "asegurar que cliente tiene apellido nuevo");
    }

    //Test 3
    @Test
    void whenSaveMultipleClients_returnUniqueIds() {
        // Given: Create two valid clients
        ClientEntity client1 = new ClientEntity(null, "11111111-1", "Daisy", "White", "1980-01-01", "validado", null, false);
        ClientEntity client2 = new ClientEntity(null, "22222222-2", "Eve", "Black", "1992-01-01", "validado", null, false);

        // When: Save both clients
        ClientEntity savedClient1 = clientRepository.save(client1);
        ClientEntity savedClient2 = clientRepository.save(client2);

        // Then: Verify that both clients have unique IDs
        assertNotEquals(savedClient1.getId(), savedClient2.getId(), "Client IDs should be unique");
    }

    //Test 4
    @Test
    void whenUpdateClientId_remainsSame() {
        // Given: guardar 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "55.555.555-5", "George", "Jetson", "1980-01-01", "validado", null, false);
        ClientEntity savedClient = clientRepository.save(client);
        Long originalId = savedClient.getId();

        // When: actualizar apellido
        savedClient.setLastName("UpdatedJetson");
        ClientEntity updatedClient = clientRepository.save(savedClient);

        // Then: asegurar que ID de cliente no cambia
        assertEquals(originalId, updatedClient.getId(), "espera que ID de cliente no cambie al usar save para actualizar");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'deleteById'

    //Test 1
    @Test
    void whenDeleteById_existingId_clientIsDeleted() {
        // Given: guardar 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "12.345.678-9", "Alice", "Johnson", "1992-03-14", "validado", null, false);
        ClientEntity savedClient = clientRepository.save(client);

        // When: aplicar 'deleteById'
        clientRepository.deleteById(savedClient.getId());

        // Then: verificar que cliente se elimina de DB
        Optional<ClientEntity> deletedClient = clientRepository.findById(savedClient.getId());
        assertTrue(deletedClient.isEmpty(), "espera Optional.isEmpty()");
    }

    //Test 2
    @Test
    void whenDeleteById_nonExistingId_doesntAffectOtherClients() {
        //Given: 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "12.345.678-9", "Alice", "Johnson", "1992-03-14", "validado", null, false);
        ClientEntity savedClient = clientRepository.save(client);
        Long clientId = savedClient.getId();

        //When: aplicar 'deleteById' con ID no existente
        clientRepository.deleteById(99L);

        //Then: se encuentra el cliente guardado inicialmente
        Optional<ClientEntity> foundClient = clientRepository.findById(clientId);
        assertTrue(foundClient.isPresent(), "se espera Optional.isPresent()");
    }

    //Test 3
    @Test
    void whenDeleteById_nullId_throwException() {
        // When: aplicar 'deleteById' con id = null
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            clientRepository.deleteById(null);
        });

        // Then: asegurar que retorna exception
        assertNotNull(exception, "espera InvalidDataAccessApiUsageException");
    }

    //Test 4
    @Test
    void whenDeleteById_multipleClients_deleteCorrectClient() {
        // Given: guardar 2 clientes en DB
        ClientEntity client1 = new ClientEntity(null, "12.345.678-9", "Bob", "Smith", "1995-06-10", "validado", null, false);
        ClientEntity client2 = new ClientEntity(null, "98.765.432-1", "Charlie", "Brown", "1988-09-12", "espera", null, false);
        clientRepository.save(client1);
        ClientEntity savedClient2 = clientRepository.save(client2);

        // When: aplicar 'deleteById' a cliente2
        clientRepository.deleteById(savedClient2.getId());

        // Then: verificar que 2do cliente ya no existe pero 1er cliente si existe
        Optional<ClientEntity> remainingClient = clientRepository.findById(client1.getId());
        Optional<ClientEntity> deletedClient = clientRepository.findById(savedClient2.getId());

        assertTrue(remainingClient.isPresent(), "espera encontrar 1er cliente");
        assertTrue(deletedClient.isEmpty(), "espera no encontrar 2do cliente");
    }

    //Test 5
    @Test
    void whenDeleteById_clientHasRelatedDocuments_documentsNotDeleted() {
        // Given: 1 cliente en DB
        ClientEntity client = new ClientEntity(null, "12345678-9", "Diana", "Prince", "1990-05-15", "validado", null, false);
        ClientEntity savedClient = clientRepository.save(client);

        // bytes minimos para cuerpo de archivo
        byte[] CDRIVES = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");

        DocumentEntity document = new DocumentEntity(null, "Doc1", "application/pdf", CDRIVES, savedClient, null);
        documentRepository.save(document);

        // When: aplicar 'deleteById'
        clientRepository.deleteById(savedClient.getId());

        // Then: asegurar que documento no se elimina
        Optional<DocumentEntity> deletedDocument = documentRepository.findById(document.getId());
        assertTrue(deletedDocument.isPresent(), "espera encontrar el documento");
    }

}