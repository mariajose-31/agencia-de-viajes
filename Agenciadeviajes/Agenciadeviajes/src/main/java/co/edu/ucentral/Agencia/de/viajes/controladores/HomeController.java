package co.edu.ucentral.Agencia.de.viajes.controladores;

import co.edu.ucentral.Agencia.de.viajes.persistencia.entidades.Reserva;
import co.edu.ucentral.Agencia.de.viajes.servicios.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private ReservaService reservaService;

    @GetMapping("/")
    public String mostrarIndex() {
        return "index";
    }
    // Página de inicio de sesión
    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // Redirige a la página login.html
    }
    // Página de registro
    @GetMapping("/register")
    public String mostrarRegistro() {
        return "register"; // Redirige a la página register.html
    }
    // Página principal (después de iniciar sesión)
    @GetMapping("/principal")
    public String mostrarPrincipal() {
        return "principal"; // Redirige a la página principal.html
    }

    @GetMapping("/compra")
    public ModelAndView mostrarCompra(@RequestParam(required = false) String reservaExitosa) {
        ModelAndView modelAndView = new ModelAndView("compra");
        if ("true".equals(reservaExitosa)) {
            modelAndView.addObject("mensajeReserva", "¡Vuelo reservado exitosamente!");
        }
        return modelAndView;
    }

    @PostMapping("/reservar-vuelo")
    public String reservarVuelo(
            @RequestParam String origen,
            @RequestParam String destino,
            @RequestParam String fechaIda,
            @RequestParam String fechaVuelta,
            @RequestParam String pasajeros) {
        // Crear la reserva y guardarla en la base de datos
        reservaService.crearReserva(origen, destino, LocalDate.parse(fechaIda), LocalDate.parse(fechaVuelta), pasajeros);
        return "redirect:/compra?reservaExitosa=true"; // Redirige a la página de compra con un parámetro de éxito
    }

    @GetMapping("/ver-reservas")
    public ModelAndView verReservas() {
        ModelAndView modelAndView = new ModelAndView("ver-reservas");
        List<Reserva> reservas = reservaService.obtenerTodasLasReservas();
        modelAndView.addObject("reservas", reservas);
        return modelAndView;
    }
}