package co.edu.ucentral.Agencia.de.viajes.persistencia.repositorios;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Usuario;
import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.UsuarioStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioStatusRepository extends JpaRepository<UsuarioStatus, Long> {
    Optional<UsuarioStatus> findByUsuario(Usuario usuario);
}