package com.example.demo.servicios;

import com.example.demo.entidades.Cliente;
import com.example.demo.enumeraciones.Rol;
import com.example.demo.errores.ErrorServicio;
import com.example.demo.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ClienteServicio implements UserDetailsService {

    private final ClienteRepositorio clienteRepositorio;

    @Autowired
    public ClienteServicio(ClienteRepositorio clienteRepositorio) {
        this.clienteRepositorio = clienteRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Cliente cliente = clienteRepositorio.findByEmail(email);

        if (cliente == null) {
            throw new UsernameNotFoundException("Cliente no encontrado");
        }

        List<GrantedAuthority> permisos = new ArrayList();
        GrantedAuthority permisosDeRol = new SimpleGrantedAuthority("ROLE_" + cliente.getRol().toString());
        permisos.add(permisosDeRol);

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession sesion = attr.getRequest().getSession(true);
        sesion.setAttribute("clienteSesion", cliente);

        return new User(cliente.getEmail(), cliente.getClave(), permisos);

    }

    @Transactional(rollbackOn = Exception.class)
    public void guardar(
            String id, String nombre, String apellido,
            String telefono, String email, String clave) throws ErrorServicio {

        validar(nombre, apellido, telefono, email, clave);

        Cliente cliente = new Cliente();

        if (id != null && !id.isEmpty() && !id.equals(" ")) {
            Optional<Cliente> option = clienteRepositorio.findById(id);
            if (option.isPresent()) {
                cliente = option.get();
            } else {
                throw new ErrorServicio("Cliente no encontrado");
            }
        }

        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setTelefono(telefono);
        cliente.setAlta(true);
        cliente.setEmail(email);
        cliente.setClave(clave);
        cliente.setRol(Rol.USER);

        String claveEncriptada = new BCryptPasswordEncoder().encode(cliente.getClave());
        cliente.setClave(claveEncriptada);

        clienteRepositorio.save(cliente);

    }

    @Transactional(rollbackOn = Exception.class)
    public void validar(String nombre, String apellido, String telefono, String email, String clave) throws ErrorServicio {

        if (nombre == null || nombre.equals("") || nombre.equals(" ") || nombre.length() < 3) {
            throw new ErrorServicio("Nombre no valido");
        }
        if (apellido == null || apellido.equals("") || apellido.equals(" ") || apellido.length() < 3) {
            throw new ErrorServicio("Apellido no valido");
        }
        if (telefono == null || telefono.equals("") || telefono.equals(" ") || telefono.length() < 7) {
            throw new ErrorServicio("Telefono no valido");
        }
        if (email == null || email.equals("") || email.equals(" ")) {
            throw new ErrorServicio("email no valido");
        }
        if (clave == null || clave.equals("") || clave.equals(" ") || clave.length() < 3) {
            throw new ErrorServicio("Clave no valida");
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public List<Cliente> listarTodos() {
        return clienteRepositorio.findAll();
    }
}
