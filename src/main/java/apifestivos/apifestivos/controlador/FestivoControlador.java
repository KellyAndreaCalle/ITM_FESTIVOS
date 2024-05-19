package apifestivos.apifestivos.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import apifestivos.apifestivos.core.interfaces.implementaciones.IFestivoServicio;
import java.time.DateTimeException;
import java.time.LocalDate;

@RestController
public class FestivoControlador {

    @Autowired
    private IFestivoServicio festivoServicio;

    @GetMapping("api/festivos")
    public ResponseEntity<?> validarFestivo(@RequestParam("fecha") String fechaString) {
        try {
            String[] partesFecha = fechaString.split("/");
            int dia = Integer.parseInt(partesFecha[0]);
            int mes = Integer.parseInt(partesFecha[1]);
            int año = Integer.parseInt(partesFecha[2]);
    
            LocalDate fecha = LocalDate.of(año, mes, dia);
            boolean esFestivo = festivoServicio.esFestivo(fecha);
            String mensaje = esFestivo ? "True" : "False";
            return ResponseEntity.ok().body(mensaje);
        } catch (DateTimeException | ArrayIndexOutOfBoundsException | NumberFormatException e) {
            return ResponseEntity.badRequest().body("Fecha no válida");
        }
    }
}

    