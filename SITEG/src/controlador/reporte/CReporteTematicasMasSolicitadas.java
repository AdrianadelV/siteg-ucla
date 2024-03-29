package controlador.reporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.AreaInvestigacion;
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
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

@Controller
public class CReporteTematicasMasSolicitadas extends CGeneral {

	Programa programa = new Programa();
	AreaInvestigacion area = new AreaInvestigacion();
	List<AreaInvestigacion> areas = new ArrayList<AreaInvestigacion>();
	String estatusProyectoTeg1, estatusProyectoTeg2 = "";

	@Wire
	private Datebox dtbFechaInicio;
	@Wire
	private Datebox dtbFechaFin;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Combobox cmbArea;
	@Wire
	private Combobox cmbEtapaTeg;
	@Wire
	private Window wdwReporteTematicasMasSolicitadas;
	private static Date fechaInicio;
	private static Date fechaFin;

	/**
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * programas disponibles, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos" y se llena una lista del mismo en el
	 * componente de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub
		List<Programa> programas = servicioPrograma.buscarActivas();
		Programa programaFicticio = new Programa(10000, "Todos", "", "", true,
				null);
		programas.add(programaFicticio);
		cmbPrograma.setModel(new ListModelList<Programa>(programas));
		cmbArea.setDisabled(true);
		cmbEtapaTeg.setDisabled(true);

	}

	/**
	 * Metodo que permite generar un reporte, dado a un programa, area, tematica
	 * se generara un pdf donde se muestra una lista de las cinco tematicas mas
	 * solicitadas de esta seleccion, para esto se realiza varias operaciones
	 * dentro de ella, donde primero se cuentan la cantidad de tematicas dado a
	 * unos estatus y otros parametros, luego se ordena de mayor a menor y el
	 * ultimo paso es donde cargan las cinco primeras tematicas de la lista
	 * ordenada que se consiguio anteriormente, donde mediante el componente
	 * "Jasperreport" se mapea una serie de parametros y una lista previamente
	 * cargada que seran los datos que se muestra en el documento.
	 */
	@Listen("onClick = #btnGenerarTematicasSolicitadas")
	public void generarProyectosSolicitados() throws JRException {

		if ((cmbPrograma.getText().compareTo("") == 0)
				|| (cmbArea.getText().compareTo("") == 0)
				|| (cmbEtapaTeg.getText().compareTo("") == 0)
				|| (dtbFechaInicio.getValue() == null)
				|| (dtbFechaFin.getValue() == null)) {
			Messagebox.show("Debe completar todos los campos", "Error",
					Messagebox.OK, Messagebox.ERROR);
		} else {

			fechaInicio = dtbFechaInicio.getValue();
			fechaFin = dtbFechaFin.getValue();

			if (fechaInicio.after(fechaFin)) {

				Messagebox
						.show("La fecha de fin debe ser posterior a la fecha de inicio",
								"Error", Messagebox.OK, Messagebox.ERROR);

			} else {

				String nombreArea = cmbArea.getValue();
				String nombrePrograma = cmbPrograma.getValue();
				String etapaTeg = cmbEtapaTeg.getValue();

				Date fechaInicio = new Date();
				Date fechaFin = new Date();
				fechaInicio = dtbFechaInicio.getValue();
				fechaFin = dtbFechaFin.getValue();
				List<Long> tematicasSeleccionadas = new ArrayList();
				List<Teg> tematicasSeleccionadas1 = new ArrayList<Teg>();
				System.out.println("estatus1:" + estatusProyectoTeg1);
				System.out.println("estatus2:" + estatusProyectoTeg2);
				String idPrograma = cmbPrograma.getSelectedItem().getId();
				Programa programa1 = servicioPrograma.buscar(Long
						.parseLong(idPrograma));

				String idArea = cmbArea.getSelectedItem().getId();
				AreaInvestigacion area1 = servicioArea.buscarArea(Long
						.parseLong(idArea));

				if (!nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")) {

					tematicasSeleccionadas1 = servicioTeg
							.buscarUltimasTematicasProgramaAreaEstatus(
									programa1, area1, fechaInicio, fechaFin,
									estatusProyectoTeg1, estatusProyectoTeg2);

				}
				if (nombrePrograma.equals("Todos")
						&& !nombreArea.equals("Todos")) {

					tematicasSeleccionadas1 = servicioTeg
							.buscarUltimasTematicasAreaEstatus(area1,
									fechaInicio, fechaFin, estatusProyectoTeg1,
									estatusProyectoTeg2);

				}

				if (nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")) {

					tematicasSeleccionadas = servicioTeg.buscarUltimasEstatus(
							estatusProyectoTeg1, estatusProyectoTeg2,
							fechaInicio, fechaFin);

				}

				if (!nombrePrograma.equals("Todos")
						&& nombreArea.equals("Todos")) {

					tematicasSeleccionadas1 = servicioTeg
							.buscarUltimasTematicasProgramaEstatus(programa1,
									fechaInicio, fechaFin, estatusProyectoTeg1,
									estatusProyectoTeg2);

				}
				for (int i = 0; i < tematicasSeleccionadas1.size(); i++) {
					tematicasSeleccionadas.add(tematicasSeleccionadas1.get(i)
							.getTematica().getId());
				}

				if (tematicasSeleccionadas.size() != 0) {

					/**************************** Contador de Lista ************* ******************/
					List<Long> tematicas = new ArrayList();
					List<Integer> contadores = new ArrayList();
					long tematica = tematicasSeleccionadas.get(0);
					int contadorTematicas = 0;
					for (int i = 0; i < tematicasSeleccionadas.size(); i++) {
						if (tematica == tematicasSeleccionadas.get(i)) {
							contadorTematicas = contadorTematicas + 1;
						} else {
							tematicas.add(tematica);
							contadores.add(contadorTematicas);
							tematica = tematicasSeleccionadas.get(i);
							contadorTematicas = 1;
						}
					}
					tematicas.add(tematica);
					contadores.add(contadorTematicas);
					/**************************** Ordenado de Lista ************* ******************/
					List<Long> tematicasOrdenados = new ArrayList();
					List<Integer> contadoresOrdenados = new ArrayList();
					List<Tematica> tematicasFinales = new ArrayList();
					int valor = 0;
					int valor2 = 0;
					long valorTematica = 0;
					for (int i = 0; i < contadores.size(); i++) {
						valor = contadores.get(i);
						for (int j = 0; j < contadores.size(); j++) {
							valor2 = contadores.get(j);
							valorTematica = tematicas.get(j);
							if (valor2 > valor) {
								contadores.remove(j);
								tematicas.remove(j);
								contadoresOrdenados.add(valor2);
								tematicasOrdenados.add(valorTematica);
								j = contadores.size();
							}
						}
					}
					if (contadoresOrdenados.size() < 5
							&& contadores.size() != 0) {
						int cantidadFaltante = 5 - contadoresOrdenados.size();
						for (int i = 0; i < cantidadFaltante; i++) {
							if (i < contadores.size()) {
								contadoresOrdenados.add(contadores.get(i));
								tematicasOrdenados.add(tematicas.get(i));
							}
						}
					}
					if (contadoresOrdenados.size() > 5) {
						for (int i = 5; i < contadoresOrdenados.size(); i++) {
							contadoresOrdenados.remove(i);
							tematicasOrdenados.remove(i);
						}
					}
					for (int i = 0; i < tematicasOrdenados.size(); i++) {
						Tematica tematicaFinal = servicioTematica
								.buscarTematica(tematicasOrdenados.get(i));
						tematicasFinales.add(tematicaFinal);
					}

					List<Teg> tegsTematicasComparativo = new ArrayList();
					long contadorFactibleAprobado = 0, contadorNoFactibleReprobado = 0;
					for (int i = 0; i < tematicasFinales.size(); i++) {
						contadorFactibleAprobado = servicioTeg.contadorEstatus(
								estatusProyectoTeg1, tematicasFinales.get(i),
								fechaInicio, fechaFin);
						contadorNoFactibleReprobado = servicioTeg
								.contadorEstatus(estatusProyectoTeg2,
										tematicasFinales.get(i), fechaInicio,
										fechaFin);
						Teg teg = new Teg(contadorFactibleAprobado, "", null,
								null, null, null, contadorNoFactibleReprobado,
								null, "", tematicasFinales.get(i), null, null);
						tegsTematicasComparativo.add(teg);
						contadorFactibleAprobado = 0;
						contadorNoFactibleReprobado = 0;
					}

					FileSystemView filesys = FileSystemView.getFileSystemView();

					String rutaUrl = obtenerDirectorio();
					String reporteSrc = rutaUrl
							+ "SITEG/vistas/reportes/estadisticos/compilados/RTematicasMasSolicitadas.jasper";
					String reporteImage = rutaUrl
							+ "SITEG/public/imagenes/reportes/";
					Map parametro = new HashMap();
					parametro.put("titulo",
							"UNIVERSIDAD CENTROCCIDENTAL LISANDRO ALVARADO"
									+ "DECANATO DE CIENCIAS Y TECNOLOGIA"
									+ "DIRECCION DE PROGRAMA");
					parametro.put("programaNombre", cmbPrograma.getValue());
					parametro.put("areaNombre", cmbArea.getValue());
					parametro.put("etapaTeg", etapaTeg);
					parametro.put("estatusProyectoTeg1", estatusProyectoTeg1);
					parametro.put("estatusProyectoTeg2", estatusProyectoTeg2);
					parametro.put("fechaInicio", fechaInicio);
					parametro.put("fechaFin", fechaFin);
					parametro.put("logoUcla", reporteImage + "logo ucla.png");
					parametro.put("logoCE", reporteImage + "logo CE.png");
					parametro.put("logoSiteg", reporteImage + "logo.png");

					JasperReport jasperReport = (JasperReport) JRLoader
							.loadObject(reporteSrc);

					JasperPrint jasperPrint = JasperFillManager.fillReport(
							jasperReport, parametro,
							new JRBeanCollectionDataSource(
									tegsTematicasComparativo));

					JasperViewer.viewReport(jasperPrint, false);
				} else {
					Messagebox
							.show("No hay informacion disponible para esta seleccion",
									"Informacion", Messagebox.OK,
									Messagebox.INFORMATION);

				}

			}

		}
	}

	/**
	 * Metodo que permite cargar las areas dado al programa seleccionado, donde
	 * si selecciona la opcion de "Todos", automaticamente se seteara ese mismo
	 * valor en el campo area, ademas se adiciona un nuevo item donde se puede
	 * seleccionar la opcion de "Todos" en el combo de las areas.
	 */
	@Listen("onSelect = #cmbPrograma")
	public void seleccionarPrograma() throws JRException {
		String idPrograma = cmbPrograma.getSelectedItem().getId();
		String nombrePrograma = cmbPrograma.getValue();
		cmbArea.setDisabled(false);
		if (nombrePrograma.equals("Todos")) {
			areas = servicioArea.buscarActivos();
			cmbArea.setValue("");
			
			
		} else {
			cmbArea.setValue("");
			programa = servicioPrograma.buscar(Long.parseLong(idPrograma));
			areas = servicioProgramaArea.buscarAreasDePrograma(programa);
		}
		AreaInvestigacion areaInvestigacion = new AreaInvestigacion(1000000,
				"Todos", "", true);
		areas.add(areaInvestigacion);
		cmbArea.setModel(new ListModelList<AreaInvestigacion>(areas));

	}

	/**
	 * Metodo que permite dado a la seleccion de la "Etapa del Teg", cargar en
	 * dos variables los estatus que seran enviados por parametro
	 * posteriormente.
	 */
	@Listen("onSelect = #cmbEtapaTeg")
	public void seleccionarEtapaTeg() throws JRException {
		List<String> listaComboEstatus = new ArrayList();
		String etapaNombre = cmbEtapaTeg.getValue();
		if (etapaNombre.equals("Proyecto")) {
			estatusProyectoTeg1 = "Proyecto Factible";
			estatusProyectoTeg2 = "Proyecto No Factible";
		}
		if (etapaNombre.equals("Teg")) {
			estatusProyectoTeg1 = "TEG Aprobado";
			estatusProyectoTeg2 = "TEG Reprobado";
		}
	}

	/**
	 * Metodo que permite dado a la seleccion del area de investigacion, para
	 * activar posteriormente el combo de etapa del teg
	 */
	@Listen("onSelect = #cmbArea")
	public void seleccionarArea() throws JRException {

		cmbEtapaTeg.setDisabled(false);
	}

	/** Metodo que permite limpiar los campos de los filtros de busqueda. */
	@Listen("onClick = #btnCancelarTematicasSolicitadas")
	public void cancelarTematicasSolicitadas() throws JRException {

		cmbPrograma.setValue("");
		cmbArea.setValue("");
		cmbArea.setDisabled(true);
		cmbEtapaTeg.setValue("");
		dtbFechaInicio.setValue(new Date());
		dtbFechaFin.setValue(new Date());
		
		cmbEtapaTeg.setDisabled(true);

	}

	/** Metodo que permite cerrar la vista. */
	@Listen("onClick = #btnSalirTematicasSolicitadas")
	public void salirTematicasSolicitadas() throws JRException {

		cancelarTematicasSolicitadas();
		wdwReporteTematicasMasSolicitadas.onClose();
	}

}
