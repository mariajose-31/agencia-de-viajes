package co.edu.ucentral.Agencia.de.viajes.persistencia.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDate fechaIda;

    @Column(nullable = false)
    private LocalDate fechaVuelta;

    @Column(nullable = false)
    private String pasajeros;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public LocalDate getFechaIda() { return fechaIda; }
    public void setFechaIda(LocalDate fechaIda) { this.fechaIda = fechaIda; }

    public LocalDate getFechaVuelta() { return fechaVuelta; }
    public void setFechaVuelta(LocalDate fechaVuelta) { this.fechaVuelta = fechaVuelta; }

    public String getPasajeros() { return pasajeros; }
    public void setPasajeros(String pasajeros) { this.pasajeros = pasajeros; }
}