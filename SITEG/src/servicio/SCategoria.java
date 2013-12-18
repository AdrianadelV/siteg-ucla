package servicio;

import java.util.List;

import interfazdao.ICategoriaDAO;

import modelo.Categoria;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SCategoria {

	@Autowired
	private ICategoriaDAO interfazCategoria;

	public Categoria buscarCategoriaPorNombre(String categorias) {
		// TODO Auto-generated method stub
		Categoria categoria;
		categoria = interfazCategoria.findByNombreAndEstatusTrue(categorias);
		return categoria;
	}

	public List<Categoria> buscarCategoria() {
		// TODO Auto-generated method stub
		List<Categoria> categorias;
		categorias = interfazCategoria.findByEstatusTrue();
		return categorias;
	}

	public Categoria buscarPorId(long idcategoria) {
		// TODO Auto-generated method stub
		Categoria categoria;
		categoria = interfazCategoria.findOne(idcategoria);
		return categoria;
	}
}