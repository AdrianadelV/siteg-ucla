package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.compuesta.Jurado;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SJurado;
import servicio.SProfesor;
import servicio.STeg;
import configuracion.GeneradorBeans;
import controlador.CCalificarDefensa;
import controlador.CGeneral;

@Controller
public class CCatalogoCalificarDefensa extends CGeneral {

	@Wire
	private Listbox ltbCalificarDefensa;
	@Wire
	private Textbox txtEstudianteCalificarDefensa;
	@Wire
	private Textbox txtMostrarFechaCalificar;
	@Wire
	private Textbox txtMostrarTematicaCalificar;
	@Wire
	private Textbox txtMostrarAreaCalificar;
	@Wire
	private Textbox txtMostrarTituloCalificar;
	@Wire
	private Textbox txtMostrarNombreTutorCalificar;
	@Wire
	private Textbox txtMostrarApellidoTutorCalificar;
	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SJurado servicioj = GeneradorBeans.getServicioJurado();
	CCalificarDefensa ventanarecibida = new CCalificarDefensa();

	@Override
	public
	void inicializar(Component comp) {

		List<Teg> t = buscar();
		for (int i = 0; i < t.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(t.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			t.get(i).setEstatus(nombre+" "+apellido);
		}
		
		ltbCalificarDefensa.setModel(new ListModelList<Teg>(t));
		Selectors.wireComponents(comp, this, false);

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

	}
//busca los tegs de un profesor el cual el estatus sea defensa asignada
	public List<Teg> buscar() {
		List<Jurado> j = servicioj
				.buscarTegDeProfesor(ObtenerUsuarioProfesor());

		List<Teg> t = new ArrayList<Teg>();
		for (int i = 0; i < j.size(); i++) {
			Teg teg = j.get(i).getTeg();

			if (teg.getEstatus().equals("Defensa Asignada")) {

				t.add(teg);
			}
		}
		return t;
	}
//Permite hacer el filtrado en el catalogo
	@Listen("onChange = #txtMostrarFechaCalificar,#txtMostrarTematicaCalificar,#txtMostrarAreaCalificar,#txtMostrarTituloCalificar,#txtMostrarNombreTutorCalificar,# txtMostrarApellidoTutorCalificar")
	public void filtrarDatosCatalogo() {
		List<Teg> teg1 = buscar();
		for (int i = 0; i < teg1.size(); i++) {
			List<Estudiante> es = servicioEstudiante.buscarEstudiantePorTeg(teg1.get(i));
			String nombre = es.get(0).getNombre();
			String apellido = es.get(0).getApellido();
			teg1.get(i).setEstatus(nombre+" "+apellido);
		}
		List<Teg> teg2 = new ArrayList<Teg>();

		for (Teg teg : teg1) {
			if (servicioEstudiante.buscarEstudiantePorTeg(teg)
					.get(0)
					.getNombre()
					.toLowerCase()
					.contains(
							txtEstudianteCalificarDefensa.getValue()
									.toLowerCase())
					&&teg.getFecha()
					.toString()
					.toLowerCase()
					.contains(txtMostrarFechaCalificar.getValue().toLowerCase())

					&& teg.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarTematicaCalificar.getValue()
											.toLowerCase())

					&& teg.getTematica()
							.getareaInvestigacion()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarAreaCalificar.getValue()
											.toLowerCase())

					&& teg.getTitulo()
							.toLowerCase()
							.contains(
									txtMostrarTituloCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(
									txtMostrarNombreTutorCalificar.getValue()
											.toLowerCase())
					&& teg.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(
									txtMostrarApellidoTutorCalificar.getValue()
											.toLowerCase()))

			{
				teg2.add(teg);
			}
		}

		ltbCalificarDefensa.setModel(new ListModelList<Teg>(teg2));

	}
//permite mapear los datos a la vista calificar defensa
	@Listen("onDoubleClick = #ltbCalificarDefensa")
	public void mostrarDatosCatalogo() {
		if(ltbCalificarDefensa.getItemCount()!=0){
		Listitem listItem = ltbCalificarDefensa.getSelectedItem();
		if(listItem!=null){
		Teg tegDatosCatalogo = (Teg) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", tegDatosCatalogo.getId());
		String vista = "transacciones/VCalificarDefensa";
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("tegCatalogo", map);

		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VCalificarDefensa.zul", null, null);
		window.doModal();
		ventanarecibida.recibir("catalogos/VCatalogoCalificarDefensa");
		}
	}
	}
}