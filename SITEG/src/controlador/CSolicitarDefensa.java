package controlador;

import java.util.List;

import modelo.Estudiante;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.STeg;

@Controller
public class CSolicitarDefensa extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();

	@Wire
	private Datebox dtbFechaSolicitarDefensa;
	@Wire
	private Textbox txtProgramaSolicitarDefensa;
	@Wire
	private Textbox txtAreaSolicitarDefensa;
	@Wire
	private Textbox txtTematicaSolicitarDefensa;
	@Wire
	private Textbox txtTituloSolicitarDefensa;
	@Wire
	private Listbox ltbEstudiantesSolicitarDefensa;
	@Wire
	private Window wdwSolicitarDefensa;
	private Teg teg;
	private String[] estatus = { "Revisiones Finalizadas", "Solicitando Defensa" };

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		// Acomodar
		teg = servicioTeg.buscarTegPorEstudiante(ObtenerUsuarioEstudiante())
				.get(0);
		List<Estudiante> estudiantes = servicioEstudiante
				.buscarEstudiantePorTeg(teg);
		
		if (teg.getEstatus().equals(estatus[0])) {
			//se guia es por el programa del estudiante
			txtProgramaSolicitarDefensa.setValue(estudiantes.get(0).getPrograma().getNombre());
			txtAreaSolicitarDefensa.setValue(teg.getTematica()
					.getareaInvestigacion().getNombre());
			txtTematicaSolicitarDefensa.setValue(teg.getTematica().getNombre());
			txtTituloSolicitarDefensa.setValue(teg.getTitulo());
			ltbEstudiantesSolicitarDefensa
					.setModel(new ListModelList<Estudiante>(estudiantes));
		} else {
			if (teg.getEstatus().equals(estatus[1])) {
				Messagebox.show(
						"Ya ud Posee una solicitud de Defensa",
						"Advertencia", Messagebox.OK,
						Messagebox.EXCLAMATION);
				cancelar();
			} else {
				Messagebox.show(
						"No puede solicitar defensa",
						"Advertencia", Messagebox.OK,
						Messagebox.EXCLAMATION);
				cancelar();
			}
		}
	}

	@Listen("onClick = #btnCancelarSolicitarDefensa")
	public void cancelar() {
		wdwSolicitarDefensa.onClose();
	}

	@Listen("onClick = #btnSolicitarDefensa")
	public void solicitarDefensa() {
		teg.setEstatus(estatus[1]);
		servicioTeg.guardar(teg);
		Messagebox.show(
				"Su Solicitud de Defensa fue enviada con exito",
				"Informaci�n", Messagebox.OK,
				Messagebox.INFORMATION);
		cancelar();
	}
}