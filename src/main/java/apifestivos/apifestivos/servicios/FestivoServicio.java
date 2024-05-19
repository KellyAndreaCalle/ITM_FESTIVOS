package apifestivos.apifestivos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import apifestivos.apifestivos.core.interfaces.repositorios.IFestivoRepositorio;
import apifestivos.apifestivos.core.interfaces.implementaciones.IFestivoServicio;
import apifestivos.apifestivos.core.dominio.Festivo;
import java.time.LocalDate;
import java.util.List;

@Service
public class FestivoServicio implements IFestivoServicio {

    @Autowired
    private IFestivoRepositorio festivoRepositorio;

    @Override
    public boolean esFestivo(LocalDate fecha) {
        List<Festivo> festivos = festivoRepositorio.findAll();
        for (Festivo festivo : festivos) {
            if (esFestivoEnFecha(festivo, fecha)) {
                return true;
            }
        }
        return false;
    }

    private boolean esFestivoEnFecha(Festivo festivo, LocalDate fecha) {
        int dia = festivo.getDia();
        int mes = festivo.getMes();
        int diasPascua = festivo.getDiasPascua();
        LocalDate fechaFestivo = calcularFechaFestivo(fecha.getYear(), mes, dia, diasPascua, festivo.getTipo().getId());
        
        
        return fechaFestivo.getYear() == fecha.getYear() &&
               fechaFestivo.getMonthValue() == fecha.getMonthValue() &&
               fechaFestivo.getDayOfMonth() == fecha.getDayOfMonth();
    }
    

    private LocalDate calcularFechaFestivo(int año, int mes, int dia, int diasPascua, int tipoFestivo) {
        if (tipoFestivo == 1) { // Fijo
            return LocalDate.of(año, mes, dia);
        } else if (tipoFestivo == 2) { // Ley Puente Festivo
            LocalDate fechaFestivo = LocalDate.of(año, mes, dia);
            if (fechaFestivo.getDayOfWeek().getValue() == 7) { // Si es domingo
                fechaFestivo = fechaFestivo.plusDays(1); // Trasladar al lunes
            }
            return fechaFestivo;
        } else { // Basado en Pascua
            // Implementar cálculo de fecha basado en Pascua aquí
            // De momento, retornamos la fecha de forma estática
            return LocalDate.of(2022, 1, 1);
        }
    }
}
