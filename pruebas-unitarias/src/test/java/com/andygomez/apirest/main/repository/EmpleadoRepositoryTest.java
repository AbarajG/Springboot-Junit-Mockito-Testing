package com.andygomez.apirest.main.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.andygomez.apirest.main.model.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EmpleadoRepositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){
        empleado = Empleado.builder()
                .nombre("Andy")
                .apellido("Apellido")
                .email("a@a.a")
                .build();
    }

    @DisplayName("Test guarda un empleado")
    @Test
    void testGuardarEmpleado(){
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Raul")
                .apellido("Perez")
                .email("p@p.p")
                .build();

        //when
        Empleado empleadoGuardado = empleadoRepository.save(empleado1);

        //then
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);
    }

    @DisplayName("Test para listar empleados")
    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado1 = Empleado.builder()
                .nombre("Patricio")
                .apellido("Robles")
                .email("p@r.p")
                .build();

        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        //when
        List<Empleado> listaEmpleados = empleadoRepository.findAll();

        //then
        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);

    }

    @DisplayName("Test para obtener empleado por ID")
    @Test
    void testObtenerIdEmpleado(){
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();

        //then
        assertThat(empleadoBD).isNotNull();
    }

    @DisplayName("Test para actualizar empleado")
    @Test
    void testActualizarEmpleado(){
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setNombre("Rodolfo");
        empleadoGuardado.setApellido("Epifanio");
        empleadoGuardado.setEmail("aaa@aaa.a");
        Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);

        //then
        assertThat(empleadoActualizado.getNombre()).isEqualTo("Rodolfo");
        assertThat(empleadoActualizado.getEmail()).isEqualTo("aaa@aaa.a");
    }

    @DisplayName("Test para eliminar un emplleado")
    @Test
    void testEliminarEmpleado(){
        empleadoRepository.save(empleado);

        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());

        //then
        assertThat(empleadoOptional).isEmpty();
    }

}