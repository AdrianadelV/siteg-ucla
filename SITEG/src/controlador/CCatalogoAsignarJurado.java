package controlador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import modelo.SolicitudTutoria;
import modelo.Teg;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SPrograma;
import servicio.STeg;

@Controller
public class CCatalogoAsignarJurado extends CGeneral {

	STeg servicioTeg = GeneradorBeans.getServicioTeg();
	SPrograma servicioPrograma = GeneradorBeans.getServicioPrograma();
	CAsignarJurado vista = new CAsignarJurado();
	
	@Wire
	private Textbox txtFechaSolicitudDefensa;
	@Wire
	private Textbox txtAreaSolicitudDefensa;
	@Wire
	private Textbox txtTematicaSolicitudDefensa;
	@Wire
	private Textbox txtTituloSolicitudDefensa;
	@Wire
	private Textbox txtNombreTutorSolicitudDefensa;
	@Wire
	private Textbox txtApellidoTutorSolicitudDefensa;
	@Wire
	private Listbox ltbSolicitudesDefensa;
	private List<Teg> tegsDefensa1 = new ArrayList<Teg>();
	private List<Teg> tegsDefensa = new ArrayList<Teg>();
	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		HashMap<String, Object> map = (HashMap<String, Object>) Sessions
				.getCurrent().getAttribute("itemsCatalogo");
		if(map != null || map==null){
			//Metodo que permite cargar los tegs en el listbox median utilizacion de servicios
			tegsDefensa1 = servicioTeg.buscarTegPorProgramaParaDefensa(servicioPrograma.buscarProgramaDeDirector(ObtenerUsuarioProfesor()));
			if(!tegsDefensa1.isEmpty()) {
	
				tegsDefensa.add(tegsDefensa1.get(0));
			for(int i =1; i<tegsDefensa1.size();i++){
				System.out.println("id"+tegsDefensa1.get(i).getId());
				long temp = tegsDefensa1.get(i-1).getId();
				System.out.println("temporal"+temp);
				if(temp !=tegsDefensa1.get(i).getId()){
					tegsDefensa.add(tegsDefensa1.get(i));
				}
				
			}
			System.out.println(tegsDefensa.toString());
			ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegsDefensa));
			}
		}
	}
	//Metodo que permite el fitrado de datos
	@Listen("onChange = #txtAreaSolicitudDefensa,#txtTematicaSolicitudDefensa,#txtTituloSolicitudDefensa,#txtNombreTutorSolicitudDefensa,#txtApellidoTutorSolicitudDefensa")
	public void filtrarCatalogo(){
		
		List<Teg> tegs = new ArrayList<Teg>();

		for (Teg teg : tegsDefensa) {
			if (teg.getTematica().getareaInvestigacion().getNombre()
					.toLowerCase()
					.contains(txtAreaSolicitudDefensa.getValue().toLowerCase())
					&& teg
							.getTematica()
							.getNombre()
							.toLowerCase()
							.contains(
									txtTematicaSolicitudDefensa.getValue()
											.toLowerCase())
					&& teg
							.getTitulo()
							.toLowerCase()
							.contains(txtTituloSolicitudDefensa.getValue())
					&& teg
							.getTutor()
							.getNombre()
							.toLowerCase()
							.contains(txtNombreTutorSolicitudDefensa.getValue())
					&& teg
							.getTutor()
							.getApellido()
							.toLowerCase()
							.contains(txtApellidoTutorSolicitudDefensa.getValue())) {
				tegs.add(teg);
			}
		}

		ltbSolicitudesDefensa.setModel(new ListModelList<Teg>(tegs));
	}
	//Metodo para seleccionar un Teg haciendo doble clic a las lista
	@Listen("onDoubleClick = #ltbSolicitudesDefensa")
	public void seleccionarLista(){
		if(ltbSolicitudesDefensa.getItemCount()!= 0){
		Listitem listItem = ltbSolicitudesDefensa.getSelectedItem();
		Teg tegSeleccionado = (Teg)listItem.getValue();
		long id = tegSeleccionado.getId();
		final HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("id",id);
		Sessions.getCurrent().setAttribute("catalogoSolicitudDefensa", map);
		Window window = (Window) Executions.createComponents(
				"/vistas/transacciones/VAsignarJurado.zul", null, null);	 				
		window.doModal();
		vista.recibir("catalogos/VCatalogoAsignarJurado");
		}
	}
}
