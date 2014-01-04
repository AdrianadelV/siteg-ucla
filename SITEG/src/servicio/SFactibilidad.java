package servicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import modelo.Estudiante;
import modelo.Factibilidad;
import modelo.Teg;
import interfazdao.IFactibilidadDAO;

@Service
public class SFactibilidad {
	@Autowired
	private IFactibilidadDAO interfacefactibilidad;
	//Metodo para guardar una factibilidad
		public void guardar(Factibilidad factibilidad){
			interfacefactibilidad.save(factibilidad);
		}
	//Metdo para buscar el id de un teg, dado el id de la factibilidad
	public Factibilidad buscarIdTeg(long id){
		return interfacefactibilidad.findOne(id);
	}
  //Metodo para buscar la factibilidad con estatus = FactibilidadEvaluada
	public List<Factibilidad> buscarActivos(){
		List<Factibilidad> factibilidad;
		factibilidad= interfacefactibilidad.buscarFactibilidadActivos();
		return factibilidad;
	}
	//Metodo encargado de buscar el profesor asociado a una factibilidad
	public List<Factibilidad> buscarProfesores(Long id){
		List<Factibilidad> factibilidad1;
		factibilidad1= interfacefactibilidad.BuscarProfesor(id);
		return factibilidad1;
	}
}