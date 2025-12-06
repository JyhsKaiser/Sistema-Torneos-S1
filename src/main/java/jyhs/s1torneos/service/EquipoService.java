package jyhs.s1torneos.service;

import jyhs.s1torneos.dto.EquipoAddJugadoresDTO;
import jyhs.s1torneos.dto.EquipoResponseDTO;
import jyhs.s1torneos.entity.Equipo;
import jyhs.s1torneos.entity.Jugador;

import java.util.List;

public interface EquipoService {
    EquipoResponseDTO registrarNuevoEquipo(Equipo nuevoEquipo);

    List<EquipoResponseDTO> verListaEquipos();

    EquipoResponseDTO obtenerEquipoPorId(Long id);

    EquipoResponseDTO actualizarEquipo(Long equipoId, Equipo equipoDetalles);

    Equipo agregarJugadores(Long equipoId, EquipoAddJugadoresDTO dto);

    EquipoResponseDTO eliminarEquipoPorId(Long equipoId);

    Equipo eliminarJugadores(Long equipoId, EquipoAddJugadoresDTO dto);

    EquipoResponseDTO obtenerEquipoPorJugadorId(Long jugadorId);
}
