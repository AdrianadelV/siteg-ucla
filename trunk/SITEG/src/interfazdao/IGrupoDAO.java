package interfazdao;

import java.util.List;

import modelo.Grupo;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IGrupoDAO extends JpaRepository<Grupo, Long> {

	public List<Grupo> findByEstatusTrue();
	
	public List<Grupo> findByUsuarios(Usuario usuario);
	
	public List<Grupo> findByIdNotIn(List<Long> ids);
}
