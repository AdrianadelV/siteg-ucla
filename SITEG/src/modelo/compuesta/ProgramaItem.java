package modelo.compuesta;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import modelo.ItemEvaluacion;
import modelo.Lapso;
import modelo.Programa;
import modelo.compuesta.id.ProgramaItemId;

@Entity
@Table(name = "programa_item")
@IdClass(ProgramaItemId.class)
public class ProgramaItem {

	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "programa_id", referencedColumnName = "id")
	private Programa programa;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "item_evaluacion_id", referencedColumnName = "id")
	private ItemEvaluacion item;
	
	@Id
	@ManyToOne(optional = false)
	@JoinColumn(name = "lapso_id", referencedColumnName = "id")
	private Lapso lapso;


	public ProgramaItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProgramaItem(Programa programa, ItemEvaluacion item, Lapso lapso) {
		super();
		this.programa = programa;
		this.item = item;
		this.lapso = lapso;
	}

	public Programa getPrograma() {
		return programa;
	}

	public void setPrograma(Programa programa) {
		this.programa = programa;
	}

	public ItemEvaluacion getItem() {
		return item;
	}

	public void setItem(ItemEvaluacion item) {
		this.item = item;
	}

	public Lapso getLapso() {
		return lapso;
	}

	public void setLapso(Lapso lapso) {
		this.lapso = lapso;
	}
}
