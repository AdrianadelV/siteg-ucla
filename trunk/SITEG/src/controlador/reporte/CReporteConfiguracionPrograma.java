package controlador.reporte;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.Requisito;
import modelo.SolicitudTutoria;
import modelo.compuesta.CondicionPrograma;
import modelo.compuesta.ProgramaArea;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Window;

import servicio.SLapso;
import servicio.SPrograma;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

@Controller
public class CReporteConfiguracionPrograma extends CGeneral {

	private static Programa programa;
	private static Lapso lapso;

	@Wire
	private Combobox cmbConfiguracionPrograma;
	@Wire
	private Combobox cmbConfiguracionLapso;
	@Wire
	private Window wdwReporteConfigurar;
	@Wire
	private Radiogroup rdgConfiguracion;
	@Wire
	private Radio rdocondicion;
	@Wire
	private Radio rdoitem;
	@Wire
	private Radio rdoarea;
	@Wire
	private Radio rdorequisito;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas y lapsos disponibles y se llena una lista del mismo en el componente de
	 * la vista
	 */
	@Override
	public void inicializar(Component comp) {
		List<Programa> programas = servicioPrograma.buscarActivas();
		List<Lapso> lapsos = servicioLapso.buscarActivos();

		cmbConfiguracionLapso.setModel(new ListModelList<Lapso>(lapsos));
		cmbConfiguracionPrograma
				.setModel(new ListModelList<Programa>(programas));

	}

	/*
	 * Metodo que permite generar un reporte, dependiendo de la opcion que elija
	 * , ya sea por condicion. por area, por item o por requisito se
	 * generara un pdf donde se muestra una lista del mismo, mediante el
	 * componente "Jasperreport" donde se mapea una serie de parametros y una
	 * lista previamente cargada que seran los datos que se muestra en el
	 * documento.
	 */
	@Listen("onClick = #btnGenerarReporteConfiguracion")
	public void GenerarReporte() throws JRException {

		if (cmbConfiguracionPrograma.getText().compareTo("") == 0
				|| cmbConfiguracionLapso.getText().compareTo("") == 0
				|| (rdoitem.isChecked() == false
						&& rdoarea.isChecked() == false
						&& rdocondicion.isChecked() == false && rdorequisito
						.isChecked() == false)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);

		} else {

			long idprograma = Long.parseLong(cmbConfiguracionPrograma
					.getSelectedItem().getId());
			programa = servicioPrograma.buscar(idprograma);
			long idlapso = Long.parseLong(cmbConfiguracionLapso
					.getSelectedItem().getId());
			lapso = servicioLapso.buscarLapso(idlapso);
			if (rdocondicion.isChecked() == true) {

				System.out.println("pase por donde era");
				List<CondicionPrograma> condicion = servicioCondicionPrograma
						.buscarCondicionesPrograma(programa, lapso);
				if (condicion.size() != 0) {

					Map p = new HashMap();
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("nombreprograma",
							cmbConfiguracionPrograma.getValue());
					mapa.put("nombrelapso", cmbConfiguracionLapso.getValue());

					FileSystemView filesys = FileSystemView.getFileSystemView();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/ReporteProgramaCondicion.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									condicion));

					JasperViewer.viewReport(jasperPrint, false);

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}
			} else if (rdoarea.isChecked() == true) {
				List<AreaInvestigacion> programaarea = servicioProgramaArea
						.buscarAreasDePrograma(programa, lapso);
				if (programaarea.size() != 0) {

					Map p = new HashMap();
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("nombreprograma",
							cmbConfiguracionPrograma.getValue());
					mapa.put("nombrelapso", cmbConfiguracionLapso.getValue());

					FileSystemView filesys = FileSystemView.getFileSystemView();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/ReporteProgramaArea.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									programaarea));

					JasperViewer.viewReport(jasperPrint, false);

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}

			} else if (rdoitem.isChecked() == true) {
				List<ItemEvaluacion> programaitem = servicioProgramaItem
						.buscarItemsEnPrograma(programa, lapso);
				if (programaitem.size() != 0) {

					Map p = new HashMap();
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("nombreprograma",
							cmbConfiguracionPrograma.getValue());
					mapa.put("nombrelapso", cmbConfiguracionLapso.getValue());

					FileSystemView filesys = FileSystemView.getFileSystemView();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/ReporteProgramaItem.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									programaitem));

					JasperViewer.viewReport(jasperPrint, false);

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}

			}

			else if (rdorequisito.isChecked() == true) {
				List<Requisito> programarequisito = servicioProgramaRequisito
						.buscarRequisitos(programa, lapso);
				if (programarequisito.size() != 0) {

					Map p = new HashMap();
					Map<String, Object> mapa = new HashMap<String, Object>();
					mapa.put("nombreprograma",
							cmbConfiguracionPrograma.getValue());
					mapa.put("nombrelapso", cmbConfiguracionLapso.getValue());

					FileSystemView filesys = FileSystemView.getFileSystemView();
					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/salidas/compilados/ReporteProgramaRequisito.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, mapa, new JRBeanCollectionDataSource(
									programarequisito));

					JasperViewer.viewReport(jasperPrint, false);

				} else {
					Messagebox
							.show("No ha informacion disponible para este intervalo");
				}

			}
		}

	}

	/* Metodo que permite limpiar los campos de los filtros de busqueda */
	@Listen("onClick = #btnCancelarReporteConfiguracion")
	public void cancelar() throws JRException {

		cmbConfiguracionPrograma.setValue("");
		cmbConfiguracionLapso.setValue("");
		rdgConfiguracion.setSelectedItem(null);

	}
	/*Metodo que permite cerrar la vista*/
	@Listen("onClick = #btnSalirReporteConfigurcion")
	public void salir() throws JRException {

		wdwReporteConfigurar.onClose();

	}

}