
package com.example.demo.repositorios;

import com.example.demo.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {
    
    @Query("select e from Editorial e where e.nombre LIKE :nombre")
    public Editorial buscarPorNombre(@Param("nombre") String nombre);
    
    /*
    @Query("SELECT l FROM Libro l WHERE l.titulo LIKE :titulo")
    public Libro buscarLibroPorNombre (@Param ("titulo") String titulo);
    */
}
