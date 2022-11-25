package com.andygomez.apirest.main.controller;

import com.andygomez.apirest.main.model.Empleado;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerTestRestTemplateTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @Order(1)
    void testGuardarEmpleado(){
        Empleado empleado = Empleado.builder()
                .nombre("Andy")
                .apellido("Gomez")
                .email("a@a.a")
                .build();
        ResponseEntity<Empleado> respuesta = testRestTemplate.postForEntity("http://localhost:8080/api/empleados", empleado,Empleado.class);
        assertEquals(HttpStatus.CREATED,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());

        Empleado empleadoCreado = respuesta.getBody();
        assertNotNull(empleadoCreado);

        assertEquals(1L,empleadoCreado.getId());
        assertEquals("Andy",empleadoCreado.getNombre());
        assertEquals("Gomez",empleadoCreado.getApellido());
        assertEquals("a@a.a",empleadoCreado.getEmail());
    }

    @Test
    @Order(2)
    void testListarEmpleados(){
        ResponseEntity<Empleado[]> respuesta = testRestTemplate.getForEntity("http://localhost:8080/api/empleados", Empleado[].class);
        List<Empleado> empleados = Arrays.asList(respuesta.getBody());

        assertEquals(HttpStatus.OK,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());

        assertEquals(1,empleados.size());
        assertEquals(1L,empleados.get(0).getId());
        assertEquals("Andy", empleados.get(0).getNombre());
        assertEquals("Gomez",empleados.get(0).getApellido());
        assertEquals("a@a.a",empleados.get(0).getEmail());
    }

    @Test
    @Order(3)
    void testObtenerEmpleado(){
        ResponseEntity<Empleado> respuesta = testRestTemplate.getForEntity("http://localhost:8080/api/empleados", Empleado.class);
        Empleado empleado = respuesta.getBody();

        assertEquals(HttpStatus.OK,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders());
    }

    @Test
    @Order(4)
    void testEliminarEmpleado(){
        ResponseEntity<Empleado[]> respuesta = testRestTemplate.getForEntity("http://localhost:8080/api/empleados", Empleado[].class);
        List<Empleado> empleados = Arrays.asList(respuesta.getBody());
        assertEquals(1,empleados.size());

        Map<String, Long> pathVariables = new HashMap<>();
        pathVariables.put("id",1L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange("http://localhost:8080/api/empleados/{id}", HttpMethod.DELETE,null, Void.class,pathVariables);

        assertEquals(HttpStatus.OK,exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        respuesta = testRestTemplate.getForEntity("http://localhost:8080/api/empleados",Empleado[].class);
        empleados = Arrays.asList(respuesta.getBody());
        assertEquals(0,empleados.size());

        ResponseEntity<Empleado> respuestaDetalle = testRestTemplate.getForEntity("http://localhost:8080/api/empleados/2",Empleado.class);
        assertEquals(HttpStatus.NOT_FOUND,respuestaDetalle.getStatusCode());
        assertFalse(respuestaDetalle.hasBody());



    }

}
