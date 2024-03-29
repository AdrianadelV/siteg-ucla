package controlador.catalogo;

import java.util.ArrayList;
import java.util.List;

import modelo.AreaInvestigacion;
import modelo.Tematica;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import controlador.CGeneral;

/**
 * Controlador asociado a la vista catalogo tematica area que permite mostrar
 * las tematicas disponibles de acuerdo a un area seleccionada, a traves de un
 * listado
 */
@Controller
public class CCatalogoTematicaArea extends CGeneral {

	private static AreaInvestigacion area;
	private long id = 0;

	@Wire
	private Listbox ltbTematica;
	@Wire
	private Textbox txtNombreMostrarTematica;
	@Wire
	private Textbox txtDescripcionMostrarTematica;
	@Wire
	private Window wdwCatalogoTematica;

	/**
	 * Metodo heredado del Controlador CGeneral donde se buscan todas las
	 * tematicas por area disponibles y se llena el listado del mismo en el
	 * componente lista de la vista.
	 */
	public void inicializar(Component comp) {

		List<Tematica> tematica = servicioTematica.buscarTematicasDeArea(area);
		ltbTematica.setModel(new ListModelList<Tematica>(tematica));

	}

	/**
	 * Metodo que permite recibir un area de investigacion
	 * 
	 * @param a
	 *            objeto de area de investigacion
	 */
	public void recibirId(AreaInvestigacion a) {
		area = a;
	}

	/**
	 * Metodo que permite filtrar las tematicas disponibles, mediante el
	 * componente de la lista, donde se podra visualizar el nombre, area y
	 * descripcion de estas.
	 */
	@Listen("onChange = #txtNombreMostrarTematica,#txtAreaMostrarTematica,#txtDescripcionMostrarTematica")
	public void filtrarDatosCatalogo() {
		List<Tematica> tematicas1 = servicioTematica.buscarActivos();
		List<Tematica> tematicas2 = new ArrayList<Tematica>();

		for (Tematica tematica : tematicas1) {
			if (tematica
					.getNombre()
					.toLowerCase()
					.contains(txtNombreMostrarTematica.getValue().toLowerCase())
					&& tematica
							.getDescripcion()
							.toLowerCase()
							.contains(
									txtDescripcionMostrarTematica.getValue()
											.toLowerCase())) {
				tematicas2.add(tematica);
			}

		}

		ltbTematica.setModel(new ListModelList<Tematica>(tematicas2));

	}

}
