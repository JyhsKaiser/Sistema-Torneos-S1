package jyhs.s1torneos.service.Implements;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.transaction.Transactional;
import jyhs.s1torneos.dto.NotificacionResponseDTO;
import jyhs.s1torneos.entity.Notificacion;
import jyhs.s1torneos.entity.Usuario;
import jyhs.s1torneos.repository.NotificacionRepository;
import jyhs.s1torneos.repository.UsuarioRepository;
import jyhs.s1torneos.service.NotificacionService;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NotificacionServiceImp implements NotificacionService {
    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public List<NotificacionResponseDTO> obtenerTodasLasNotificaciones() {
        List<Notificacion> listaNotificaciones = notificacionRepository.findAll();
        List<NotificacionResponseDTO> listaNotificacionesDTO = new ArrayList<>();
        listaNotificaciones.forEach(notificacion -> {
            NotificacionResponseDTO notificacionResponseDTO = convertirNotificacionADTO(notificacion);
            listaNotificacionesDTO.add(notificacionResponseDTO);
        });
        return listaNotificacionesDTO;
    }

    @Override
    @Transactional
    public List<NotificacionResponseDTO> obtenerNotificacionesPorUsuarioId(Long usuarioId) {
        List<Notificacion> listaNotificaciones = notificacionRepository.findNotificacionByUsuario_Id(usuarioId);
        if (listaNotificaciones.isEmpty()) {
            return null;
        }
        List<NotificacionResponseDTO> listaNotificacionesDTO = new ArrayList<>();
        listaNotificaciones.forEach(notificacion -> {
            NotificacionResponseDTO notificacionDTO = convertirNotificacionADTO(notificacion);
            listaNotificacionesDTO.add(notificacionDTO);
        });
        return listaNotificacionesDTO;
    }

    @Override
    @Transactional
    public NotificacionResponseDTO crearNotificacion(Long usuarioId, Notificacion notificacion) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) {
            return null;
        }
        // Agregamos el usuario a la notificacion
        notificacion.setUsuario(usuario);
        notificacionRepository.save(notificacion);


        // Agregamos la notificacion al usuario:
        List<Notificacion> setNotificaciones = usuario.getNotificaciones();
        setNotificaciones.add(notificacion);
        usuario.setNotificaciones(setNotificaciones);
        usuarioRepository.save(usuario);


        return convertirNotificacionADTO(notificacion);
    }

    @Transactional
    @Override
    public NotificacionResponseDTO actualizarModificacion(Notificacion notificacion)
    {
// 1. Verificar si se proporcionó el ID para buscar la entidad existente
        if (notificacion.getId() == null) {
            // Se debe lanzar una excepción o manejar el error adecuadamente
            throw new IllegalArgumentException("El ID de la notificación es requerido para la actualización.");
        }

        // 2. Cargar la entidad existente de la base de datos
        Notificacion notificacionExistente = notificacionRepository.findById(notificacion.getId())
                .orElse(null);

        if (notificacionExistente == null) {
            return null; // Notificación no encontrada
        }

        // 3. Aplicar los cambios condicionalmente (PATCH Lógica)
        // Solo actualizamos el campo si el valor del objeto entrante (notificacionPatch) NO es null.

        // Origen
        if (notificacion.getOrigen() != null) {
            notificacionExistente.setOrigen(notificacion.getOrigen());
        }

        // Asunto
        if (notificacion.getAsunto() != null) {
            notificacionExistente.setAsunto(notificacion.getAsunto());
        }

        // Mensaje
        if (notificacion.getMensaje() != null) {
            notificacionExistente.setMensaje(notificacion.getMensaje());
        }

        // Fecha (Generalmente no se actualiza, pero se incluye por completitud)
        if (notificacion.getFecha() != null) {
            notificacionExistente.setFecha(notificacion.getFecha());
        }

        // Leída (CRÍTICO: Suele ser el campo más actualizado por PATCH)
        if (notificacion.getLeida() != null) {
            notificacionExistente.setLeida(notificacion.getLeida());
        }

        // Tipo
        if (notificacion.getTipo() != null) {
            notificacionExistente.setTipo(notificacion.getTipo());
        }

        // Usuario (Si se actualiza la relación, es CRÍTICO manejar el lazy loading)
        // Esto solo se actualizaría si se cambia la pertenencia de la notificación.
        if (notificacion.getUsuario() != null) {
            // Es mejor solo actualizar el usuario_id si es necesario:
            // notificacionExistente.setUsuario(notificacionPatch.getUsuario());
        }

        // 4. Guardar la entidad modificada
        Notificacion notificacionActualizada = notificacionRepository.save(notificacionExistente);

        // 5. Convertir a DTO y retornar
        return convertirNotificacionADTO(notificacionActualizada);
    }

    @Transactional
    @Override
    public void eliminarTodasLasNotificacionesDelUsuario(Long usuarioId)
    {
        List<Notificacion> listaDeNotificaciones = notificacionRepository.findNotificacionByUsuario_Id(usuarioId);
        notificacionRepository.deleteAll(listaDeNotificaciones);
    }

    @Override
    @Transactional
    public void eliminarNotificaciones() {
        notificacionRepository.deleteAll();
    }
//
//    }
//    @Override
//    @Transactional
//    public NotificacionResponseDTO crearNotificacion(Notificacion notificacion, Long usuarioId) {
//
//
//        Usuario usuario = usuarioRepository.findUsuarioById(usuarioId);
//
//        notificacion.setUsuario(usuario);
//
//        notificacionRepository.save(notificacion);
//
//        return convertirNotificacionADTO(notificacion);
//    }

    private NotificacionResponseDTO convertirNotificacionADTO(Notificacion notificacion) {
        NotificacionResponseDTO notificacionResponseDTO = new NotificacionResponseDTO();

        notificacionResponseDTO.setId(notificacion.getId());
        notificacionResponseDTO.setOrigen(notificacion.getOrigen());
        notificacionResponseDTO.setAsunto(notificacion.getAsunto());
        notificacionResponseDTO.setMensaje(notificacion.getMensaje());
        notificacionResponseDTO.setFecha(notificacion.getFecha());
        notificacionResponseDTO.setLeida(notificacion.getLeida());
        notificacionResponseDTO.setTipo(notificacion.getTipo());
        notificacionResponseDTO.setUsuarioid(notificacion.getUsuario().getId());

        return notificacionResponseDTO;
    }

}
