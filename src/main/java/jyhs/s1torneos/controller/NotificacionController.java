package jyhs.s1torneos.controller;

import jyhs.s1torneos.dto.NotificacionResponseDTO;
import jyhs.s1torneos.entity.Notificacion;
import jyhs.s1torneos.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificacion")
public class NotificacionController {
    @Autowired
    NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> findAll() {
        List<NotificacionResponseDTO> listaNotificaciones = notificacionService.obtenerTodasLasNotificaciones();
        if (listaNotificaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaNotificaciones);
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> findAllByUsuarioId(@PathVariable Long usuarioId) {
        List<NotificacionResponseDTO> listaNotificaciones = notificacionService.obtenerNotificacionesPorUsuarioId(usuarioId);
        if (listaNotificaciones == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listaNotificaciones);
    }

    @PostMapping("/{usuarioId}")
    public ResponseEntity<NotificacionResponseDTO> postNotificacion(@PathVariable Long usuarioId, @RequestBody Notificacion notificacion)
    {
        NotificacionResponseDTO notificacionResponseDTO = notificacionService.crearNotificacion(usuarioId, notificacion);
        if (notificacionResponseDTO != null) {
            return ResponseEntity.ok(notificacionResponseDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping
    public ResponseEntity<?> patchNotificacion(@RequestBody Notificacion notificacion) {
        NotificacionResponseDTO respuesta = notificacionService.actualizarModificacion(notificacion);
        if (respuesta == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/borrarNotificaciones/{usuarioId}")
    public ResponseEntity<NotificacionResponseDTO> borrarNotificacionesUsuario(@PathVariable Long usuarioId) {
        try {
            notificacionService.eliminarTodasLasNotificacionesDelUsuario(usuarioId);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteNotificacion() {
        try {
            notificacionService.eliminarNotificaciones();
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
//    @PostMapping
//    public ResponseEntity<Notificacion> postNotificacion(@RequestBody Notificacion notificacion) {
//        Notificacion newNotificacion = notificacionService.crearNotificacion(notificacion);
//        return ResponseEntity.ok(newNotificacion);
//    }
}
