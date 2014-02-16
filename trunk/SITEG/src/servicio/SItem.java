package servicio;

import java.util.List;


import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import interfazdao.IItemDAO;

@Service
public class SItem {
@Autowired
private IItemDAO interfaceItem;

public void guardar(ItemEvaluacion item){
	interfaceItem.save(item);
}

public List<ItemEvaluacion> buscarItemsActivos(){
	List<ItemEvaluacion> items;
	items= interfaceItem.findByEstatusTrueOrdenByNombreAndTipoAsc();
	return items;
}
public ItemEvaluacion buscarItem(long codigo){
	return interfaceItem.findById(codigo);
}

public List<ItemEvaluacion> buscarItemsDisponibles(Programa programa,
		Lapso lapso) {
	// TODO Auto-generated method stub
	List<ItemEvaluacion> items;
	items = interfaceItem.buscarDisponibles(programa, lapso);
	return items;
}

}

