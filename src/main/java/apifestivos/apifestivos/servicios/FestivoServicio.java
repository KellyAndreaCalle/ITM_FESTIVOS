package apifestivos.apifestivos.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import apifestivos.apifestivos.core.interfaces.repositorios.IFestivoRepositorio;
import apifestivos.apifestivos.core.interfaces.implementaciones.IFestivoServicio;
import apifestivos.apifestivos.core.dominio.Festivo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
        LocalDate fechaFestivo = calcularFechaFestivo(fecha.getYear(), mes, dia, festivo.getTipo().getId());
        
        return fechaFestivo.getYear() == fecha.getYear() &&
               fechaFestivo.getMonthValue() == fecha.getMonthValue() &&
               fechaFestivo.getDayOfMonth() == fecha.getDayOfMonth();
    }
    

    private LocalDate calcularFechaFestivo(int año, int mes, int dia, int tipoFestivo) {
        if (tipoFestivo == 1) { // Fijo
            return LocalDate.of(año, mes, dia);
        } else if (tipoFestivo == 2) { // Ley Puente Festivo
            LocalDate fechaFestivo = LocalDate.of(año, mes, dia);
            if (fechaFestivo.getDayOfWeek() == DayOfWeek.SUNDAY) { // Si es domingo
                fechaFestivo = fechaFestivo.plusDays(1); // Trasladar al lunes
            }
            return fechaFestivo;
        } else if (tipoFestivo == 3 || tipoFestivo == 4) { // Basado en Pascua
            int a = año % 19;
            int b = año % 4;
            int c = año % 7;
            int d = (19 * a + 24) % 30;
            int e = (2 * b + 4 * c + 6 * d + 5) % 7;
            int diaPascua = 22 + d + e;

            // Manejo de casos especiales
            if ((d == 29 && e == 6) || (d == 28 && e == 6 && (11 * (a + 1)) % 30 < 19)) {
                diaPascua -= 7;
            }

            LocalDate fechaPascua = LocalDate.of(año, 3, 1).plusDays(diaPascua);

            // Si la fecha de Pascua cae en marzo, se ajusta al domingo siguiente (Pascua se celebra en abril)
            if (fechaPascua.getMonthValue() == 3) {
                fechaPascua = fechaPascua.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
            }

            if (tipoFestivo == 4) { // Si es Basado en Pascua y Ley de "Puente festivo"
                if (fechaPascua.getDayOfWeek() == DayOfWeek.SUNDAY) { // Si la fecha de Pascua es domingo
                    fechaPascua = fechaPascua.plusDays(1); // Trasladar al lunes
                }
            }

            return fechaPascua.plusDays(dia);
        }

        return null; // Tipo de festivo no reconocido
    }
}
