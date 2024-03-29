package servicio;

import interfazdao.IArchivoDAO;

import java.util.List;

import modelo.Archivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SArchivo")
public class SArchivo {

	@Autowired
	private IArchivoDAO interfaceArchivo;

	public void guardar(Archivo archivo) {
		interfaceArchivo.save(archivo);
	}

	public Archivo buscarArchivo(long id) {
		return interfaceArchivo.findOne(id);
	}
	
	public Archivo buscarNombreArchivo(String nombre) {
		Archivo archivo = interfaceArchivo.findByNombre(nombre); 
		return archivo;
	}

	public List<Archivo> buscarActivos(String h) {
		List<Archivo> archivo;
		archivo = interfaceArchivo.findByEstatusAndTipoArchivoOrderByProgramaAsc(true, h);
		return archivo;
	}

}
