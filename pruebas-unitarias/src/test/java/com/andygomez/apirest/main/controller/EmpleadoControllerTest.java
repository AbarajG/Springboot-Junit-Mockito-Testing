package com.andygomez.apirest.main.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.andygomez.apirest.main.model.Empleado;
import com.andygomez.apirest.main.service.EmpleadoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
class EmpleadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGuardarEmpleado() throws Exception {
        //given
        Empleado empleado = Empleado.builder()
                .id(1L)
                .nombre("Christian")
                .apellido("Ramirez")
                .email("c1@gmail.com")
                .build();
        given(empleadoService.saveEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }

    @Test
    void testListarEmpleados() throws Exception{
        //given
        List<Empleado> listaEmpleados = new ArrayList<>();
        listaEmpleados.add(Empleado.builder().nombre("Paco").apellido("Cantu").email("p@p.p").build());
        listaEmpleados.add(Empleado.builder().nombre("Raul").apellido("Lopez").email("r@r.r").build());
        listaEmpleados.add(Empleado.builder().nombre("Pedro").apellido("Roblez").email("p@r.p").build());
        listaEmpleados.add(Empleado.builder().nombre("Uriel").apellido("Perez").email("u@p.p").build());
        listaEmpleados.add(Empleado.builder().nombre("Rodolfo").apellido("Epifanio").email("r@e.p").build());
        given(empleadoService.getAllEmpleado()).willReturn(listaEmpleados);

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados"));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listaEmpleados.size())));

    }

    @Test
    void testObtenerIdEmpleado() throws Exception {
        //given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Andy")
                .apellido("Gomez")
                .email("a@a.a")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }

    @Test
    void testObtenerIdEmpleadoNoEncontrado() throws Exception {
        //given
        long empleadoId = 1L;
        Empleado empleado = Empleado.builder()
                .nombre("Andy")
                .apellido("Gomez")
                .email("a@a.a")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/api/empleados/{id}", empleadoId));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testActualizarEmpleado() throws Exception {
        //given
        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Andy")
                .apellido("Gomez")
                .email("a@a.a")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Andy actualizado")
                .apellido("Gomez actualizado")
                .email("aa@a.a")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));
    }

    @Test
    void testActualizarEmpleadoNoEncontrado() throws Exception {
        //given
        long empleadoId = 1L;
        Empleado empleadoGuardado = Empleado.builder()
                .nombre("Andy")
                .apellido("Gomez")
                .email("a@a.a")
                .build();

        Empleado empleadoActualizado = Empleado.builder()
                .nombre("Andy actualizado")
                .apellido("Gomez actualizado")
                .email("aa@a.a")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/api/empleados/{id}", empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));

        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testEliminarEmpleado() throws Exception {
        //given
        long empleadoId = 1L;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);

        //when
        ResultActions response = mockMvc.perform(delete("/api/empleados/{id}",empleadoId));

        //then
        response.andExpect(status().isOk())
                .andDo(print());
    }

}