package servicio;

import java.util.Date;
import java.util.List;
import interfazdao.IEstudianteDAO;
import interfazdao.ITegDAO;
import modelo.AreaInvestigacion;
import modelo.Estudiante;
import modelo.Profesor;
import modelo.Programa;
import modelo.Teg;
import modelo.Tematica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class STeg {
	@Autowired
	private ITegDAO interfaceTeg;
	@Autowired
	private IEstudianteDAO interfaceEstudiante;

	private String[] estatus = { "Solicitando Registro", "Proyecto Registrado",
			"Comision Asignada", "Factibilidad Evaluada", "Proyecto Factible",
			"Proyecto No Factible", "Avances Finalizados", "TEG Registrado",
			"Revisiones Finalizadas", "Solicitando Defensa",
			"Defensa Asignada", "TEG Aprobado", "TEG Reprobado",
			"Jurado Asignado", "Proyecto en Desarrollo",
			"Trabajo en Desarrollo" };

	public Teg buscarTeg(long id) {
		return interfaceTeg.findOne(id);

	}
	/* Buscar los teg que tengan estatus Aprobado/Reprobado */
	public List<Teg> buscarSegunDosEstatus(String estatus1,String estatus2) {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus1, estatus2);
		return tegs;

	}
	public List<Teg> BuscarTegCalificandoDefensa() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[10]);
		return tegs;

	}

	public List<Teg> buscarTegs(String estatus) {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus);
		return tegs;

	}

	// Evaluar Revisiones
	public List<Teg> buscarTegRegistrado() {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus[7], estatus[15]);
		return tegs;

	}

	public List<Teg> buscarTegFactibilidad() {
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus[4], estatus[5]);
		return tegs;

	}
	
	public List<Teg> buscarActivos() {
		List<Teg> tegs;
		tegs = interfaceTeg.findAll();
		return tegs;
	}

	public void guardar(Teg objetoTeg1) {
		// TODO Auto-generated method stub
		interfaceTeg.save(objetoTeg1);
	}

	/* Buscar los teg que tengan estatus Proyecto Registrado */
	public List<Teg> BuscarProyectoRegistrado() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[1]);
		return tegs;

	}

	public List<Teg> buscarProyectoAsignado() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[2]);
		return tegs;

	}

	public List<Teg> BuscarTegSolicitandoRegistro() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[0]);
		return tegs;

	}

	/* Busca los teg asociados al Profesor */
	public List<Teg> buscarTutoriaProfesor(Profesor profesor) {
		List<Teg> teg;
		teg = interfaceTeg.findByTutor(profesor);
		return teg;
	}

	/* Busca los teg asociados al Estudiante */
	public List<Teg> buscarTegPorEstudiante(Estudiante estudiante) {
		// TODO Auto-generated method stub
		List<Teg> teg;
		teg = interfaceTeg.findByEstudiantes(estudiante);
		return teg;
	}
	public List<Teg> buscarTegPorProfesor(String estatus1,String estatus2, Profesor profesor)
	{
		List<Teg> teg;
		teg = interfaceTeg.findByEstatusOrEstatusLikeAndTutor(estatus1,estatus2,
				profesor);
		return teg;
		
	}

	/*
	 * Busca un teg asociado a un estudiante que tengan estatus avances
	 * finalizados
	 */
	public Teg buscarTegEstudiantePorEstatus(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusLikeAndEstudiantes(estatus[6],
				estudiante);
		return teg;
	}

	
	/* Busca un teg asociado a un estudiante que tenga estatus teg aprobado */
	public Teg buscarTegEstudiantePorEstatusAprobado(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusLikeAndEstudiantes(estatus[11],
				estudiante);
		return teg;
	}
	

	/* Buscar los teg que tengan estatus Proyecto Registrado */
	public List<Teg> buscarProyectoFactible() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatus(estatus[4], estatus[14]);
		return tegs;

	}

	/* Buscar los teg que tengan estatus Factibilidad Evaluada */
	public List<Teg> buscarProyectoFactibilidadEvaluada() {

		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatus(estatus[3]);
		return tegs;

	}

	/* Buscar ultimo teg registrado */
	public Teg buscarUltimoTeg() {
		Teg ultimoTeg;
		ultimoTeg = interfaceTeg.findOne(interfaceTeg.ultimoTegRegistrado());
		return ultimoTeg;
	}

	public Teg buscarTegConRevisionFinal(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusAndEstudiantes(estatus[8], estudiante);
		return teg;
	}

	public Teg buscarTegSolicitandoDefensa(Estudiante estudiante) {
		// TODO Auto-generated method stub
		Teg teg;
		teg = interfaceTeg.findByEstatusAndEstudiantes(estatus[9], estudiante);
		return teg;
	}

	public List<Teg> buscarTegPorProgramaParaDefensa(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[9], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}
	
	/* Buscar los teg que tengan estatus Factibilidad Evaluada de acuerdo a un programa */

	public List<Teg> buscarTegPorProgramaParaRegistrarFactibilidad(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[3], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}
	
	/* Buscar los teg que tengan estatus Solicitando Registro de acuerdo a un programa */

	public List<Teg> buscarTegPorProgramaParaRegistrarTeg(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[0], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	/* Buscar los teg que tengan estatus Proyecto Registrado de acuerdo a un programa */

	public List<Teg> buscarTegPorProgramaParaAsignarComision(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[1], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}
	
	/* Buscar los teg que tengan estatus Jurado Asignado de acuerdo a un programa */

	public List<Teg> buscarTegPorProgramaParaDefensa2(Programa programa) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusAndEstudiantesInOrderByIdAsc(
				estatus[13], interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}

	public List<Teg> buscarTegDeComision(Profesor obtenerUsuarioProfesor) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByProfesores(obtenerUsuarioProfesor);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorDosFechasYTematica(
			Profesor buscarProfesorPorCedula, Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndTematicaAndFechaBetweenOrderByFechaAsc(
				buscarProfesorPorCedula, tematica, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorTematicaPorDosFechasYEstatus(
			Profesor profesor, Tematica tematica, String estatus2,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndTematicaAndEstatusAndFechaBetweenOrderByFechaAsc(
				profesor, tematica, estatus2, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarTodosTegsDeTutorPorDosFechas(Profesor profesor,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndFechaBetweenOrderByFechaAsc(profesor, fechaInicio,
				fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegsDeTutorPorDosFechasYEstatus(Profesor profesor,
			String estatus2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByTutorAndEstatusAndFechaBetweenOrderByFechaAsc(profesor,
				estatus2, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarUltimasTematicas(String estatus,
			AreaInvestigacion area, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.buscarUltimasTematicas(estatus, area, fechaInicio,
				fechaFin);
		return tegs;
	}
	
	public List<Teg> buscarTegPorProgramaUnEstatus(String estatus, Programa programa, Date fechaInicio, Date fechaFin){
		return interfaceTeg.buscarTegPorProgramaVariasAreasUnEstatus(estatus, programa, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegsSegunAreaUnEstatus(AreaInvestigacion area, Date fechaInicio, Date fechaFin,String estatus){
		List<Teg> tegs;
		tegs= interfaceTeg.buscarTegAreaestatus(area, fechaInicio, fechaFin,estatus);
		return tegs;
		
	}


	public List<Teg> buscarTegsDeTematicaPorDosFechas(Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.findByTematicaAndFechaBetween(tematica,
				fechaInicio, fechaFin);
	}

	public List<Long> buscarUltimasTematicasProgramaAreaEstatus(
			String estatusProyectoTeg1, String estatusProyectoTeg2,
			AreaInvestigacion area, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Long> tegs;
		tegs = interfaceTeg.buscarUltimasTematicasProgramaAreaEstatus(
				estatusProyectoTeg1, estatusProyectoTeg2, area, fechaInicio,
				fechaFin);
		return tegs;
	}

	public List<Teg> buscarUltimasTematicasProgramaEstatus(
			String estatus1,String estatus2,Programa programa,Date fecha1,Date fecha2) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.findByEstatusOrEstatusAndEstudiantesInAndFechaBetweenOrderByTematicaIdAsc(
				estatus1, estatus2, interfaceEstudiante.findByPrograma(programa),
				fecha1, fecha2);
		return tegs;
	}

	public List<Long> buscarUltimasEstatus(String estatusProyectoTeg1,
			String estatusProyectoTeg2, Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Long> tegs;
		tegs = interfaceTeg.buscarUltimasEstatus(estatusProyectoTeg1,
				estatusProyectoTeg2, fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarUltimasOrdenadasEstatus(String estatusProyectoTeg1,
			String estatusProyectoTeg2, List<Tematica> tematicas,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		List<Teg> tegs;
		tegs = interfaceTeg.buscarUltimasOrdenadasEstatus(estatusProyectoTeg1,
				estatusProyectoTeg2, tematicas, fechaInicio, fechaFin);
		return tegs;
	}

	public long contadorEstatus(String estatusProyectoTeg, Tematica tematica,
			Date fechaInicio, Date fechaFin) {
		// TODO Auto-generated method stub
		long tegs;
		tegs = interfaceTeg.countByEstatus(estatusProyectoTeg, tematica,
				fechaInicio, fechaFin);
		return tegs;
	}

	public List<Teg> buscarTegPorDosFechasyUnEstatus(
			String estatus, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFecha(estatus,fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyVariosEstatus(
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7,
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegporFechayVariosEstatus(estatus1, estatus2,
				estatus3, estatus4, estatus5, estatus6, estatus7, tematica,
				fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorProgramaVariasAreasVariosEstatus(
			String estatus1, String estatus2, String estatus3, String estatus4,
			String estatus5, String estatus6, String estatus7,
			Programa programa, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorProgramaVariasAreasVariosEstatus(
				estatus1, estatus2, estatus3, estatus4, estatus5, estatus6,
				estatus7, programa, fechaInicio, fechaFin);
	}

	public List<Teg> buscarTegPorVariosProgramasVariosEstatus(String estatus1,
			String estatus2, String estatus3, String estatus4, String estatus5,
			String estatus6, String estatus7, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosProgramasVariosEstatus(estatus1,
				estatus2, estatus3, estatus4, estatus5, estatus6, estatus7,
				fechaInicio, fechaFin);

	}

	public List<Teg> buscarTegsSegunArea(AreaInvestigacion area,
			Date fechaInicio, Date fechaFin) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegArea(area, fechaInicio, fechaFin);
		return tegs;

	}

	public List<Teg> buscarTegsSegunPrograma(Programa programa,
			Date fechaInicio, Date fechaFin) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegPrograma(programa, fechaInicio, fechaFin);
		return tegs;

	}

	public List<Teg> buscarTegTodos(Date fechaInicio, Date fechaFin) {
		List<Teg> tegs;
		tegs = interfaceTeg.buscarTegTodos(fechaInicio, fechaFin);
		return tegs;
	}
	
	public List<Teg> buscarTegSegunAreaInvestigacionEstatus(AreaInvestigacion area, String estatus1,String estatus2){
		List<Teg> tegs;
		tegs= interfaceTeg.buscarTegSegunArea(area, estatus1,estatus2);
		return tegs;
	}
	public List<Teg> buscarTegSegunAreaInvestigacionPorDosFechasyEstatus(AreaInvestigacion area, String estatus1,String estatus2,String estatus3,String estatus4,Date fechaInicio,Date fechaFin){
		List<Teg> tegs;
		tegs= interfaceTeg.buscarTegSegunAreaInvestigacionPorDosFechasyEstatus(area, estatus1,estatus2,estatus3,estatus4,fechaInicio,fechaFin);
		return tegs;
	}
	public List<Teg> buscarTegSegunTematicaEstatus(Tematica tematica, String estatus1,String estatus2){
		List<Teg> tegs;
		tegs= interfaceTeg.buscarTegSegunTematica(tematica, estatus1,estatus2);
		return tegs;
	}
	public List<Teg> buscarTegSegunProgramaDosEstatus(Programa programa, String estatus1,String estatus2){
		List<Teg> tegs;
		tegs= interfaceTeg.findByEstatusOrEstatusAndEstudiantesIn(estatus1,estatus2,interfaceEstudiante.findByPrograma(programa));
		return tegs;
	}
	public List<Teg> buscarSegunTegs(List<Teg> tegs){
		List<Teg> tegsSeleccionados;
		tegsSeleccionados= interfaceTeg.buscarSegunTegs(tegs);
		return tegsSeleccionados;
	}
	public List<Teg> buscarTegSegunEstatus(String estatus1,String estatus2){
		List<Teg> tegsSeleccionados;
		tegsSeleccionados= interfaceTeg.buscarTegSegunEstatus(estatus1,estatus2);
		return tegsSeleccionados;
	}
	
	/*---- Servicios para Reporte Proyecto ----*/
	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyUnEstatus(
			String estatus, Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechayEstatus(estatus, tematica,
				fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorDosFechasyUnEstatus(
			AreaInvestigacion area1, String estatus,Date fechaInicio,Date fechaFin) {
		return interfaceTeg.buscarTegPorFecha(area1, estatus,fechaInicio, fechaFin);
	}	
	
	public List<Teg> buscarTegPorDosFechasyArea(AreaInvestigacion area1, 
			 Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegFechaArea(area1, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegDeUnaTematicaPorDosFechasyVariosEstatus1(
			String estatus1, String estatus2, String estatus3, String estatus4,
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegporFechayVariosEstatus1(estatus1,
				estatus2, estatus3, estatus4, tematica, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorProgramaVariasAreasUnEstatus(String estatus,
			Programa programa, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorProgramaVariasAreasUnEstatus(estatus,
				programa, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorProgramaVariasAreasVariosEstatus1(
			String estatus1, String estatus2, String estatus3, String estatus4,
			Programa programa, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorProgramaVariasAreasVariosEstatus1(
				estatus1, estatus2, estatus3, estatus4, programa, fechaInicio,
				fechaFin);
	}
	
	public List<Teg> buscarTegPorVariosProgramaUnEstatus(String estatus,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosProgramaUnEstatus(estatus,
				fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegPorVariosProgramasVariosEstatus1(String estatus1,
			String estatus2, String estatus3, String estatus4,
			Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorVariosProgramasVariosEstatus1(estatus1,
				estatus2, estatus3, estatus4, fechaInicio, fechaFin);
	}
	
	/*---- Servicios para Reporte Profesor ----*/	
	
	public List<Teg> buscarTegDeUnaTematicaPorDosFechas(
			Tematica tematica, Date fechaInicio, Date fechaFin) {
		return interfaceTeg.buscarTegPorFechayTematica(tematica, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegDeUnAreaPorDosFechas(
			AreaInvestigacion area1, Date fechaInicio,Date fechaFin) {
		return interfaceTeg.buscarTegPorFechayArea(area1, fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTegDeUnProgramaPorDosFechas(
			Programa programa,Date fechaInicio,Date fechaFin) {
		return interfaceTeg.buscarTegPorFechayPrograma(programa,fechaInicio, fechaFin);
	}
	
	public List<Teg> buscarTodosTegPorDosFechas(
			Date fechaInicio,Date fechaFin) {
		return interfaceTeg.buscarTodosTegporFecha(fechaInicio, fechaFin);
	}
	public List<Teg> buscarTegSegunProgramaEstatus(Programa programa, String estatus1,String estatus2){
		List<Teg> tegs;
		tegs= interfaceTeg.buscarTegSegunPrograma(programa, estatus1,estatus2);
		return tegs;
	}
	
	/*---- Servicios para Reporte Teg ----*/
	public List<Teg> buscarTodosProgramasUnAreaUnaTematicaUnEstatus(Tematica tematica,
				String estatus,Date fechaInicio,Date fechaFin){
			return interfaceTeg.buscarTodosProgramasUnAreaUnaTematicaUnEstatus(tematica,
					estatus, fechaInicio, fechaFin);
		}
		
		public List<Teg> buscarTodosProgramasUnAreaUnaTematicaTodosEstatus(Tematica tematica,
				Date fechaInicio,Date fechaFin){
			return interfaceTeg.buscarTodosProgramasUnAreaUnaTematicaTodosEstatus(tematica,
					fechaInicio,fechaFin);		
		}
		
		public List<Teg> buscarTodosProgramasUnAreaTodasTematicaUnEstatus(AreaInvestigacion areaInvestigacion, 
				String estatus, Date fechaInicio, Date fechaFin){
			return interfaceTeg.buscarTodosProgramasUnAreaTodasTematicaUnEstatus(areaInvestigacion, 
					 estatus, fechaInicio, fechaFin);
			
		}
		
		public List<Teg> buscarTodosProgramasUnAreaTodasTematicaTodosEstatus(AreaInvestigacion areaInvestigacion, 
				Date fechaInicio, Date fechaFin){
			return interfaceTeg.buscarTodosProgramasUnAreaTodasTematicaTodosEstatus(areaInvestigacion,
					fechaInicio, fechaFin);
		}
		
	
}
