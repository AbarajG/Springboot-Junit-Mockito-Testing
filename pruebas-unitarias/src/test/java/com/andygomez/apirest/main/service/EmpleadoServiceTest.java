package com.andygomez.apirest.main.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.andygomez.apirest.main.exception.ResourceNotFoundException;
import com.andygomez.apirest.main.model.Empleado;
import com.andygomez.apirest.main.repository.EmpleadoRepository;
import com.andygomez.apirest.main.service.Impl.EmpleadoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .id(1L)
                .nombre("Andy")
                .apellido("Gomez")
                .email("a@a.a")
                .build();
    }

    @DisplayName("Test para guardar empleado")
    @Test
    void testGuardarEmpleado(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado)).willReturn(empleado);

        //when
        Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);

        //then
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para guardar empleado con ThrowException")
    @Test
    void testGuardarEmpleadoConThrowException(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));

        //when
        assertThrows(ResourceNotFoundException.class,() -> {
            empleadoService.saveEmpleado(empleado);
        });

        //then
        verify(empleadoRepository,never()).save(any(Empleado.class));
    }

    @DisplayName("Test para listar los empleados")
    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .id(1L)
                .nombre("Olivia")
                .apellido("Perez")
                .email("a@a.a")
                .build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));

        //when
        List<Empleado> empleados = empleadoService.getAllEmpleado();

        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);

    }

    @DisplayName("Test para retornar una lista vacia")
    @Test
    void testListarcoleccionEmpleadosVacios(){
        //given
        Empleado empleado1 = Empleado.builder()
                .id(1L)
                .nombre("Olivia")
                .apellido("Perez")
                .email("a@a.a")
                .build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Empleado> listaEmpleados = empleadoService.getAllEmpleado();

        //then
        assertThat(listaEmpleados).isEmpty();
        assertThat(listaEmpleados.size()).isEqualTo(0);
    }

    @DisplayName("Test para obtener un empleado por ID")
    @Test
    void testObtenerIdEmpleado(){
        //given
        given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));

        //when
        Empleado empleadoGuardado = empleadoService.getEmpleadoById(empleado.getId()).get();

        //then
        assertThat(empleadoGuardado).isNotNull();
    }

    @DisplayName("Test para actualizar un empleado")
    @Test
    void testActualizarEmpleado(){
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("a@a.a");
        empleado.setNombre("Andy");

        //when
        Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);

        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("a@a.a");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Andy");
    }

    @DisplayName("Test para eliminar un empleado")
    @Test
    void testEliminarEmpleado(){
        //given
        long empleadoId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);

        //when
        empleadoService.deleteEmpleado(empleadoId);

        //then
        verify(empleadoRepository, times(1)).deleteById(empleadoId);
    }


}