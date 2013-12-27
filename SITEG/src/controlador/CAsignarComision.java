package controlador;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import modelo.Actividad;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Requisito;
import modelo.Teg;
import modelo.Usuario;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SActividad;
import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SProgramaRequisito;
import servicio.SRequisito;
import servicio.STeg;
import servicio.SUsuario;
import configuracion.GeneradorBeans;

public class CAsignarComision extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();

	@Wire
	private Textbox txtAreaComision;
	@Wire
	private Textbox txtTematicaComision;
	@Wire
	private Textbox txtTituloComision;
	@Wire
	private Textbox txtNombreTutorComision;
	@Wire
	private Textbox txtCedulaTutorComision;
	@Wire
	private Textbox txtApellidoTutorComision;
	@Wire
	private Textbox txtProgramaComision;
	@Wire
	private Textbox txtCorreoTutorComision;
	@Wire
	private Listbox lsbProfesoresDisponibles;
	@Wire
	private Listbox lsbProfesoresSeleccionados;
	@Wire
	private Listbox lsbEstudiantesTeg;
	@Wire
	private Window wdwAsignarComision;
	@Wire
	private Window wdwCatalogoAsignarComision;
	private static String vistaRecibida;

	private List<Profesor> profesores;

	private long id = 0;
	private static long auxiliarId = 0;

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub

		Profesor profesor = ObtenerUsuarioProfesor();
		Programa programa = new Programa();
		programa = profesor.getPrograma();

		List<Profesor> profesores = servicioProfesor
				.buscarProfesorDelPrograma(programa);
		lsbProfesoresDisponibles.setModel(new ListModelList<Profesor>(
				profesores));

		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("tegCatalogo");

		if (map != null) {
			if (map.get("id") != null) {
				long codigo = (Long) map.get("id");
				auxiliarId = codigo;
				Teg teg2 = servicioTeg.buscarTeg(auxiliarId);
				txtProgramaComision.setValue(programa.getNombre());
				txtNombreTutorComision.setValue(teg2.getTutor().getNombre());
				txtApellidoTutorComision
						.setValue(teg2.getTutor().getApellido());
				txtCorreoTutorComision.setValue(teg2.getTutor()
						.getCorreoElectronico());
				txtTituloComision.setValue(teg2.getTitulo());
				txtCedulaTutorComision.setValue(teg2.getTutor().getCedula());
				txtAreaComision.setValue(teg2.getTematica()
						.getareaInvestigacion().getNombre());
				txtTematicaComision.setValue(teg2.getTematica().getNombre());
				Teg tegPorCodigo = servicioTeg.buscarTeg(auxiliarId);
				List<Estudiante> estudiantes = servicioEstudiante
						.buscarEstudiantesDelTeg(tegPorCodigo);
				lsbEstudiantesTeg.setModel(new ListModelList<Estudiante>(
						estudiantes));
				id = teg2.getId();

				map.clear();
				map = null;
			}
		}

	}

	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	private void salir() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwAsignarComision.onClose();
	}

	// Metodo que permite agregar los profesores a la lista de integrantes de la
	// comision evaluadora
	@Listen("onClick = #btnAgregarProfesoresComision")
	public void agregarProfesor() {

		Listitem list1 = lsbProfesoresDisponibles.getSelectedItem();
		if (list1 == null)
			Messagebox.show("Seleccione un profesor", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		else
			list1.setParent(lsbProfesoresSeleccionados);
	}

	// Metodo que permite quitar los profesores de la lista de integrantes de la
	// comision evaluadora
	@Listen("onClick = #btnRemoverProfesoresComision")
	public void removerProfesor() {
		Listitem list2 = lsbProfesoresSeleccionados.getSelectedItem();
		if (list2 == null)
			Messagebox.show("Seleccione un profesor", "Advertencia",
					Messagebox.OK, Messagebox.EXCLAMATION);
		else
			list2.setParent(lsbProfesoresDisponibles);
	}

	// Metodo que permite limpiar las listas tanto de los profesores disponibles
	// como el de los seleccionados para formar parte de la comision evaluador
	@Listen("onClick = #btnCancelarComision")
	public void limpiarCampos() {
		List<Profesor> profesores = servicioProfesor.buscarActivos();
		lsbProfesoresDisponibles.setModel(new ListModelList<Profesor>(
				profesores));
		lsbProfesoresSeleccionados.getItems().clear();

	}

	// Metodo que permite guardar los integrantes de la comision evaluadora
	@Listen("onClick = #btnGuardarComision")
	public void GuardarComision() {

		Listitem listProfesoresSeleccionados = lsbProfesoresSeleccionados
				.getSelectedItem();
		if (listProfesoresSeleccionados == null)
			Messagebox
					.show("Debe Seleccionar los profesores que conformar�n la comisi�n evaluadora",
							"Error", Messagebox.OK, Messagebox.ERROR);
		else {

			Set<Profesor> profesoresSeleccionados = new HashSet<Profesor>();
			for (int i = 0; i < lsbProfesoresSeleccionados.getItemCount(); i++) {
				Profesor profesor = lsbProfesoresSeleccionados.getItems()
						.get(i).getValue();
				profesoresSeleccionados.add(profesor);
			}

			Teg tegSeleccionado = servicioTeg.buscarTeg(auxiliarId);
			tegSeleccionado.setProfesores(profesoresSeleccionados);
			tegSeleccionado.setEstatus("Comision Asignada");
			servicioTeg.guardar(tegSeleccionado);
			Messagebox.show("Comisi�n asignada exitosamente", "Informaci�n",
					Messagebox.OK, Messagebox.INFORMATION);
			salir();

		}

	}

}
