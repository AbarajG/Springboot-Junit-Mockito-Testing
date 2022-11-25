package com.andygomez.apirest.main.service;

import com.andygomez.apirest.main.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    Empleado saveEmpleado(Empleado empleado);

    List<Empleado> getAllEmpleado();

    Optional<Empleado> getEmpleadoById(Long id);

    Empleado updateEmpleado(Empleado empleadoActualizado);

    void deleteEmpleado(Long id);

}
