package controlador.reporte;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.SolicitudTutoria;
import modelo.Teg;
import modelo.Tematica;
import modelo.reporte.ListaTeg;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;

import servicio.SAreaInvestigacion;
import servicio.SJurado;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.SSolicitudTutoria;
import servicio.STeg;
import servicio.STematica;
import servicio.SEstudiante;

@Controller
public class CReporteTeg extends CGeneral {

	private String[] estatusTeg = { "Todos", "TEG Registrado",
			"Revisiones Finalizadas", "Solicitando Defensa",
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado",
			"Jurado Asignado" };
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	List<Tematica> tematicas = new ArrayList<Tematica>();
	List<Programa> programas = new ArrayList<Programa>();
	long idTematica = 0;
	long idArea = 0;

	@Wire
	private Window wdwReporteTeg;
	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbEstatus;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Jasperreport jstVistaPrevia;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos", junto a esto se tiene una lista
	 * previamente cargada de manera estatica, los estatus de la defensa y se
	 * llena una lista del mismo en el componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		programas = servicioPrograma.buscarActivas();
		Programa programaa = new Programa(987, "Todos", "", "", true, null);
		programas.add(programaa);

		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbEstatus.setModel(new ListModelList<String>(estatusTeg));
	}

	/*
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area y tematica, ademas se adiciona un nuevo item donde
	 * se puede seleccionar la opcion de "Todos" en el combo de las areas
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccinarPrograma() {
		if (cmbPrograma.getValue().equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
		} else {
			cmbArea.setValue("");
			cmbTematica.setValue("");
			Programa programa = (Programa) cmbPrograma.getSelectedItem()
					.getValue();
			areas = servicioProgramaArea.buscarAreasDePrograma(servicioPrograma
					.buscar(programa.getId()));
			AreaInvestigacion area = new AreaInvestigacion(10000000, "Todos",
					"", true);
			areas.add(area);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));
		}

	}

	/*
	 * Metodo que permite cargar las tematicas dado al area seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo tematica
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() {
		if (cmbArea.getValue().equals("Todos")) {

			cmbTematica.setValue("Todos");
		} else {
			cmbTematica.setValue("");
			AreaInvestigacion tematica = (AreaInvestigacion) cmbArea
					.getSelectedItem().getValue();
			tematicas = servicioTematica.buscarTematicasDeArea(servicioArea
					.buscarArea(tematica.getId()));
			cmbTematica.setModel(new ListModelList<Tematica>(tematicas));
		}
	}

	/*
	 * Metodo que permite extraer el valor del id de la tematica al seleccionar
	 * uno en el campo del mismo.
	 */
	@Listen("onSelect = #cmbTematica")
	public void seleccionarTematica() {
		Tematica tematica = (Tematica) cmbTematica.getSelectedItem().getValue();
		// idTematica = tematica.getId();
	}

	/*
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * y unos estatus previamente establecidos, se generara un pdf donde se
	 * muestra una lista de tegs de esta seleccion, mediante el componente
	 * "Jasperreport" donde se mapea una serie de parametros y una lista
	 * previamente cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarReporteTeg")
	public void generarReporteTEG() throws JRException {
		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		Date fechaInicio = dtbFechaInicio.getValue();
		Date fechaFin = dtbFechaFin.getValue();
		String estatus = cmbEstatus.getValue();
		List<Teg> teg = null;
		if ((cmbPrograma.getValue() == "") || (cmbArea.getValue() == "")
				|| (cmbTematica.getValue() == "")
				|| (cmbEstatus.getValue() == "")) {
			Messagebox.show("Datos imcompletos", "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
		}

		else {
			if (fechaFin == null || fechaInicio == null
					|| fechaInicio.after(fechaFin)) {
				Messagebox
						.show("La fecha de inicio debe ser primero que la fecha de fin",
								"Error", Messagebox.OK, Messagebox.ERROR);
			} else {
				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idTematica = cmbTematica.getSelectedItem().getId();
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
					teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
									estatus, tematica1, fechaInicio, fechaFin);

				}

				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")
						&& estatus.equals("Todos")) {
					String idTematica = cmbTematica.getSelectedItem().getId();
					Tematica tematica1 = servicioTematica.buscarTematica(Long
							.parseLong(idTematica));
					String estatusTeg1 = "TEG Registrado";
					String estatusTeg2 = "Revisiones Finalizadas";
					String estatusTeg3 = "Solicitando Defensa";
					String estatusTeg4 = "Defensa Asignada";
					String estatusTeg5 = "TEG Aprobado";
					String estatusTeg6 = "TEG Reprobado";
					String estatusTeg7 = "Jurado Asignado";
					teg = servicioTeg
							.buscarTegDeUnaTematicaPorDosFechasyVariosEstatus(
									estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, tematica1, fechaInicio,
									fechaFin);
				}

				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& !estatus.equals("Todos")) {
					String idPrograma = cmbPrograma.getSelectedItem().getId();
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					teg = servicioTeg.buscarTegPorProgramaVariasAreasUnEstatus(
							estatus, programa1, fechaInicio, fechaFin);
				}
				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")
						&& estatus.equals("Todos")) {
					String idPrograma = cmbPrograma.getSelectedItem().getId();
					Programa programa1 = servicioPrograma.buscar(Long
							.parseLong(idPrograma));
					String estatusTeg1 = "TEG Registrado";
					String estatusTeg2 = "Revisiones Finalizadas";
					String estatusTeg3 = "Solicitando Defensa";
					String estatusTeg4 = "Defensa Asignada";
					String estatusTeg5 = "TEG Aprobado";
					String estatusTeg6 = "TEG Reprobado";
					String estatusTeg7 = "Jurado Asignado";
					teg = servicioTeg
							.buscarTegPorProgramaVariasAreasVariosEstatus(
									estatusTeg1, estatusTeg2, estatusTeg3,
									estatusTeg4, estatusTeg5, estatusTeg6,
									estatusTeg7, programa1, fechaInicio,
									fechaFin);
				}
				if (nombrePrograma.equals("Todos") && !estatus.equals("Todos")) {
					teg = servicioTeg.buscarTegPorVariosProgramaUnEstatus(
							estatus, fechaInicio, fechaFin);
				}

				if (nombrePrograma.equals("Todos") && estatus.equals("Todos")) {
					String estatusTeg1 = "TEG Registrado";
					String estatusTeg2 = "Revisiones Finalizadas";
					String estatusTeg3 = "Solicitando Defensa";
					String estatusTeg4 = "Defensa Asignada";
					String estatusTeg5 = "TEG Aprobado";
					String estatusTeg6 = "TEG Reprobado";
					String estatusTeg7 = "Jurado Asignado";
					teg = servicioTeg.buscarTegPorVariosProgramasVariosEstatus(
							estatusTeg1, estatusTeg2, estatusTeg3, estatusTeg4,
							estatusTeg5, estatusTeg6, estatusTeg7, fechaInicio,
							fechaFin);
				}

				List<ListaTeg> elementos = new ArrayList<ListaTeg>();
				for (Teg t : teg) {
					List<Estudiante> estudiantes = servicioEstudiante
							.buscarEstudiantePorTeg(t);

					String nombreEstudiantes = "";
					for (Estudiante e : estudiantes) {
						nombreEstudiantes += e.getNombre() + " "
								+ e.getApellido() + " ";
					}

					elementos.add(new ListaTeg(t, nombreEstudiantes));

				}
				FileSystemView filesys = FileSystemView.getFileSystemView();
				Map p = new HashMap();
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/estructurados/compilados/ReporteTEG.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				p.put("programa", cmbPrograma.getValue());
				p.put("FechaInicio", dtbFechaInicio.getValue());
				p.put("FechaFin", dtbFechaFin.getValue());
				p.put("Area", cmbArea.getValue());
				p.put("Tematica", cmbTematica.getValue());
				p.put("Estatus", cmbEstatus.getValue());
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jstVistaPrevia.setSrc(reporteSrc);
				jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
						elementos));
				jstVistaPrevia.setType("pdf");
				jstVistaPrevia.setParameters(p);
			}

		}
	}
/* Metodo que permite limpiar los campos de los filtros de busqueda.*/
	@Listen("onClick = #btnSalirReporteTeg")
	public void cancelarItem() {
		cmbEstatus.setValue("");
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbTematica.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
	}
}