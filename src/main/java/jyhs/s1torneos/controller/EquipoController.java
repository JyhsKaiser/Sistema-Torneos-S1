package jyhs.s1torneos.controller;

import jyhs.s1torneos.dto.EquipoAddJugadoresDTO;
import jyhs.s1torneos.dto.EquipoResponseDTO;
import jyhs.s1torneos.entity.Equipo;
import jyhs.s1torneos.entity.Jugador;
import jyhs.s1torneos.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipos")
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> getListaEquipos(){
        List<EquipoResponseDTO> listaEquipos = equipoService.verListaEquipos();
        return ResponseEntity.ok().body(listaEquipos);

    }

    @GetMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> getEquipo(@PathVariable Long equipoId){
        EquipoResponseDTO equipoResponseDTO = equipoService.obtenerEquipoPorId(equipoId);
        if (equipoResponseDTO == null){
            return ResponseEntity.notFound().build();
        }
//        return ResponseEntity.ok().build();
        return ResponseEntity.ok().body(equipoResponseDTO);
    }

    @GetMapping("/jugadorId/{jugadorId}")
    public ResponseEntity<?> getEquipoPorJugadorId(@PathVariable Long jugadorId)
    {
        EquipoResponseDTO respuesta = equipoService.obtenerEquipoPorJugadorId(jugadorId);
        if (respuesta == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<EquipoResponseDTO> postEquipo(@RequestBody Equipo equipo){
        EquipoResponseDTO newTeam = equipoService.registrarNuevoEquipo(equipo);
        return ResponseEntity.ok(newTeam);
    }

    /**
     * Actualiza parcialmente un Equipo (PATCH).
     * @param equipoId El ID del equipo a actualizar.
     * @param equipoDetalles El objeto Equipo con los campos a modificar.
     * @return El Equipo actualizado.
     */
    @PatchMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> actualizarParcialmenteEquipo(
            @PathVariable Long equipoId,
            @RequestBody Equipo equipoDetalles) {

        // El servicio maneja la lógica de actualizar solo los campos presentes.
        EquipoResponseDTO equipoActualizado = equipoService.actualizarEquipo(equipoId, equipoDetalles);

        return ResponseEntity.ok(equipoActualizado);
    }

    @PatchMapping("/{equipoId}/jugadores")
    public ResponseEntity<EquipoResponseDTO> agregarJugadores(
            @PathVariable Long equipoId,
            @RequestBody EquipoAddJugadoresDTO dto) {

        // Llama a la lógica de servicio
        Equipo equipoActualizado = equipoService.agregarJugadores(equipoId, dto);

        // Mapea la entidad Equipo a un DTO de respuesta (EquipoResponseDTO)
        EquipoResponseDTO responseDTO = mapToResponseDTO(equipoActualizado);

        return ResponseEntity.ok(responseDTO);
    }
    @PatchMapping("/{equipoId}/jugadores-eliminados")
    public ResponseEntity<?> deleteEliminarJugadores(
            @PathVariable Long equipoId,
            @RequestBody EquipoAddJugadoresDTO dto) {

        // Llama a la lógica de servicio
        Equipo equipoActualizado = equipoService.eliminarJugadores(equipoId, dto);

        // Mapea la entidad Equipo a un DTO de respuesta (EquipoResponseDTO)
        EquipoResponseDTO responseDTO = mapToResponseDTO(equipoActualizado);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{equipoId}")
    public ResponseEntity<EquipoResponseDTO> eliminarEquipo(@PathVariable Long equipoId) {
        EquipoResponseDTO equipoResponseDTO =  equipoService.eliminarEquipoPorId(equipoId);
        if (equipoResponseDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    // Método auxiliar de mapeo (necesario para evitar errores de serialización)
    private EquipoResponseDTO mapToResponseDTO(Equipo equipo) {
        // ... (Implementación para convertir la Entidad a un DTO sin proxies de Hibernate)
        return new EquipoResponseDTO(); // Placeholder
    }
}
