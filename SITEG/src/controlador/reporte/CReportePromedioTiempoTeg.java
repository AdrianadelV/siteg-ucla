package controlador.reporte;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;

import modelo.Programa;

import modelo.Teg;
import modelo.TegEstatus;
import modelo.Tematica;
import modelo.reporte.PromedioTiempoTeg;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;
import controlador.CGeneral;
import controlador.catalogo.CCatalogoTeg;

import servicio.SAreaInvestigacion;
import servicio.SPrograma;
import servicio.SProgramaArea;
import servicio.STeg;
import servicio.STegEstatus;
import servicio.STematica;

@Controller
public class CReportePromedioTiempoTeg extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	STegEstatus servicioTegEstatus = GeneradorBeans.getServicioTegEstatus();
	SProgramaArea servicioProgramaArea = GeneradorBeans
			.getServicioProgramaArea();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SAreaInvestigacion servicioArea = GeneradorBeans.getServicioArea();
	STematica servicioTematica = GeneradorBeans.getSTematica();
	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	Tematica tematica = new Tematica();
	CCatalogoTeg catalogo = new CCatalogoTeg();
	List<Teg> tegs = new ArrayList();
	long idTeg = 0;

	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbTematica;
	@Wire
	private Window wdwReportePromedioTiempoTeg;
	@Wire
	private Jasperreport jstVistaPrevia;

	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaTodos = new Programa(10000, "Todos", "", "", true,
				null);
		programas.add(programaTodos);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
	}

	@Listen("onClick = #btnGenerarReportePromedioTiempoTeg")
	public void generarPromedioTiempoTeg() throws JRException {

		List<String> nombreEstatus = new ArrayList();
		nombreEstatus.add("Solicitando Registro");
		nombreEstatus.add("Proyecto Registrado");
		nombreEstatus.add("Comision Asignada");
		nombreEstatus.add("Factibilidad Evaluada");
		nombreEstatus.add("Proyecto Factible");
		nombreEstatus.add("Proyecto en Desarrollo");
		nombreEstatus.add("Avances Finalizados");
		nombreEstatus.add("TEG Registrado");
		nombreEstatus.add("Trabajo en Desarrollo");
		nombreEstatus.add("Revisiones Finalizadas");
		nombreEstatus.add("Solicitando Defensa");
		nombreEstatus.add("Jurado Asignado");
		nombreEstatus.add("Defensa Asignada");
		nombreEstatus.add("TEG Aprobado");
		nombreEstatus.add("TEG Reprobado");

		long contador13 = 0;
		long contador46 = 0;
		long contador7 = 0;
		long cantidadTotal = 0;
		String estatus1 = "";
		String estatus2 = "";
		List<PromedioTiempoTeg> tegsPromedios = new ArrayList();
		int tamanioListaEstatus = nombreEstatus.size();

		if (tamanioListaEstatus % 2 != 0) {
			tamanioListaEstatus = tamanioListaEstatus - 3;
		}
		filtrarDatosBusqueda();
		if (tegs.size() != 0) {
			for (int i = 0; i < nombreEstatus.size(); i++) {
				
				List<TegEstatus> tegEstatus1 = servicioTegEstatus
						.buscarEstatusSegunTeg(nombreEstatus.get(i), tegs);
				List<TegEstatus> tegEstatus2 = servicioTegEstatus
						.buscarEstatusSegunTeg(nombreEstatus.get(i + 1), tegs);

				if (nombreEstatus.size() % 2 != 0 && tamanioListaEstatus == i) {
					List<TegEstatus> tegEstatus3 = servicioTegEstatus
							.buscarEstatusSegunTeg(nombreEstatus.get(i + 2),
									tegs);
					if (tegEstatus3.size() != 0) {
						for (int q = 0; q < tegEstatus3.size(); q++) {
							tegEstatus2.add(tegEstatus3.get(q));
						}

					}
					estatus1 = nombreEstatus.get(i);
					estatus2 = nombreEstatus.get(i + 1) + "/"
							+ nombreEstatus.get(i + 2);

					i = nombreEstatus.size();

				} else {

					estatus1 = nombreEstatus.get(i);
					estatus2 = nombreEstatus.get(i + 1);
				}

				for (int j = 0; j < tegEstatus1.size(); j++) {
					for (int v = 0; v < tegEstatus2.size(); v++) {
						if (tegEstatus1.get(j).getTeg().getId() == tegEstatus1
								.get(v).getTeg().getId()) {

							Date fecha1 = tegEstatus1.get(j).getFechaEstatus();
							Date fecha2 = tegEstatus2.get(v).getFechaEstatus();

							Calendar calendarioFecha1 = Calendar.getInstance();
							calendarioFecha1.setTime(fecha1);

							Calendar calendarioFecha2 = Calendar.getInstance();
							calendarioFecha2.setTime(fecha2);

							long miliSegundoFecha1 = calendarioFecha1
									.getTimeInMillis();
							long miliSegundoFecha2 = calendarioFecha2
									.getTimeInMillis();
							long diferenciaFecha = miliSegundoFecha2
									- miliSegundoFecha1;
							long direfenciaDias = Math.abs(diferenciaFecha
									/ (24 * 60 * 60 * 1000));

							cantidadTotal = cantidadTotal + 1;

							if (direfenciaDias >= 0 && direfenciaDias <= 3) {
								contador13 = contador13 + 1;
							} else if (direfenciaDias >= 4
									&& direfenciaDias <= 6) {
								contador46 = contador46 + 1;
							} else {
								contador7 = contador7 + 1;
							}
						}

					}
				}
				PromedioTiempoTeg promedio = new PromedioTiempoTeg(estatus1
						+ "-" + estatus2, contador13, contador46, contador7);
				tegsPromedios.add(promedio);

				contador13 = 0;
				contador46 = 0;
				contador7 = 0;
				cantidadTotal = 0;

				i = i + 1;
			}
			FileSystemView filesys = FileSystemView.getFileSystemView();
			Map parametro = new HashMap();
			String rutaUrl = obtenerDirectorio();
			String reporteSrc = rutaUrl
					+ "SITEG/vistas/reportes/estadisticos/compilados/RPromedioTiempoTeg.jasper";
			String reporteImage = rutaUrl + "SITEG/public/imagenes/reportes/";
			parametro.put("titulo",
					"UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
							+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
							+ "DIRECCION DE PROGRAMA");
			parametro.put("programaNombre", cmbPrograma.getValue());
			parametro.put("areaNombre", cmbArea.getValue());
			parametro.put("tematicaNombre", cmbTematica.getValue());
			parametro.put("logoUcla", reporteImage + "logo ucla.png");
			parametro.put("logoCE", reporteImage + "logo CE.png");
			parametro.put("logoSiteg", reporteImage + "logo.png");
			
			
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(reporteSrc);

			
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, parametro, new JRBeanCollectionDataSource(
							tegsPromedios));
			
			JasperViewer.viewReport(jasperPrint, false);
//			jstVistaPrevia.setSrc(reporteSrc);
//			jstVistaPrevia.setDatasource(new JRBeanCollectionDataSource(
//					tegsPromedios));
//			jstVistaPrevia.setType("pdf");
//			jstVistaPrevia.setParameters(parametro);

		} else {
			Messagebox.show("No hay informacion disponible");
			cancelarPromedioTiempoTeg();
		}
	}

	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		String idPrograma = cmbPrograma.getSelectedItem().getId();
		String nombrePrograma = cmbPrograma.getValue();
		if (nombrePrograma.equals("Todos")) {
			cmbArea.setValue("Todos");
			cmbTematica.setValue("Todos");
			cmbArea.setDisabled(true);
			cmbTematica.setDisabled(true);

		} else {
			cmbArea.setDisabled(false);
			cmbArea.setValue("");
			programa = servicioPrograma.buscar(Long.parseLong(idPrograma));
			List<AreaInvestigacion> programaAreas = servicioProgramaArea
					.buscarAreasDePrograma(programa);
			AreaInvestigacion areaInvestigacion = new AreaInvestigacion(
					1000000, "Todos", "", true);
			programaAreas.add(areaInvestigacion);
			cmbArea.setModel(new ListModelList<AreaInvestigacion>(programaAreas));
		}
	}

	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() throws JRException {
		String idArea = cmbArea.getSelectedItem().getId();
		String nombreArea = cmbArea.getValue();
		if (nombreArea.equals("Todos")) {
			cmbTematica.setValue("Todos");
			cmbTematica.setDisabled(true);
		} else {
			cmbTematica.setDisabled(false);
			cmbTematica.setValue("");
			area = servicioArea.buscarArea(Long.parseLong(idArea));
			List<Tematica> tematicasTodos = servicioTematica
					.buscarTematicasDeArea(area);
			Tematica tematicaTodos = new Tematica(10000000, "Todos", "", true,
					null);
			tematicasTodos.add(tematicaTodos);
			cmbTematica.setModel(new ListModelList<Tematica>(tematicasTodos));
		}
	}

	@Listen("onClick = #btnCancelarReportePromedioTiempoTeg")
	public void cancelarPromedioTiempoTeg() throws JRException {
		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbTematica.setValue("");
		cmbTematica.setDisabled(true);
		tegs = null;
		jstVistaPrevia.setSrc("");
		jstVistaPrevia.setDatasource(null);
	}
	@Listen("onClick = #btnSalirReportePromedioTiempoTeg")
	public void salirPromedioTiempoTeg() throws JRException {
		cancelarPromedioTiempoTeg();
		wdwReportePromedioTiempoTeg.onClose();
	}
	
	
	public void filtrarDatosBusqueda() {

		String nombreArea = cmbArea.getValue();
		String nombrePrograma = cmbPrograma.getValue();
		String nombreTematica = cmbTematica.getValue();
		String estatusAprobado = "TEG Aprobado";
		String estatusReprobado = "TEG Reprobado";
		tegs = null;
		if (nombrePrograma.equals("Todos")) {
			tegs = servicioTeg.buscarTegSegunEstatus(estatusAprobado,
					estatusReprobado);
		} else if (!nombrePrograma.equals("Todos")
				&& nombreArea.equals("Todos")) {
			tegs = servicioTeg.buscarTegSegunProgramaEstatus(programa,
					estatusAprobado, estatusReprobado);
		} else if (!nombrePrograma.equals("Todos")
				&& !nombreArea.equals("Todos")
				&& !nombreTematica.equals("Todos")) {
			Tematica tematica = servicioTematica
					.buscarTematicaPorNombre(nombreTematica);
			tegs = servicioTeg.buscarTegSegunTematicaEstatus(tematica,
					estatusAprobado, estatusReprobado);
		} else if (!nombrePrograma.equals("Todos")
				&& !nombreArea.equals("Todos")
				&& nombreTematica.equals("Todos")) {
			tegs = servicioTeg.buscarTegSegunAreaInvestigacionEstatus(area,
					estatusAprobado, estatusReprobado);
		}

	}

}