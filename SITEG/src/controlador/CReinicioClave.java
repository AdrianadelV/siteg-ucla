package controlador;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import modelo.Estudiante;
import modelo.Profesor;
import modelo.Usuario;

import org.springframework.stereotype.Controller;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import configuracion.GeneradorBeans;

import servicio.SEstudiante;
import servicio.SProfesor;
import servicio.SUsuario;

@Controller
public class CReinicioClave extends CGeneral {

	SEstudiante servicioEstudiante = GeneradorBeans.getServicioEstudiante();
	SProfesor servicioProfesor = GeneradorBeans.getServicioProfesor();
	SUsuario servicioUsuario = GeneradorBeans.getServicioUsuario();

	@Wire
	private Window wdwReinicioClave;
	@Wire
	private Textbox txtNombreUsuario;
	@Wire
	private Textbox txtCorreoUsuario;
	@Wire
	private Label lblNombreUsuario;
	@Wire
	private Checkbox cbxUsuario;
	@Wire
	private Checkbox cbxClave;

	ArrayList<Boolean> valorCorreo = new ArrayList<Boolean>();
	
	private String mensaje = "Su solicitud ha sido exitosamente procesada, le enviamos su usuario y contraseņa,";

	@Override
	void inicializar(Component comp) {
		// TODO Auto-generated method stub
		
	}
	@Listen("onClick = #btnEnviarCorreo")
	public void reiniciarClave() {
    
		if(txtNombreUsuario.getText().compareTo("") != 0 && txtCorreoUsuario.getText().compareTo("") != 0){
			
			String nombreUsuario=txtNombreUsuario.getValue();
			String correoUsuario=txtCorreoUsuario.getValue();
			Usuario usuario=servicioUsuario.buscarUsuarioPorNombre(txtNombreUsuario.getValue());
		//	int claveUsuario = ((int) Math.random() * 100);
			
			valorCorreo.add(enviarEmailNotificacion(correoUsuario, mensaje+" Usuario: "+nombreUsuario+"  "+"Contraseņa: "+nombreUsuario));
			confirmacion(valorCorreo);

			wdwReinicioClave.onClose();
		}
}
	private int confirmacion(ArrayList<Boolean> valor2) {
		// TODO Auto-generated method stub
		for(int w=0; w<valor2.size();w++){
			if(valor2.get(w).equals(false)){
				return Messagebox.show("Correo electronico no enviado", "Error", Messagebox.OK, Messagebox.ERROR);
			}
		}
		return Messagebox.show("Correo electronico enviado","Informacion", Messagebox.OK,Messagebox.INFORMATION); 
	}
	@Listen("onClick = #btnCancelarCorreo")
	public void cancelarSolicitud() {
		
		txtNombreUsuario.setValue("");
		txtCorreoUsuario.setValue("");
		
	}
    
	
}
