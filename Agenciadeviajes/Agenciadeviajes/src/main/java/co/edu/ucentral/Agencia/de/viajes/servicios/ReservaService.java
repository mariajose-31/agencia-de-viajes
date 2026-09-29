package co.edu.ucentral.Agencia.de.viajes.servicios;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Reserva;
import co.edu.ucentral.Agencia.de.viajes.persistencia.repositorios.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Crea una nueva reserva y la guarda en la base de datos.
     *
     * @param origen      El lugar de origen del vuelo.
     * @param destino     El lugar de destino del vuelo.
     * @param fechaIda    La fecha de ida del vuelo.
     * @param fechaVuelta La fecha de vuelta del vuelo.
     * @param pasajeros   La cantidad de pasajeros y la clase seleccionada.
     * @return La reserva creada y guardada en la base de datos.
     */
    public Reserva crearReserva(String origen, String destino, LocalDate fechaIda, LocalDate fechaVuelta, String pasajeros) {
        // Validación básica (opcional, pero recomendada)
        if (origen == null || origen.isEmpty()) {
            throw new IllegalArgumentException("El origen no puede estar vacío.");
        }
        if (destino == null || destino.isEmpty()) {
            throw new IllegalArgumentException("El destino no puede estar vacío.");
        }
        if (fechaIda == null || fechaVuelta == null) {
            throw new IllegalArgumentException("Las fechas de ida y vuelta son obligatorias.");
        }
        if (pasajeros == null || pasajeros.isEmpty()) {
            throw new IllegalArgumentException("La información de los pasajeros es obligatoria.");
        }

        // Crear la entidad Reserva
        Reserva reserva = new Reserva();
        reserva.setOrigen(origen);
        reserva.setDestino(destino);
        reserva.setFechaIda(fechaIda);
        reserva.setFechaVuelta(fechaVuelta);
        reserva.setPasajeros(pasajeros);

        // Guardar la reserva en la base de datos
        return reservaRepository.save(reserva);
    }

    /**
     * Obtiene todas las reservas almacenadas en la base de datos.
     *
     * @return Una lista con todas las reservas existentes.
     */
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }

    /**
     * Elimina una reserva por su ID.
     *
     * @param id El ID de la reserva a eliminar.
     */
    public void eliminarReservaPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser nulo.");
        }
        reservaRepository.deleteById(id);
    }

    /**
     * Busca una reserva por su ID.
     *
     * @param id El ID de la reserva a buscar.
     * @return La reserva encontrada, o null si no existe.
     */
    public Reserva buscarReservaPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la reserva no puede ser nulo.");
        }
        return reservaRepository.findById(id).orElse(null);
    }
}