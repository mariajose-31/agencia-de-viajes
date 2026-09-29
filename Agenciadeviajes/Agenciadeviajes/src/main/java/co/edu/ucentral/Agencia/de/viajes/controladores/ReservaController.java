package co.edu.ucentral.Agencia.de.viajes.controladores;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Reserva;
import co.edu.ucentral.Agencia.de.viajes.servicios.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/crear")
    public ResponseEntity<Reserva> crearReserva(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fechaIda,
            @RequestParam String fechaVuelta,
            @RequestParam String pasajeros) {
        Reserva reserva = reservaService.crearReserva(
                origen,
                destino,
                LocalDate.parse(fechaIda),
                LocalDate.parse(fechaVuelta),
                pasajeros
        );
        return ResponseEntity.ok(reserva);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Reserva>> listarReservas() {
        return ResponseEntity.ok(reservaService.obtenerTodasLasReservas());
    }
}