
package com.example.demo.repositorios;

import com.example.demo.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {
    
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id LIKE :id")
    public List<Prestamo> buscarPorCliente(@Param("id")String id);
    
    @Query("SELECT p FROM Prestamo p WHERE p.libro.id LIKE :id")
    public List<Prestamo> buscarPorLibro(@Param("id")String id);
    
    @Query("SELECT p FROM Prestamo p WHERE p.libro.id LIKE :libroId AND p.cliente.id LIKE :clienteId")
    public List<Prestamo> buscarPorLibroYCliente(@Param("libroId") String libroId, @Param("clienteId") String clienteId);
    
    @Query("SELECT p FROM Prestamo p ORDER BY p.fechaPrestamo Desc")
    public List<Prestamo> buscarTodosOrdenadosPorFecha();
    
    @Query("SELECT p FROM Prestamo p WHERE p.alta = TRUE ORDER BY p.fechaPrestamo Desc")
    public List<Prestamo> buscarTodosActivosOrdenadosPorFecha();
    
    //public List<Prestamo> findAllByOrderByfechaPrestamoDesc();
    
}
