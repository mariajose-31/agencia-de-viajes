package co.edu.ucentral.Agencia.de.viajes.controladores;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Usuario;
import co.edu.ucentral.Agencia.de.viajes.servicios.UsuarioService;
import co.edu.ucentral.Agencia.de.viajes.servicios.UsuarioStatusService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioStatusService usuarioStatusService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService, UsuarioStatusService usuarioStatusService) {
        this.usuarioService = usuarioService;
        this.usuarioStatusService = usuarioStatusService;
    }

    // Método para verificar token hCaptcha
    private boolean verificarTokenHCaptcha(String token) {
        String secretKey = "ES_73d5d915613744438d51a2b9829762f5";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", secretKey);
        requestMap.add("response", token);
        String verifyUrl = "https://hcaptcha.com/siteverify";
        Map<?, ?> response = restTemplate.postForObject(verifyUrl, requestMap, Map.class);
        return response != null && Boolean.TRUE.equals(response.get("success"));
    }

    // Resetear contraseña
    @PutMapping("/passwordreset")
    public ResponseEntity<Map<String, String>> actualizarPassword(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        String emailOrUsername = request.get("emailOrUsername");
        String newPassword = request.get("newPassword");
        String captchaToken = request.get("h-captcha-response");

        if (!verificarTokenHCaptcha(captchaToken)) {
            response.put("error", "No completaste el hCaptcha correctamente.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        if (emailOrUsername == null || newPassword == null) {
            response.put("error", "Faltan datos requeridos. ❌");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        boolean actualizado = usuarioService.actualizarPassword(emailOrUsername, newPassword);
        if (actualizado) {
            Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorEmailOrUsername(emailOrUsername);
            if (!usuarioOpt.isPresent()) {
                response.put("error", "No se encontró el usuario.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            Usuario usuario = usuarioOpt.get();
            usuarioStatusService.resetFailedAttempts(usuario);
            response.put("success", "La contraseña se actualizó correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "No se pudo actualizar la contraseña.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Map<String, String> request,
            HttpSession session) {
        Map<String, String> response = new HashMap<>();
        String emailOrUsername = request.get("emailOrUsername");
        String password = request.get("password");

        // Buscar usuario por correo o nombre de usuario
        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorEmailOrUsername(emailOrUsername);

        // Verificar si el usuario existe
        if (!usuarioExistente.isPresent()) {
            response.put("error", "Credenciales incorrectas.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Verificar intentos fallidos (opcional)
        int failedAttempts = usuarioStatusService.getFailedAttempts(usuarioExistente.get());
        if (failedAttempts >= 2) {
            response.put("error", "Demasiados intentos fallidos. Bloqueado temporalmente.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Validar credenciales
        Optional<Usuario> usuario = usuarioService.validarLogin(emailOrUsername, password);
        if (usuario.isPresent()) {
            Usuario usuarioLogueado = usuario.get();
            session.setAttribute("usuario", usuarioLogueado.getUsuario());
            usuarioStatusService.resetFailedAttempts(usuarioLogueado);
            response.put("success", "Credenciales correctas.");
            return ResponseEntity.ok(response);
        } else {
            usuarioStatusService.incrementFailedAttempts(usuarioExistente.get());
            response.put("error", "Credenciales incorrectas.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    // Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        Usuario usuarioRegistrado = usuarioService.registrarUsuario(
                usuario.getCorreo(),
                usuario.getUsuario(),
                usuario.getNombre(),
                usuario.getTelefono(),
                usuario.getPassword()
        );
        return new ResponseEntity<>(usuarioRegistrado, HttpStatus.CREATED);
    }

    // Verificar duplicados
    @GetMapping("/usuario/existe")
    public ResponseEntity<Map<String, Boolean>> existeUsuario(
            @RequestParam("usuario") String usuario,
            @RequestParam("correo") String correo) {
        Map<String, Boolean> resultado = usuarioService.validarDuplicado(usuario, correo);
        return ResponseEntity.ok(resultado);
    }
}