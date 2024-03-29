package servicio;

import interfazdao.ICondicionProgramaDAO;
import interfazdao.ILapsoDAO;

import java.util.List;

import modelo.Condicion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.CondicionPrograma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SCondicionPrograma")
public class SCondicionPrograma {

	@Autowired
	private ICondicionProgramaDAO interfaceCondicionPrograma;
	@Autowired
	private ILapsoDAO interfaceLapso;

	public void guardar(List<CondicionPrograma> condicionesProgramas) {
		interfaceCondicionPrograma.save(condicionesProgramas);
	}

	public List<CondicionPrograma> buscarCondicionesPrograma(Programa programa,
			Lapso lapso) {
		List<CondicionPrograma> condicionesProgramas;
		condicionesProgramas = interfaceCondicionPrograma
				.findByProgramaAndLapso(programa, lapso);
		return condicionesProgramas;
	}

	public List<CondicionPrograma> buscarUltimasCondiciones(Programa programa) {
		// TODO Auto-generated method stub
		List<CondicionPrograma> condiciones;
		condiciones = interfaceCondicionPrograma.buscarCondicionesActuales(
				programa, interfaceLapso.buscarLapsoVigente());
		return condiciones;
	}

	public CondicionPrograma buscarPorCondicionProgramaYLapso(
			Condicion condicion, Programa p, Lapso lapso) {
		// TODO Auto-generated method stub
		CondicionPrograma condicionPrograma;
		condicionPrograma = interfaceCondicionPrograma
				.findByCondicionAndProgramaAndLapso(condicion, p, lapso);
		return condicionPrograma;
	}

}
