package com.example.demo.entidades;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Prestamo {

    @Id
    @GeneratedValue(generator ="uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")
    public String id;
    public Date fechaPrestamo;
    public Date fechaDevolucion;
    public Boolean alta;
    @OneToOne
    public Libro libro;
    @OneToOne
    public Cliente cliente;
    
}
