package apifestivos.apifestivos.core.interfaces.implementaciones;

import java.time.LocalDate;

public interface IFestivoServicio {
    boolean esFestivo(LocalDate fecha);
}
