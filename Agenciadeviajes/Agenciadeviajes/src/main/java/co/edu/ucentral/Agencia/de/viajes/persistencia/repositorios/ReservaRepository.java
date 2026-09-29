package co.edu.ucentral.Agencia.de.viajes.persistencia.repositorios;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

}