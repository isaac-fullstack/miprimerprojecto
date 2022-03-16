
package com.example.demo.repositorios;

import com.example.demo.entidades.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {
 
@Query("SELECT c FROM Cliente c WHERE email LIKE :email")    
public Cliente findByEmail(@Param("email") String email);
    
}
