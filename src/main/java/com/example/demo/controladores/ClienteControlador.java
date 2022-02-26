package com.example.demo.controladores;

import com.example.demo.servicios.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteControlador {
    
    private final ClienteServicio clienteServicio;
    
    @Autowired
    public ClienteControlador(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }
    
    @GetMapping("/form")
    public String crearCliente(){
        
        
        return"/cliente/crearCliente.html";
    }
    
    
}
