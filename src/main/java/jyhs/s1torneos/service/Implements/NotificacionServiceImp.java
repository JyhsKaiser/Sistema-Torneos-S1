package jyhs.s1torneos.service.Implements;

import jakarta.transaction.Transactional;
import jyhs.s1torneos.dto.NotificacionResponseDTO;
import jyhs.s1torneos.entity.Notificacion;
import jyhs.s1torneos.entity.Usuario;
import jyhs.s1torneos.repository.NotificacionRepository;
import jyhs.s1torneos.repository.UsuarioRepository;
import jyhs.s1torneos.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        notificacionResponseDTO.setAsunto(notificacion.getAsunto());
        notificacionResponseDTO.setMensaje(notificacion.getMensaje());
        notificacionResponseDTO.setDestinatario(notificacion.getDestinatario());
        notificacionResponseDTO.setLeido(notificacion.getLeido());
        notificacionResponseDTO.setUsuarioid(notificacion.getUsuario().getId());
        return notificacionResponseDTO;
    }

}
