package co.edu.ucentral.Agencia.de.viajes.servicios;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Usuario;
import co.edu.ucentral.Agencia.de.viajes.persistencia.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método para registrar un nuevo usuario.
     *
     * @param correo    El correo electrónico del usuario.
     * @param usuario   El nombre de usuario.
     * @param nombre    El nombre completo del usuario.
     * @param telefono  El número de teléfono del usuario.
     * @param password  La contraseña del usuario.
     * @return El usuario registrado.
     */
    public Usuario registrarUsuario(String correo, String usuario, String nombre, String telefono, String password) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setCorreo(correo);
        nuevoUsuario.setUsuario(usuario);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setTelefono(telefono);
        nuevoUsuario.setPassword(password);
        nuevoUsuario.setEstado("ACTIVO");
        return usuarioRepository.save(nuevoUsuario);
    }

    /**
     * Método para validar duplicados en el registro (usuario y correo).
     *
     * @param usuario El nombre de usuario.
     * @param correo  El correo electrónico.
     * @return Un mapa indicando si el usuario o correo ya existen.
     */
    public Map<String, Boolean> validarDuplicado(String usuario, String correo) {
        Map<String, Boolean> resultado = new HashMap<>();
        boolean existeUsuario = usuarioRepository.findByUsuario(usuario).isPresent();
        boolean existeCorreo = usuarioRepository.findByCorreo(correo).isPresent();
        resultado.put("existeUsuario", existeUsuario);
        resultado.put("existeCorreo", existeCorreo);
        return resultado;
    }

    /**
     * Método para obtener un usuario por correo o nombre de usuario.
     *
     * @param emailOrUsername El correo o nombre de usuario.
     * @return El usuario encontrado, si existe.
     */
    public Optional<Usuario> obtenerUsuarioPorEmailOrUsername(String emailOrUsername) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(emailOrUsername);
        if (!usuario.isPresent()) {
            usuario = usuarioRepository.findByUsuario(emailOrUsername);
        }
        return usuario;
    }

    /**
     * Método para actualizar la contraseña de un usuario.
     *
     * @param emailOrUsername El correo o nombre de usuario.
     * @param newPassword     La nueva contraseña.
     * @return `true` si la actualización fue exitosa, `false` en caso contrario.
     */
    public boolean actualizarPassword(String emailOrUsername, String newPassword) {
        Optional<Usuario> usuarioOpt = obtenerUsuarioPorEmailOrUsername(emailOrUsername);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPassword(newPassword);
            usuarioRepository.save(usuario);
            return true;
        }
        return false;
    }

    /**
     * Método para validar el inicio de sesión.
     *
     * @param emailOrUsername El correo o nombre de usuario.
     * @param password        La contraseña ingresada.
     * @return El usuario si las credenciales son correctas, o `Optional.empty()` si no lo son.
     */
    public Optional<Usuario> validarLogin(String emailOrUsername, String password) {
        // Buscar el usuario por correo o nombre de usuario
        Optional<Usuario> usuarioOpt = obtenerUsuarioPorEmailOrUsername(emailOrUsername);

        // Verificar si la contraseña coincide
        if (usuarioOpt.isPresent() && usuarioOpt.get().getPassword().equals(password)) {
            return usuarioOpt;
        }
        return Optional.empty();
    }
}