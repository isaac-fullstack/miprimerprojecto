
package com.example.demo.repositorios;

import com.example.demo.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String>
{
    
    @Query("SELECT l FROM Libro l WHERE l.autor.id LIKE :autorId")
    public List<Libro> buscarPorAutorId(@Param("autorId")String autorId);
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.id LIKE :editorialId")
    public List<Libro> buscarPorEditorialId(@Param("editorialId")String editorialId);
    
    
    
}
