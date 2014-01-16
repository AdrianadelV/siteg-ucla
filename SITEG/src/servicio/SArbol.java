package servicio;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



import modelo.Arbol;
import modelo.Grupo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import interfazdao.IArbolDAO;
@Service
public class SArbol {
@Autowired
private IArbolDAO interfaceArbol;

public void guardar(Arbol arbol){
	interfaceArbol.save(arbol);
}
public Arbol buscar(long id){
	
	return interfaceArbol.findOne(id);
}
public List<Arbol> listarArbol(){
	return interfaceArbol.buscarTodos();
}

public Arbol buscarPorNombreArbol(String nombre){
	Arbol arbol;
	arbol= interfaceArbol.findByNombre(nombre);
	return arbol;
}
public List<Arbol> ordenarPorID(ArrayList<Long> ids) {
	// TODO Auto-generated method stub
	List<Arbol> arboles;
	arboles = interfaceArbol.buscar(ids);
	return arboles;
	
}
public Arbol buscarPorId(Long id) {
	// TODO Auto-generated method stub
	Arbol arbol;
	arbol = interfaceArbol.findOne(id);
	return arbol;
}
/*public List<Arbol> buscarGrupo(long id){
	List<Arbol> arboles;
	arboles=interfaceArbol.buscarPorGrupo(id);
	return arboles;
}*/
public List<Arbol> buscarporGrupo(Grupo grupo){
	List<Arbol> arboles;
	arboles=interfaceArbol.findByGrupos(grupo);
	return arboles;
}
}