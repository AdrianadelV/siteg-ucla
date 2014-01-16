package controlador;

import java.util.List;

import modelo.Cronograma;
import modelo.Estudiante;
import modelo.Lapso;
import modelo.Programa;
import configuracion.GeneradorBeans;
import controlador.CConsultarEstatus;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.zkoss.spring.security.SecurityUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.lowagie.text.Image;

import servicio.SCronograma;
import servicio.SEstudiante;
import servicio.SLapso;
import servicio.SPrograma;

@Controller
public class CInicio extends CGeneral {
	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	SLapso servicioLapso = GeneradorBeans.getServicioLapso();
	SCronograma servicioCronograma = GeneradorBeans.getServicioCronograma();
	@Wire
	private Treeitem fila3;
	@Wire
	private Treecell fila211;
	@Wire
	private Treecell fila2;
	@Wire
	private Treecell fila11;
	@Wire
	private Treecell fila1;
	@Wire
	private Treecell fila12;
	@Wire
	private Treecell fila112;
	@Wire
	private Treecell fila111;
	@Wire
	private Intbox cedulaEstatus;
	@Wire
	private Window wdwConsultarEstatusProyecto;
	@Wire
	private Window wdwCatalogoArchivo;
	@Wire
	private Button btnSolicitarTutor;
	@Wire
	private Button btnBiblioteca;
	@Wire
	private Combobox cmbPrograma;
	@Wire
	private Listbox ltbCronograma;
	@Wire
	private Window wdwCrono;
	@Wire
	private Label lblOlvidoClave;
	public static long idPrograma;

	@Override
	void inicializar(Component com) {
		// TODO Auto-generated method stub
		List<Programa> programa = servicioPrograma.buscarActivas();
		if (cmbPrograma != null) {
			cmbPrograma.setModel(new ListModelList<Programa>(programa));

		}
		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa1 = servicioPrograma.buscar(idPrograma);

	}

	@Listen("onSelect = #cmbPrograma")
	public void llenarCronograma() {
		idPrograma = Long.parseLong(cmbPrograma.getSelectedItem().getId());
		System.out.println(idPrograma);
		Lapso lapso = servicioLapso.buscarLapsoVigente();
		Programa programa = servicioPrograma.buscar(idPrograma);
		List<Cronograma> cronograma = servicioCronograma
				.buscarCronogramaPorLapsoYPrograma(programa, lapso);
		ltbCronograma.setModel(new ListModelList<Cronograma>(cronograma));
		wdwCrono.setVisible(false);

	}

	@Listen("onClick = #btnSolicitarTutor")
	public void SolicitarTutor() {
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VSolicitarTutor.zul", null, null);
		window.doModal();
	}
	@Listen("onClick = #lblOlvidoClave")
	public void reiniciarClave() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VReinicioClave.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #mitQuienesSomos")
	public void QuienesSomos() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VQuienesSomos.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #mitContactanos")
	public void contactanos() {
		Window window = (Window) Executions.createComponents(
				"/vistas/portal-web/VContactanos.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #btnBiblioteca")
	public void BibliotecaVirtual() {
		Window window = (Window) Executions.createComponents(
				"/vistas/catalogos/VCatalogoArchivo.zul", null, null);
		window.doModal();
	}

	@Listen("onClick = #btnDatos")
	public void datos() {
		// Agarrar datos del usuario
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		System.out.println(userDetails.toString());
	}

	@Listen("onClick = #btnInformacionInteres")
	public void InformacionInteres(Event e) {
		Window wdwInformacionInteres = (Window) Executions.createComponents(
				"/vistas/portal-web/VInformacionInteres.zul", null, null);
		wdwInformacionInteres.doModal();
		CCatalogoArchivoDescarga cata = new controlador.CCatalogoArchivoDescarga();
		cata.metodoPrender();
	}

	@Listen("onClick = #btnConsultarEstatus")
	public void ventanaEmergente(Event e) {
		String cedula = Integer.toString(cedulaEstatus.getValue());

		if (cedula != "") {
			if (servicioEstudiante.buscarEstudiante(cedula) != null) {

				cedulaEstatus.setValue(null);
				CConsultarEstatus consultarestatus = new CConsultarEstatus();
				consultarestatus.recibirCedula(cedula);
			} else {

				Messagebox
						.show("Estudiante no autorizado para realizar un Trabajo Especial de Grado",
								"Información", Messagebox.OK,
								Messagebox.INFORMATION);
			
			}

		}

	}

}
