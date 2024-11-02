package com.example.demo.repository;

import com.example.demo.entity.DocumentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DocumentRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DocumentRepository documentRepository;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'save'

    //Test 1
    @Test
    void whenSave_validDocument_documentIsSaved() {
        // Given: 1 documento valido en DB
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);

        // When: aplicar 'save'
        DocumentEntity savedDocument = documentRepository.save(document);

        // Then: asegurar que documento esta en DB y tiene ID no nulo
        assertNotNull(savedDocument.getId(), "espera ID de documento no nulo");
        assertEquals("Document1", savedDocument.getName(), "espera que nombre de documento guardado es igual al original");
    }


    //Test 2
    @Test
    void whenSave_nullDocument_throwException() {
        // When: aplicar 'save' a document = null
        Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            documentRepository.save(null);
        });

        // Then: asegurar que retorna exception
        assertNotNull(exception, "espera InvalidDataAccessApiUsageException");
    }

    //Test 3
    @Test
    void whenSave_existingDocument_updatesDocument() {
        // Given: guardar 1 documento en DB
        DocumentEntity document = new DocumentEntity(null, "Document2", "application/pdf", new byte[]{}, null, null);
        DocumentEntity savedDocument = documentRepository.save(document);

        // When: actualizar via 'save'
        savedDocument.setName("UpdatedDocument2");
        DocumentEntity updatedDocument = documentRepository.save(savedDocument);

        // Then: asegurar que se actualiza en DB
        assertEquals("UpdatedDocument2", updatedDocument.getName(), "espera nombre actualizado del documento");
    }

    //Test 4
    @Test
    void whenSave_documentWithNullFileField_documentIsSaved() {
        // Given: 1 documento con file = null
        DocumentEntity document = new DocumentEntity(null, "Document4", "application/pdf", null, null, null);

        // When: aplicar 'save'
        DocumentEntity savedDocument = documentRepository.save(document);

        // Then: asegurar que se guardo exitosamente en DB
        assertNotNull(savedDocument.getId(), "espera ID de documento no nulo");
        assertEquals("Document4", savedDocument.getName(), "espera nombre de documento guardado igual al original");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findAll'

    //Test 1
    @Test
    void whenFindAll_withNoDocuments_returnEmptyList() {
        // Given: DB sin documentos
        documentRepository.deleteAll();

        // When: aplicar 'findAll'
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que retorna lista vacia
        assertTrue(documents.isEmpty(), "espera List vacia");
    }

    //Test 2
    @Test
    void whenFindAll_withSingleDocument_returnSingleDocument() {
        // Given: 1 documento en DB
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(document);

        // When: aplicar 'findAll'
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que retorna lista con 1 documento
        assertEquals(1, documents.size(), "espera List con 1 documento");
        assertEquals("Document1", documents.get(0).getName(), "espera que nombre de documento en lista tenga mismo nombre que el original");
    }

    //Test 3
    @Test
    void whenFindAll_withMultipleDocuments_returnAllDocuments() {
        // Given: 2 documentos en DB
        DocumentEntity doc1 = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc1);
        DocumentEntity doc2 = new DocumentEntity(null, "Document2", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc2);

        // When: aplicar 'findAll'
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que lista contiene ambos documentos
        assertEquals(2, documents.size(), "espera lista con 2 documentos");
        assertTrue(documents.stream().anyMatch(d -> d.getName().equals("Document1")), "espera Document1 en la lista");
        assertTrue(documents.stream().anyMatch(d -> d.getName().equals("Document2")), "espera Document2 en la lista");
    }

    //Test 4
    @Test
    void whenFindAll_withDifferentTypes_returnCorrectTypes() {
        // Given: 2 documentos en DB, c/u con tipos MIME distintos
        DocumentEntity doc1 = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc1);
        DocumentEntity doc2 = new DocumentEntity(null, "Document2", "image/png", new byte[]{}, null, null);
        documentRepository.save(doc2);

        // When: Call findAll
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que documentos en lista tienen los tipos MIME originales
        assertEquals(2, documents.size(), "espera lista con 2 documentos");
        assertTrue(documents.stream().anyMatch(d -> "application/pdf".equals(d.getType())), "espera documento tipo PDF");
        assertTrue(documents.stream().anyMatch(d -> "image/png".equals(d.getType())), "espera documento tipo PNG");
    }

    //Test 5
    @Test
    void whenFindAll_withLargeNumberOfDocuments_returnAllDocuments() {
        // Given: guardar 100 documentos en DB
        for (int i = 1; i <= 100; i++) {
            DocumentEntity document =new DocumentEntity(null, "Document" + i, "application/pdf", new byte[]{}, null, null);
            documentRepository.save(document);
        }

        // When: aplicar 'findAll'
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que retorna lista de largo 100
        assertEquals(100, documents.size(), "espera lista de largo 100");
    }

    //Test 6
    @Test
    void whenFindAll_afterDeletingSomeDocuments_returnRemainingDocuments() {
        // Given: guardar 2 documentos y luego borrar 1er documento en DB
        DocumentEntity doc1 = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc1);
        DocumentEntity doc2 = new DocumentEntity(null, "Document2", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc2);
        documentRepository.delete(doc1);

        // When: aplicar 'findAll'
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que retorna lista con solamente doc2
        assertEquals(1, documents.size(), "espera lista de largo 1");
        assertEquals("Document2", documents.get(0).getName(), "espera que documento en lista es Document2");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS para 'findById'

    //Test 1
    @Test
    void whenFindById_withValidId_returnDocument() {
        // Given: 1 documento en DB + su id (via save)
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        DocumentEntity savedDocument = documentRepository.save(document);

        // When: aplicar 'findById'
        Optional<DocumentEntity> foundDocument = documentRepository.findById(savedDocument.getId());

        // Then: asegurar que retorna el documento original
        assertTrue(foundDocument.isPresent(), "espera documento con ID original");
        assertEquals("Document1", foundDocument.get().getName(), "espera que nombre de documento retornado sea igual al del documento original");
    }

    //Test 2
    @Test
    void whenFindById_withInvalidId_returnEmpty() {
        // Given: no existe documento en DB con id = 999
        Long invalidId = 999L;

        // When: aplicar 'findById'
        Optional<DocumentEntity> foundDocument = documentRepository.findById(invalidId);

        // Then: asegurar que retorna Optional.isEmpty()
        assertTrue(foundDocument.isEmpty(), "espera Optional.isEmpty()");
    }

    //Test 3
    @Test
    void whenFindById_withNullId_throwException() {
        // Given: ID = null

        // When & Then: al aplicar 'findById', este retorna InvalidDataAccessApiUsageException
        assertThrows(InvalidDataAccessApiUsageException.class, () -> documentRepository.findById(null),
                "espera InvalidDataAccessApiUsageException");
    }

    //Test 4
    @Test
    void whenFindById_withMultipleDocuments_returnCorrectDocument() {
        // Given: 2 documentos en DB
        DocumentEntity doc1 = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc1);
        DocumentEntity doc2 = new DocumentEntity(null, "Document2", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(doc2);

        // When: aplicar 'findById' con id de doc2
        Optional<DocumentEntity> foundDocument = documentRepository.findById(doc2.getId());

        // Then: Assert that the correct document is returned
        assertTrue(foundDocument.isPresent(), "espera documento con mismo ID que Document2");
        assertEquals("Document2", foundDocument.get().getName(), "espera mismo nombre que Document2");
    }

    //Test 5
    @Test
    void whenFindById_withMaxValueId_returnEmpty() {
        // Given: ID no existente en DB con valor borde
        Long maxId = Long.MAX_VALUE;

        // When: aplicar 'findById' con valor borde
        Optional<DocumentEntity> foundDocument = documentRepository.findById(maxId);

        // Then: asegurar que retorna Optional.isEmpty()
        assertTrue(foundDocument.isEmpty(), "espera Optional.isEmpty()");
    }

    //Test 6
    @Test
    void whenFindById_afterDocumentDeletion_returnEmpty() {
        // Given: guardar 1 documento en DB y luego borrarlo
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        DocumentEntity savedDocument = documentRepository.save(document);
        documentRepository.deleteById(savedDocument.getId());

        // When: aplicar 'findById'
        Optional<DocumentEntity> foundDocument = documentRepository.findById(savedDocument.getId());

        // Then: verificar que retorna Optional.isEmpty()
        assertTrue(foundDocument.isEmpty(), "espera Optional.isEmpty()");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TEST para 'deleteById'

    //Test 1
    @Test
    void whenDeleteById_withExistingId_documentIsDeleted() {
        // Given: 1 documento en DB
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        DocumentEntity savedDocument = documentRepository.save(document);

        // When: aplicar 'deleteById'
        documentRepository.deleteById(savedDocument.getId());

        // Then: asegurar que retorna Optional.isEmpty()
        Optional<DocumentEntity> foundDocument = documentRepository.findById(savedDocument.getId());
        assertTrue(foundDocument.isEmpty(), "espera Optional.isEmpty()");
    }

    //Test 2
    @Test
    void whenDeleteById_withNonExistentId_noExceptionThrown() {
        // Given: ID no guardado en DB
        Long nonExistentId = 999L;

        // When & Then: aplicar 'deleteById' y asegurar que no retorna exception
        assertDoesNotThrow(() -> documentRepository.deleteById(nonExistentId),
                "espera que no haya exception");
    }

    //Test 3
    @Test
    void whenDeleteById_withNullId_throwException() {
        // Given: id = null

        // When & Then: Expect IllegalArgumentException
        assertThrows(InvalidDataAccessApiUsageException.class, () -> documentRepository.deleteById(null),
                "espera InvalidDataAccessApiUsageException");
    }

    //Test 4
    @Test
    void whenDeleteById_andFindAll_noDocumentsReturned() {
        // Given: guardar 1 documento en DB y luego se elimina
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        DocumentEntity savedDocument = documentRepository.save(document);
        documentRepository.deleteById(savedDocument.getId());

        // When: aplicar 'findAll'
        List<DocumentEntity> documents = documentRepository.findAll();

        // Then: asegurar que retorna lista sin documento eliminado
        assertFalse(documents.stream().anyMatch(d -> d.getId().equals(savedDocument.getId())),
                "Expected the deleted document to be absent in the result list");
    }

    //Test 5
    @Test
    void whenDeleteById_multipleDocuments_onlySpecifiedDocumentDeleted() {
        // Given: 2 documentos en DB
        DocumentEntity document1 = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(document1);
        DocumentEntity document2 = new DocumentEntity(null, "Document2", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(document2);

        // When: Delete only the first document
        documentRepository.deleteById(document1.getId());

        // Then: Assert the first document is deleted and the second document still exists
        assertTrue(documentRepository.findById(document1.getId()).isEmpty(),
                "espera Optional.isEmpty()");
        assertTrue(documentRepository.findById(document2.getId()).isPresent(),
                "espera Optional.isPresent()");
    }

    //Test 6
    @Test
    void whenDeleteById_thenRepositoryCountIsUpdated() {
        // Given: guardar 1 documento en DB y contar total de documentos en DB
        DocumentEntity document = new DocumentEntity(null, "Document1", "application/pdf", new byte[]{}, null, null);
        documentRepository.save(document);
        long initialCount = documentRepository.count();

        // When: aplicar 'deleteById'
        documentRepository.deleteById(document.getId());

        // Then: asegurar que contador se actualiza tras 'deleteById'
        assertEquals(initialCount - 1, documentRepository.count(), "espera que contador se reduzca por 1");
    }

}
