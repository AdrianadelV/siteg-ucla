package servicio;

import java.util.List;

import interfazdao.IMencionDAO;

import modelo.Mencion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SMencion {

	@Autowired
	private IMencionDAO interfaceMencion;
	private List<Mencion> menciones;
	private Mencion mencion;
	
	public List<Mencion> buscarActivos(){
		menciones = interfaceMencion.findByEstatusTrue();
		return menciones;
	}

	public Mencion buscar(long id) {
		// TODO Auto-generated method stub
		mencion=interfaceMencion.findOne(id);
		return mencion;
	}
}
