package com.salesianos.dam.primerjemplo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MonumentControllerTest {

    @Autowired
    private MonumentController monumentController;

    @Autowired
    private MonumentRepository monumentRepository;

    @BeforeEach
    void setUp() {
        monumentRepository.deleteAll();
    }

    @Test
    void shouldCreateAndGetMonument() {
        Monument monument = new Monument();
        monument.setCodigoPais("ES");
        monument.setNombrePais("España");
        monument.setCiudad("Sevilla");
        monument.setLatitud(37.3851);
        monument.setLongitud(-5.9925);
        monument.setNombre("Giralda");
        monument.setDescripcion("Torre campanario de la Catedral de Sevilla.");
        monument.setUrlFoto("https://example.com/giralda.jpg");

        ResponseEntity<Monument> createResponse = monumentController.createMonument(monument);
        assertEquals(201, createResponse.getStatusCode().value());
        assertNotNull(createResponse.getBody());

        ResponseEntity<List<Monument>> listResponse = monumentController.getAllMonuments();
        assertEquals(200, listResponse.getStatusCode().value());
        assertEquals("ES", listResponse.getBody().get(0).getCodigoPais());
    }

    @Test
    void shouldReturnNotFoundWhenNoMonuments() {
        ResponseEntity<List<Monument>> response = monumentController.getAllMonuments();
        assertEquals(404, response.getStatusCode().value());
    }
}
