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
    public ResponseEntity<?> validarFestivo(@RequestParam int año, @RequestParam int mes, @RequestParam int dia) {
        try {
            LocalDate fecha = LocalDate.of(año, mes, dia);
            boolean esFestivo = festivoServicio.esFestivo(fecha);
            return ResponseEntity.ok(esFestivo);
        } catch (DateTimeException e) {
            return ResponseEntity.badRequest().body("Fecha no válida");
        }
    }
}