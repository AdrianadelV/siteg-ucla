package interfazdao;

import java.util.List;


import modelo.Teg;
import modelo.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ITegDAO extends JpaRepository <Teg, Long>  {
	
	@Query("select t from Teg t where t.estatus='SolicitandoRegistro'")
	public List<Teg> buscarSolicitudRegistroTeg();
    //Busca los tegs con estatus = ProyectoFactible
	@Query("select t from Teg t where t.estatus='ProyectoFactible'")
	public List<Teg> buscarProyectoFactible();

	//Evaluar Revisiones
	@Query("select t from Teg t where t.estatus='TEGRegistrado'")
	public List<Teg> buscarProyectoRegistrado();
	
	
	
}