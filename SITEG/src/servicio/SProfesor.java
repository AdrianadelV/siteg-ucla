package servicio;

import interfazdao.IProfesorDAO;

import java.util.List;

import modelo.Profesor;
import modelo.Teg;
import modelo.Tematica;
import modelo.seguridad.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SProfesor")
public class SProfesor {

	@Autowired
	private IProfesorDAO interfazProfesor;

	public void guardarProfesor(Profesor profesor) {
		// TODO Auto-generated method stub
		interfazProfesor.save(profesor);
	}

	public Profesor buscarProfesorPorCedula(String cedula) {
		// TODO Auto-generated method stub
		Profesor profesor;
		profesor = interfazProfesor.findByCedula(cedula);
		return profesor;
	}

	public List<Profesor> buscarActivos() {
		// TODO Auto-generated method stub
		List<Profesor> profesores;
		profesores = interfazProfesor.findByEstatusTrueOrderByCategoriaAsc();
		return profesores;
	}

	public List<Profesor> buscarTodos() {
		// TODO Auto-generated method stub
		List<Profesor> profesores;
		profesores = interfazProfesor.findAll();
		return profesores;
	}

	public Profesor buscarProfesorLoggeado(Usuario u) {
		// TODO Auto-generated method stub
		Profesor profesor;
		profesor = interfazProfesor.findByUsuario(u);
		return profesor;
	}

	public List<Profesor> buscarProfesorSinUsuario() {
		// TODO Auto-generated method stub
		List<Profesor> profesores;
		profesores = interfazProfesor.buscarSinUsuario();
		return profesores;
	}

	public List<Profesor> buscarProfesorJuradoDadoTeg(Teg teg) {
		// TODO Auto-generated method stub
		List<Profesor> profesores;
		profesores = interfazProfesor.buscarProfesorEnJurado(teg);
		return profesores;
	}

	public List<Profesor> buscarComisionDelTeg(Teg teg) {
		List<Profesor> profesores;
		profesores = interfazProfesor.findByTegs(teg);
		return profesores;
	}

	public List<Profesor> buscarProfesoresSinComision(List<String> cedulas) {
		List<Profesor> profesores;
		profesores = interfazProfesor.findByCedulaNotIn(cedulas);
		return profesores;
	}

	public List<Profesor> buscarProfesoresPorTematica(Tematica tematica) {
		List<Profesor> profesores;
		profesores = interfazProfesor.findByTematicas(tematica);
		return profesores;
	}

	public List<Profesor> buscarProfesoresSinPrograma() {
		List<Profesor> profesores;
		profesores = interfazProfesor.profesorSinPrograma();
		return profesores;
	}

}
