package com.andygomez.apirest.main.service.Impl;

import com.andygomez.apirest.main.exception.ResourceNotFoundException;
import com.andygomez.apirest.main.model.Empleado;
import com.andygomez.apirest.main.repository.EmpleadoRepository;
import com.andygomez.apirest.main.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Override
    public Empleado saveEmpleado(Empleado empleado) {
        Optional<Empleado> empleadoGuardado = empleadoRepository.findByEmail(empleado.getEmail());
        if(empleadoGuardado.isPresent()){
            throw new ResourceNotFoundException("El empleado con ese email ya existe : " + empleado.getEmail());
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    public List<Empleado> getAllEmpleado() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> getEmpleadoById(Long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado updateEmpleado(Empleado empleadoActualizado) {
        return empleadoRepository.save(empleadoActualizado);
    }

    @Override
    public void deleteEmpleado(Long id) {
        empleadoRepository.deleteById(id);
    }
}
