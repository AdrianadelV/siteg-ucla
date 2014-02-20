package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;
import controlador.catalogo.CCatalogoProfesor;
import controlador.catalogo.CCatalogoProfesorTematica;

@Controller
public class CReporteProfesorTeg extends CGeneral {

	private static final long serialVersionUID = 197224884104774871L;
	CCatalogoProfesorTematica catalogo = new CCatalogoProfesorTematica();
	CCatalogoProfesor catalogoProfesor = new CCatalogoProfesor();
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	private String[] estatusProyecto = { "Todos", "Solicitando Registro",
			"Proyecto Registrado", "Comision Asignada",
			"Factibilidad Evaluada", "Proyecto Factible",
			"Proyecto No Factible", "Proyecto en Desarrollo",
			"Avances Finalizados", "TEG Registrado", "Trabajo en Desarrollo",
			"Revisiones Finalizadas", "Solicitando Defensa", "Jurado Asignado",
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado" };

	@Wire
	private Window wdwReporteProfesorTeg;
	@Wire
	private Datebox dtbFechaReporteProfesorTeg;
	@Wire
	private Datebox dtbInicioReporteProfesorTeg;
	@Wire
	private Datebox dtbFinReporteProfesorTeg;
	@Wire
	private Textbox txtCedulaReporteProfesorTeg;
	@Wire
	private Combobox cmbEstatusReporteProfesorTeg;
	@Wire
	private Combobox cmbProgramaReporteProfesorTeg;
	@Wire
	private Combobox cmbAreaReporteProfesorTeg;
	@Wire
	private Combobox cmbTematicaReporteProfesorTeg;
	@Wire
	private Button btnProfesorReporteProfesorTeg;

	/*
	 * Metodo heredado del Controlador CGeneral donde se verifica que el mapa
	 * recibido del catalogo exista, tambien se buscan todos los programas
	 * disponibles, adicionando un nuevo item donde se puede seleccionar la
	 * opcion de "Todos", junto a esto se tiene una lista previamente cargada de
	 * manera estatica los estatus de la defensa y se llenan los campos
	 * correspondientes de la vista, asi como los objetos empleados dentro de
	 * este controlador.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(100000, "Todos", "", "", true, null);
		programas.add(programaa);
		cmbProgramaReporteProfesorTeg.setModel(new ListModelList<Programa>(
				programas));
		cmbEstatusReporteProfesorTeg.setModel(new ListModelList<String>(
				estatusProyecto));
		Selectors.wireComponents(comp, this, false);
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if ((String) map.get("cedula") != null) {
				Profesor profesor = servicioProfesor
						.buscarProfesorPorCedula((String) map.get("cedula"));
				txtCedulaReporteProfesorTeg.setValue(profesor.getCedula());
				dtbInicioReporteProfesorTeg.setValue((Date) map.get("fecha1"));
				dtbFinReporteProfesorTeg.setValue((Date) map.get("fecha2"));
				cmbEstatusReporteProfesorTeg.setValue((String)map.get("estatus"));
				if (map.get("area").equals(estatusProyecto[0])) {
					cmbProgramaReporteProfesorTeg.setValue((String) map
							.get("programa"));
					cmbAreaReporteProfesorTeg
							.setValue((String) map.get("area"));
					cmbTematicaReporteProfesorTeg.setValue((String) map
							.get("tematica"));
				} else {
					idTematica = (Long) map.get("tematica");

					Programa programa = servicioPrograma.buscar((Long) map
							.get("programa"));
					Tematica tematica = servicioTematica
							.buscarTematica((Long) map.get("tematica"));
					cmbProgramaReporteProfesorTeg
							.setValue(programa.getNombre());
					cmbAreaReporteProfesorTeg
							.setValue((String) map.get("area"));
					cmbTematicaReporteProfesorTeg
							.setValue(tematica.getNombre());
				}
			}
			map.clear();
			map = null;
		}
	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas.
	 */
	@Listen("onSelect = #cmbProgramaReporteProfesorTeg")
	public void seleccinarPrograma() {
		txtCedulaReporteProfesorTeg.setValue("");
		cmbAreaReporteProfesorTeg.setValue("");
		cmbTematicaReporteProfesorTeg.setValue("");
		if (cmbProgramaReporteProfesorTeg.getValue().equals(estatusProyecto[0])) {
			cmbAreaReporteProfesorTeg.setValue("Todos");
			cmbTematicaReporteProfesorTeg.setValue("Todos");
			cmbAreaReporteProfesorTeg.setDisabled(true);
			cmbTematicaReporteProfesorTeg.setDisabled(true);
		} else {
			cmbAreaReporteProfesorTeg.setDisabled(false);
			cmbTematicaReporteProfesorTeg.setDisabled(false);
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(Long.parseLong(cmbProgramaReporteProfesorTeg
							.getSelectedItem().getId())));
			cmbAreaReporteProfesorTeg
					.setModel(new ListModelList<AreaInvestigacion>(areas));
		}
	}

	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbAreaReporteProfesorTeg")
	public void seleccionarArea() {
		cmbTematicaReporteProfesorTeg.setValue("");
		tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
				.buscarArea(Long.parseLong(cmbAreaReporteProfesorTeg
						.getSelectedItem().getId())));
		cmbTematicaReporteProfesorTeg.setModel(new ListModelList<Tematica>(
				tematicas));
	}

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematicaReporteProfesorTeg")
	public void seleccionarTematica() {
		idTematica = Long.parseLong(cmbTematicaReporteProfesorTeg
				.getSelectedItem().getId());
	}

	/*
	 * Metodo que permite dado al condicional, mapear los datos vaciados en la
	 * vista, para poder ser utilizados en la vista asociada.
	 */
	@Listen("onClick = #btnProfesorReporteProfesorTeg")
	public void buscarProfesor() {
		if(idTematica !=0 || !cmbTematicaReporteProfesorTeg.getValue().equals("")){
			System.out.println(idTematica);
			System.out.println(cmbTematicaReporteProfesorTeg.getValue());
		final HashMap<String, Object> map2 = new HashMap<String, Object>();
			map2.put("fecha1", dtbInicioReporteProfesorTeg.getValue());
			map2.put("estatus", cmbEstatusReporteProfesorTeg.getValue());
			map2.put("fecha2", dtbFinReporteProfesorTeg.getValue());
		if (cmbProgramaReporteProfesorTeg.getValue().equals(estatusProyecto[0])) {
			map2.put("area", cmbAreaReporteProfesorTeg.getValue());
			map2.put("programa", cmbProgramaReporteProfesorTeg.getValue());
			map2.put("tematica", cmbTematicaReporteProfesorTeg.getValue());
			Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesor.zul", null, null);
			window.doModal();

			catalogoProfesor
					.recibir("reportes/estructurados/VReporteProfesorTeg");
		} else {
			map2.put("area", cmbAreaReporteProfesorTeg.getValue());
			Sessions.getCurrent().setAttribute("itemsCatalogo", map2);
			catalogo.recibir("reportes/estructurados/VReporteProfesorTeg", Long
					.parseLong(cmbProgramaReporteProfesorTeg.getSelectedItem()
							.getId()), Long
					.parseLong(cmbTematicaReporteProfesorTeg.getSelectedItem()
							.getId()));

			Window window = (Window) Executions.createComponents(
					"/vistas/catalogos/VCatalogoProfesorTematica.zul", null,
					null);
			window.doModal();
		}
		}else{
			Messagebox
			.show("Primero debe seleccionar una tematica");
			}

	}

	/* Metodo que permite cerrar la vista */
	@Listen("onClick = #btnSalirReporteProfesorTeg")
	public void salir() {
		wdwReporteProfesorTeg.onClose();
	}

	/*
	 * Metodo que permite limpiar los campos de la vista y colocarlos en el
	 * estado inicial
	 */
	@Listen("onClick = #btnCancelarReporteProfesorTeg")
	public void limpiarCampos() {
		cmbAreaReporteProfesorTeg.setDisabled(false);
		cmbTematicaReporteProfesorTeg.setDisabled(false);
		dtbFinReporteProfesorTeg.setValue(null);
		dtbInicioReporteProfesorTeg.setValue(null);
		cmbAreaReporteProfesorTeg.setValue("");
		cmbTematicaReporteProfesorTeg.setValue("");
		cmbProgramaReporteProfesorTeg.setValue("");
		cmbEstatusReporteProfesorTeg.setValue("");
		txtCedulaReporteProfesorTeg.setValue("");
		idTematica = 0;
	}

	/*
	 * Metodo que permite generar un reporte, dado a una tematica y un estatus
	 * del proyecto, se generara un pdf donde se muestra una lista de teg
	 * asociados a un profesor de esta seleccion, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteProfesorTeg")
	public void generarReporteProfesorTeg() throws JRException {
		String cedula = txtCedulaReporteProfesorTeg.getValue();
		Date fechaInicio = dtbInicioReporteProfesorTeg.getValue();
		Date fechaFin = dtbFinReporteProfesorTeg.getValue();
		String estatus = cmbEstatusReporteProfesorTeg.getValue();
		Tematica tematica = servicioTematica.buscarTematica(idTematica);
		Profesor profesor = servicioProfesor.buscarProfesorPorCedula(cedula);
		Map<String, Object> p = new HashMap<String, Object>();
		List<Teg> tegs = new ArrayList<Teg>();
		String rutaUrl = obtenerDirectorio();
		if (fechaFin == null || fechaInicio == null
				|| fechaInicio.after(fechaFin)) {
			Messagebox.show(
					"La fecha de inicio debe ser primero que la fecha de fin",
					"Error", Messagebox.OK, Messagebox.ERROR);
		} else {if(!estatus.equals("")|| profesor != null || !cmbTematicaReporteProfesorTeg.getValue().equals("")){
			if (estatus.equals(estatusProyecto[0])) {
				if (cmbTematicaReporteProfesorTeg.getValue().equals(
						estatusProyecto[0])) {
					tegs = servicioTeg.buscarTodosTegsDeTutorPorDosFechas(
							profesor, fechaInicio, fechaFin);
					p.put("tematica", "Todas las Tematicas");
					p.put("area", "Todas las Areas");
					p.put("modeloReporte", "1");
				} else {
					tegs = servicioTeg.buscarTegsDeTutorPorDosFechasYTematica(
							profesor, tematica, fechaInicio, fechaFin);
					p.put("tematica", tematica.getNombre());
					p.put("area", tematica.getareaInvestigacion().getNombre());
					p.put("modeloReporte", "2");
				}
				p.put("estatus", "Todos los estatus");
			} else {
				if (cmbTematicaReporteProfesorTeg.getValue().equals(
						estatusProyecto[0])) {
					tegs = servicioTeg.buscarTegsDeTutorPorDosFechasYEstatus(
							profesor, estatus, fechaInicio, fechaFin);
					p.put("tematica", "Todas las Tematicas");
					p.put("area", "Todas las Areas");
					p.put("modeloReporte", "3");
				} else {

					tegs = servicioTeg
							.buscarTegsDeTutorPorTematicaPorDosFechasYEstatus(
									profesor, tematica, estatus, fechaInicio,
									fechaFin);
					p.put("tematica", tematica.getNombre());
					p.put("area", tematica.getareaInvestigacion().getNombre());
					p.put("modeloReporte", "4");
				}

				p.put("estatus", estatus);
			}
			if (!tegs.isEmpty()) {
				p.put("cedula", profesor.getCedula());
				p.put("nombre", profesor.getNombre());
				p.put("apellido", profesor.getApellido());
				p.put("fecha1", fechaInicio);
				p.put("fecha2", fechaFin);
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/estructurados/compilados/RProyectosProfesor.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				System.out.println(reporteSrc);
//				p.put("logoUcla", reporteImage + "logo ucla.png");
//				p.put("logoCE", reporteImage + "logo CE.png");
//				p.put("logoSiteg", reporteImage + "logo.png");
				JasperReport jasperReport = (JasperReport) JRLoader
						.loadObject(getClass().getResource(
								"RProyectosProfesor.jasper"));
				// JasperReport jasperReport = (JasperReport) JRLoader
				// .loadObject(reporteSrc);

				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(tegs));
				JasperViewer.viewReport(jasperPrint, false);
				p.clear();
				p = null;
			} else {
				Messagebox
						.show("No hay informacion disponible para esta selecci�n");
			}
		}else{
			Messagebox
			.show("Debe completar todos los campos");
		}
		}

	}

}
