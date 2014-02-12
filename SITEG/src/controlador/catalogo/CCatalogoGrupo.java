package controlador.catalogo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import modelo.ItemEvaluacion;
import modelo.seguridad.Grupo;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import servicio.SItem;
import servicio.seguridad.SGrupo;
import configuracion.GeneradorBeans;
import controlador.CGeneral;

public class CCatalogoGrupo extends CGeneral {

	private static String vistaRecibida;

	@Wire
	private Textbox txtNombreMostrarGrupo;
	@Wire
	private Listbox ltbGrupo;
	@Wire
	private Window wdwCatalogoGrupo;

	/*
	 * Metodo heredado del Controlador CGeneral donde se buscan todos los
	 * grupos disponibles y se llena el listado del mismo en el componente
	 * lista de la vista.
	 */
	@Override
	public void inicializar(Component comp) {
		List<Grupo> grupos = servicioGrupo.buscarActivos();
		ltbGrupo.setModel(new ListModelList<Grupo>(grupos));
	
	}
	
	/*
	 * Metodo que permite filtrar los grupos disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre estos.
	 */
	@Listen("onChange = #txtNombreMostarGrupo")
	public void filtrarDatosCatalogo() {
		List<Grupo> grupos = servicioGrupo.buscarActivos();
		List<Grupo> grupos1 = servicioGrupo.buscarActivos();
	
		for (Grupo grupo : grupos) {
			if (grupo
					.getNombre()
					.toLowerCase()
					.contains(
							txtNombreMostrarGrupo.getValue().toLowerCase())
					)
			{
				grupos1.add(grupo);
			}

		}

		ltbGrupo.setModel(new ListModelList<Grupo>(grupos1));

	}
	/*
	 * Metodo que permite recibir el nombre de la vista a la cual esta asociado
	 * este catalogo para poder redireccionar al mismo luego de realizar la
	 * operacion correspondiente a este.
	 */
	public void recibir(String vista) {
		vistaRecibida = vista;

	}
	/*
	 * Metodo que permite obtener el objeto Grupo al realizar el evento
	 * doble clic sobre un item en especifico en la lista, extrayendo asi su id,
	 * para luego poder ser mapeada y enviada a la vista asociada a ella.
	 */
	@Listen("onDoubleClick = #ltbGrupo")
	public void mostrarDatosCatalogo() {
		if(ltbGrupo.getItemCount()!=0){
		Listitem listItem = ltbGrupo.getSelectedItem();
		Grupo grupo = (Grupo) listItem.getValue();
		Grupo grupo1=servicioGrupo.BuscarPorNombre(grupo.getNombre());
		String vista = vistaRecibida;
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if(map2!=null)
			map=map2;
		map.put("id", grupo1.getId());
		map.put("vista", vista);
		Sessions.getCurrent().setAttribute("itemsCatalogo", map);
		Executions.sendRedirect("/vistas/arbol.zul");
		wdwCatalogoGrupo.onClose();

	}
	}
}
