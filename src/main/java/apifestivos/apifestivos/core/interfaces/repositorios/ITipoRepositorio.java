package apifestivos.apifestivos.core.interfaces.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import apifestivos.apifestivos.core.dominio.Tipo;

@Repository
public interface ITipoRepositorio extends JpaRepository<Tipo, Integer> {
}
