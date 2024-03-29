package controlador.catalogo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.filechooser.FileSystemView;

import modelo.Estudiante;
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
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/**
 * Controlador asociado a la vista catalogo estudiante que permite mostrar los
 * estudiantes disponibles a traves de un listado
 */
@Controller
public class CCatalogoEstudiante extends CGeneral {

	List<Estudiante> estudiantes = new ArrayList();
	private static String vistaRecibida;
	private static boolean variable = false;
	public static List<Estudiante> estudiantes1;

	@Wire
	private Listbox ltbEstudiante;
	@Wire
	private Window wdwCatalogoEstudiante;
	@Wire
	private Textbox txtCedulaMostrarEstudiante;
	@Wire
	private Textbox txtNombreMostrarEstudiante;
	@Wire
	private Textbox txtApellidoMostrarEstudiante;
	@Wire
	private Textbox txtCorreoMostrarEstudiante;
	@Wire
	private Textbox txtProgramaMostrarEstudiante;

	/**
	 * Metodo heredado del Controlador CGeneral donde se verifica que el map
	 * recibido del catalogo exista y se llenan las listas correspondientes de
	 * la vista dado una condicional, que si se cumple se mostrara los
	 * estudiantes sin usuarios sino todos los estudiantes activos.
	 */
	@Override
	public void inicializar(Component comp) {
		// TODO Auto-generated method stub

		List<Estudiante> estudiantes = servicioEstudiante.buscarActivos();
		ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes));

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");

		if (map != null) {
			if (map.get("usuario") != null) {
				variable = true;
				List<Estudiante> estudiantes1 = servicioEstudiante
						.buscarEstudianteSinUsuario();
				ltbEstudiante.setModel(new ListModelList<Estudiante>(
						estudiantes1));

			} else {
				List<Estudiante> estudiantes1 = servicioEstudiante
						.buscarActivos();
				ltbEstudiante.setModel(new ListModelList<Estudiante>(
						estudiantes1));

			}

			Selectors.wireComponents(comp, this, false);
		}

	}

	/**
	 * Metodo que permite filtrar los estudiantes disponibles, dado a la
	 * condicional de la variable booleana si es igual a "true" se mostraran los
	 * estudiantes sin usuario sino si es "false" seran todos los estudiantes
	 * activos, mediante el componente de la lista, donde se podra visualizar la
	 * cedula, nombre, apellido, correo y programa
	 */
	@Listen("onChange = #txtCedulaMostrarEstudiante,#txtNombreMostrarEstudiante,#txtApellidoMostrarEstudiante,#txtCorreoMostrarEstudiante,#txtProgramaMostrarEstudiante")
	public void filtrarDatosCatalogo() {

		if (variable == true) {
			estudiantes1 = servicioEstudiante.buscarEstudianteSinUsuario();

		} else {
			estudiantes1 = servicioEstudiante.buscarActivos();
		}
		List<Estudiante> estudiantes2 = new ArrayList<Estudiante>();

		for (Estudiante estudiante : estudiantes1) {
			if (estudiante
					.getCedula()
					.toLowerCase()
					.contains(
							txtCedulaMostrarEstudiante.getValue().toLowerCase())
					&& estudiante
							.getNombre()
							.toLowerCase()
							.contains(
									txtNombreMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getApellido()
							.toLowerCase()
							.contains(
									txtApellidoMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getCorreoElectronico()
							.toLowerCase()
							.contains(
									txtCorreoMostrarEstudiante.getValue()
											.toLowerCase())
					&& estudiante
							.getPrograma()
							.getNombre()
							.toLowerCase()
							.contains(
									txtProgramaMostrarEstudiante.getValue()
											.toLowerCase())) {
				estudiantes2.add(estudiante);
			}

		}

		ltbEstudiante.setModel(new ListModelList<Estudiante>(estudiantes2));

	}

	/**
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 * @param vista
	 *            nombre de la vista a la cual se hace referencia
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}

	/**
	 * Metodo que permite obtener el objeto Estudiante al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su
	 * cedula, para luego poder ser mapeada y enviada a la vista asociada a
	 * ella.
	 */
	@Listen("onDoubleClick = #ltbEstudiante")
	public void mostrarDatosCatalogo() {

		try {
			if (vistaRecibida == null) {

				vistaRecibida = "maestros/VEstudiante";

			} else {
				if (ltbEstudiante.getItemCount() != 0) {
					Listitem listItem = ltbEstudiante.getSelectedItem();
					Estudiante estudianteDatosCatalogo = (Estudiante) listItem
							.getValue();
					HashMap<String, Object> map = new HashMap<String, Object>();
					HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
							.getCurrent().getAttribute("itemsCatalogo");

					if (map2 != null)
						map = map2;

					map.put("cedula", estudianteDatosCatalogo.getCedula());
					String vista = vistaRecibida;
					map.put("vista", vista);
					Sessions.getCurrent().setAttribute("itemsCatalogo", map);
					Executions.sendRedirect("/vistas/arbol.zul");
					wdwCatalogoEstudiante.onClose();
				}
			}
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Metodo que permite generar una lista de los estudiantes que se encuentran
	 * activos en el sistema, agrupados por programa mediante el componente
	 * "Jasperreport"
	 */

	@Listen("onClick = #btnImprimir")
	public void imprimir() throws SQLException {
		FileSystemView filesys = FileSystemView.getFileSystemView();
		List<Estudiante> estudiantes = servicioEstudiante.buscarActivos();

		if (estudiantes.size() != 0) {

			JasperReport jasperReport;
			try {
				String rutaUrl = obtenerDirectorio();
				String reporteSrc = rutaUrl
						+ "SITEG/vistas/reportes/salidas/compilados/REstudiante.jasper";
				String reporteImage = rutaUrl
						+ "SITEG/public/imagenes/reportes/";
				Map p = new HashMap();
				p.put("logoUcla", reporteImage + "logo ucla.png");
				p.put("logoCE", reporteImage + "logo CE.png");
				p.put("logoSiteg", reporteImage + "logo.png");

				jasperReport = (JasperReport) JRLoader.loadObject(reporteSrc);
				JasperPrint jasperPrint = JasperFillManager.fillReport(
						jasperReport, p, new JRBeanCollectionDataSource(
								estudiantes));
				JasperViewer.viewReport(jasperPrint, false);

			} catch (JRException e) {
				System.out.println(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Messagebox.show("No hay informacion disponible", "Informacion",
					Messagebox.OK, Messagebox.INFORMATION);
		}

	}

	/** Metodo que permite cerrar la ventana correspondiente al Catalogo */
	@Listen("onClick = #btnSalirCatalogoEstudiante")
	public void salirCatalogoEstudiante() {
		wdwCatalogoEstudiante.onClose();
	}

}
