package com.andygomez.apirest.main.controller;

import com.andygomez.apirest.main.model.Empleado;
import com.andygomez.apirest.main.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Empleado guardarEmpleado(@RequestBody Empleado empleado){
        return empleadoService.saveEmpleado(empleado);
    }

    @GetMapping
    public List<Empleado> listarEmpleados(){
        return empleadoService.getAllEmpleado();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoId(@PathVariable("id")long empladoId){
        return empleadoService.getEmpleadoById(empladoId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable("id")long empleadoId, @RequestBody Empleado empleado){
        return empleadoService.getEmpleadoById(empleadoId)
                .map(empleadoGuardado -> {
                    empleadoGuardado.setNombre(empleado.getNombre());
                    empleadoGuardado.setApellido(empleado.getApellido());
                    empleadoGuardado.setEmail(empleado.getEmail());

                    Empleado empleadoActualizado = empleadoService.updateEmpleado(empleadoGuardado);
                    return new ResponseEntity<>(empleadoActualizado,HttpStatus.OK);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarEmpleado(@PathVariable("id")long empleadoId){
        empleadoService.deleteEmpleado(empleadoId);
        return new ResponseEntity<String>("Empleado eliminado con exito",HttpStatus.OK);
    }

}
