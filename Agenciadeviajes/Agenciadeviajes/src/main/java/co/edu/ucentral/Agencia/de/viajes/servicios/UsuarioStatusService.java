package co.edu.ucentral.Agencia.de.viajes.servicios;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Usuario;
import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.UsuarioStatus;
import co.edu.ucentral.Agencia.de.viajes.persistencia.repositorios.UsuarioStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioStatusService {

    @Autowired
    private UsuarioStatusRepository usuarioStatusRepository;

    public UsuarioStatus getOrCreateStatus(Usuario usuario) {
        Optional<UsuarioStatus> optionalStatus = usuarioStatusRepository.findByUsuario(usuario);
        if (optionalStatus.isPresent()) {
            return optionalStatus.get();
        } else {
            UsuarioStatus nuevo = new UsuarioStatus();
            nuevo.setUsuario(usuario);
            nuevo.setFailedAttempts(0);
            nuevo.setStatus("DESBLOQUEADO");
            nuevo.setAttemptTime(LocalDateTime.now());
            return usuarioStatusRepository.save(nuevo);
        }
    }

    public UsuarioStatus incrementFailedAttempts(Usuario usuario) {
        UsuarioStatus status = getOrCreateStatus(usuario);
        status.setFailedAttempts(status.getFailedAttempts() + 1);
        status.setAttemptTime(LocalDateTime.now());
        if (status.getFailedAttempts() >= 3) {
            status.setStatus("BLOQUEADO");
        }
        return usuarioStatusRepository.save(status);
    }

    public UsuarioStatus resetFailedAttempts(Usuario usuario) {
        UsuarioStatus status = getOrCreateStatus(usuario);
        status.setFailedAttempts(0);
        status.setStatus("DESBLOQUEADO");
        status.setAttemptTime(LocalDateTime.now());
        return usuarioStatusRepository.save(status);
    }

    public int getFailedAttempts(Usuario usuario) {
        UsuarioStatus status = getOrCreateStatus(usuario);
        return status.getFailedAttempts();
    }
}