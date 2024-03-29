package servicio.seguridad;

import interfazdao.seguridad.IGrupoDAO;

import java.util.List;

import modelo.seguridad.Grupo;
import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SGrupo")
public class SGrupo {

	@Autowired
	private IGrupoDAO interfazGrupo;

	public void guardarGrupo(Grupo grupo) {
		interfazGrupo.save(grupo);
	}

	public List<Grupo> buscarActivos() {
		List<Grupo> grupos;
		grupos = interfazGrupo.findByEstatusTrue();
		return grupos;
	}

	public Grupo buscarGrupo(long id) {
		return interfazGrupo.findOne(id);
	}

	public List<Grupo> buscarGruposDelUsuario(Usuario usuario) {
		List<Grupo> grupos;
		grupos = interfazGrupo.findByUsuarios(usuario);
		return grupos;
	}

	public List<Grupo> buscarGruposDisponibles(List<Long> ids) {
		List<Grupo> grupos;
		grupos = interfazGrupo.findByIdNotIn(ids);
		return grupos;
	}

	public Grupo BuscarPorNombre(String string) {
		// TODO Auto-generated method stub
		Grupo grupito;
		grupito = interfazGrupo.findByNombre(string);
		return grupito;
	}

}
