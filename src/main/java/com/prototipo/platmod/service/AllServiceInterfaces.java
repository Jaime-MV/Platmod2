package com.prototipo.platmod.service;
import com.prototipo.platmod.entity.*;
import java.util.List;

// ============================================
// PERFIL DETALLE SERVICE
// ============================================
interface PerfilDetalleService {
    List<PerfilDetalle> obtenerTodos();
    PerfilDetalle obtenerPorId(Long id);
    PerfilDetalle obtenerPorUsuario(Usuario usuario);
    PerfilDetalle crear(PerfilDetalle perfilDetalle);
    PerfilDetalle actualizar(Long id, PerfilDetalle perfilDetalle);
    void eliminar(Long id);
    void actualizarUltimoLogin(Long idUsuario);
}


// ============================================
// DOCENTE SERVICE
// ============================================
interface DocenteService {
    List<Docente> obtenerTodos();
    Docente obtenerPorId(Long id);
    Docente obtenerPorUsuario(Usuario usuario);
    Docente obtenerPorIdUsuario(Long idUsuario);
    List<Docente> obtenerPorEstado(Boolean estado);
    List<Docente> obtenerActivos();
    Docente crear(Docente docente);
    Docente actualizar(Long id, Docente docente);
    void eliminar(Long id);
    void cambiarEstado(Long id, Boolean estado);
}

// ============================================
// MODULO SERVICE
// ============================================
interface ModuloService {
    List<Modulo> obtenerTodos();
    Modulo obtenerPorId(Long id);
    List<Modulo> obtenerPorLeccion(Leccion leccion);
    List<Modulo> obtenerPorLeccionOrdenados(Long idLeccion);
    Modulo crear(Modulo modulo);
    Modulo actualizar(Long id, Modulo modulo);
    void eliminar(Long id);
    void reordenar(Long idLeccion, List<Long> idsModulosOrdenados);
}

// ============================================
// PROGRESO ESTUDIANTE SERVICE
// ============================================
interface ProgresoEstudianteService {
    List<ProgresoEstudiante> obtenerTodos();
    ProgresoEstudiante obtenerPorId(Long id);
    List<ProgresoEstudiante> obtenerPorEstudiante(Estudiante estudiante);
    List<ProgresoEstudiante> obtenerPorEstudianteId(Long idEstudiante);
    List<ProgresoEstudiante> obtenerCompletados(Long idEstudiante);
    ProgresoEstudiante marcarComoCompletado(Long idEstudiante, Long idModulo);
    ProgresoEstudiante crear(ProgresoEstudiante progreso);
    ProgresoEstudiante actualizar(Long id, ProgresoEstudiante progreso);
    void eliminar(Long id);
    double calcularPorcentajeProgreso(Long idEstudiante, Long idCurso);
}

// ============================================
// CERTIFICADO SERVICE
// ============================================
interface CertificadoService {
    List<Certificado> obtenerTodos();
    Certificado obtenerPorId(Long id);
    List<Certificado> obtenerPorEstudiante(Estudiante estudiante);
    List<Certificado> obtenerPorEstudianteId(Long idEstudiante);
    Certificado obtenerPorCodigo(String codigoVerificacion);
    Certificado crear(Certificado certificado);
    Certificado generarCertificado(Long idEstudiante, Long idLeccion);
    void eliminar(Long id);
    boolean verificarCertificado(String codigoVerificacion);
}

// ============================================
// COMENTARIO SERVICE
// ============================================
interface ComentarioService {
    List<Comentario> obtenerTodos();
    Comentario obtenerPorId(Long id);
    List<Comentario> obtenerPorLeccion(Leccion leccion);
    List<Comentario> obtenerPorLeccionId(Long idLeccion);
    List<Comentario> obtenerPorUsuario(Usuario usuario);
    Comentario crear(Comentario comentario);
    Comentario actualizar(Long id, Comentario comentario);
    void eliminar(Long id);
}

// ============================================
// CHAT GRUPAL SERVICE
// ============================================
interface ChatGrupalService {
    List<ChatGrupal> obtenerTodos();
    ChatGrupal obtenerPorId(Long id);
    List<ChatGrupal> obtenerPorLeccion(Long idLeccion);
    List<ChatGrupal> obtenerRecientes(Long idLeccion, int limite);
    ChatGrupal crear(ChatGrupal chatGrupal);
    void eliminar(Long id);
}

// ============================================
// MENSAJE PRIVADO SERVICE
// ============================================
interface MensajePrivadoService {
    List<MensajePrivado> obtenerTodos();
    MensajePrivado obtenerPorId(Long id);
    List<MensajePrivado> obtenerConversacion(Long idUsuario1, Long idUsuario2);
    List<MensajePrivado> obtenerEnviados(Long idEmisor);
    List<MensajePrivado> obtenerRecibidos(Long idReceptor);
    MensajePrivado enviar(MensajePrivado mensaje);
    void eliminar(Long id);
}

// ============================================
// FORO PREGUNTA SERVICE
// ============================================
interface ForoPreguntaService {
    List<ForoPregunta> obtenerTodas();
    ForoPregunta obtenerPorId(Long id);
    List<ForoPregunta> obtenerPorCategoria(String categoria);
    List<ForoPregunta> obtenerPorUsuario(Usuario usuario);
    List<ForoPregunta> buscarPorTitulo(String titulo);
    ForoPregunta crear(ForoPregunta pregunta);
    ForoPregunta actualizar(Long id, ForoPregunta pregunta);
    void eliminar(Long id);
}

// ============================================
// FORO RESPUESTA SERVICE
// ============================================
interface ForoRespuestaService {
    List<ForoRespuesta> obtenerTodas();
    ForoRespuesta obtenerPorId(Long id);
    List<ForoRespuesta> obtenerPorPregunta(ForoPregunta pregunta);
    List<ForoRespuesta> obtenerPorPreguntaId(Long idPregunta);
    List<ForoRespuesta> obtenerVerificadas(Long idPregunta);
    ForoRespuesta crear(ForoRespuesta respuesta);
    ForoRespuesta actualizar(Long id, ForoRespuesta respuesta);
    void eliminar(Long id);
    void verificarRespuesta(Long id);
}
