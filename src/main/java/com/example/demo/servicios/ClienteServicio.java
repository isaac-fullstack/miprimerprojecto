package com.example.demo.servicios;

import com.example.demo.entidades.Cliente;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.repositorios.ClienteRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicio {

    private final ClienteRepositorio clienteRepositorio;

    @Autowired
    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Transactional(rollbackOn = Exception.class)
    public void guardar(
            String id, String nombre, String apellido,
            String telefono) throws ErrorServicio {

        validar(nombre, apellido, telefono);
        
        Cliente cliente = new Cliente();
        
        if (id != null && !id.isEmpty() && !id.equals(" ")) {
            Optional<Cliente> option = clienteRepositorio.findById(id);
            if (option.isPresent()) {
                cliente = option.get();
            }else{
                throw new ErrorServicio("Cliente no encontrado");
            }
        }
        
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setAlta(true);
        
        clienteRepositorio.save(cliente);

    }

    @Transactional(rollbackOn = Exception.class)
    private void validar(String nombre, String apellido,
            String telefono) throws ErrorServicio {

        if (nombre.isEmpty() || nombre.equals("") || nombre.equals(" ")) {
            throw new ErrorServicio("El nombre no puede estar vacio");
        }
        if (apellido.isEmpty() || apellido.equals("") || apellido.equals(" ")) {
            throw new ErrorServicio("El apellido no puede estar vacio");
        }
        if (telefono.isEmpty() || telefono.equals("") || telefono.equals(" ")) {
            throw new ErrorServicio("El telefono no puede estar vacio");
        }

    }

}
