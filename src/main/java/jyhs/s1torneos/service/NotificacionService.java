package jyhs.s1torneos.service;

import jyhs.s1torneos.dto.NotificacionResponseDTO;
import jyhs.s1torneos.entity.Notificacion;

import java.util.List;

public interface NotificacionService {
    List<NotificacionResponseDTO> obtenerTodasLasNotificaciones();
    List<NotificacionResponseDTO> obtenerNotificacionesPorUsuarioId(Long usuarioId);
    NotificacionResponseDTO crearNotificacion(Long usuarioId, Notificacion notificacion);
    void eliminarNotificaciones();
    void eliminarTodasLasNotificacionesDelUsuario(Long usuarioId);

    NotificacionResponseDTO actualizarModificacion(Notificacion notificacion);
//    NotificacionResponseDTO crearNotificacion(Notificacion notificacion, Long usuarioId);
//    Notificacion crearNotificacion(Notificacion notificacion);
}
