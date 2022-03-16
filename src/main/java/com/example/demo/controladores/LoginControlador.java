
package com.example.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginControlador {
    
    @GetMapping("/login")
    public String loginForm(@RequestParam(required = false)String error, ModelMap model
    ){
        if (error != null) {
            
            model.put("error", "Usuario o clave incorrectos.");
        }
        return "login.html";
    }
    
  
}
