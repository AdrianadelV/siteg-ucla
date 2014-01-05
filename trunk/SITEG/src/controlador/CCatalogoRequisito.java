package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.Actividad;
import modelo.Estudiante;
import modelo.Requisito;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import servicio.SRequisito;

import configuracion.GeneradorBeans;

@Controller
public class CCatalogoRequisito extends CGeneral {

	SRequisito servicioRequisito = GeneradorBeans.getServicioRequisito();

	@Wire
	private Listbox ltbRequisito;
	@Wire
	private Textbox txtNombreMostrarRequisito;
	@Wire
	private Textbox txtDescripcionMostrarRequisito;
	@Wire
	private Textbox txtNombreRequisito;
	@Wire
	private Textbox txtDescripcionRequisito;
	@Wire
	private Button btnEliminarRequisito;
	private long id = 0;
	@Wire
	private Window wdwCatalogoRequisito;

	private static String vistaRecibida;

	/**
	 * Metodo para inicializar componentes al momento que se ejecuta las vistas
	 * tanto VActividad como VCatalogoActividad
	 * 
	 * @date 09-12-2013
	 */
	void inicializar(Component comp) {

		/*
		 * Listado de todos las actividades que se encuentran activos, cuyo
		 * estatus=true con el servicioActividad mediante el metodo
		 * buscarActivos
		 */

		List<Requisito> requisito = servicioRequisito.buscarActivos();

		/*
		 * Validacion para mostrar el listado de actividades mediante el
		 * componente ltbActividad dependiendo si se encuentra ejecutando la
		 * vista VCatalogoActividad
		 */
		if (txtNombreRequisito == null) {
			ltbRequisito.setModel(new ListModelList<Requisito>(requisito));
		}

		Selectors.wireComponents(comp, this, false);

		/*
		 * Permite retornar el valor asignado previamente guardado al
		 * seleccionar un item de la vista VActividad
		 */

		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("requisitoCatalogo");
		/*
		 * Validacion para vaciar la informacion del VActividad a la vista
		 * VActividad.zul si la varible map tiene algun dato contenido
		 */
		if (map != null) {
			if (map.get("id") != null) {

				long codigo = (Long) map.get("id");
				Requisito requisito2 = servicioRequisito
						.buscarRequisito(codigo);
				txtNombreRequisito.setValue(requisito2.getNombre());
				txtDescripcionRequisito.setValue(requisito2.getDescripcion());
				id = requisito2.getId();
				map.clear();
				map = null;
				
				
			}
		}
	}

	/**
	 * Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	 * descripcion
	 * 
	 * @date 09-12-2013
	 */

	/**
	 * Aca se selecciona una actividad del catalogo
	 * 
	 * @date 09-12-2013
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;
	}

	// Aca se filtran las busqueda en el catalogo, ya sea por nombre o por
	// descripcion
	@Listen("onChange = #txtNombreMostrarRequisito,#txtDescripcionMostrarRequisito")
	public void filtrarDatosCatalogo() {
		List<Requisito> requisito1 = servicioRequisito.buscarActivos();
		List<Requisito> requisito2 = new ArrayList<Requisito>();

		for (Requisito requisito : requisito1) {
			if (requisito
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarRequisito.getValue().toLowerCase())
					&& requisito
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarRequisito.getValue()
											.toLowerCase())) {
				requisito2.add(requisito);
			}
		}

		ltbRequisito.setModel(new ListModelList<Requisito>(requisito2));

	}

	// Aca se selecciona una actividad del catalogo
	@Listen("onDoubleClick = #ltbRequisito")
	public void mostrarDatosCatalogo() {
		
		if (vistaRecibida == null) {

			vistaRecibida = "maestros/VRequisito";

		} else {

		Listitem listItem = ltbRequisito.getSelectedItem();
		Requisito requisitoDatosCatalogo = (Requisito) listItem.getValue();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id", requisitoDatosCatalogo.getId());
		String vista = vistaRecibida;
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoRequisito.onClose();
		}
	}
	
	

	


}