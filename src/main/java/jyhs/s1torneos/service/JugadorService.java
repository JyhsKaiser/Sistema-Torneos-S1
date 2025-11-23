package jyhs.s1torneos.service;

import jyhs.s1torneos.client.dto.SancionDTO;
import jyhs.s1torneos.dto.JugadorResponseDTO;
import jyhs.s1torneos.entity.Jugador;

import java.util.List;

public interface JugadorService {
//    List<Jugador> obtenerListaJugadores();

    List<JugadorResponseDTO> obtenerJugadores();

    JugadorResponseDTO obtenerJugadorPorId(Long id);

    //    List<SancionDTO> obtenerSancionesDeJugador(Long jugadorId);

    JugadorResponseDTO actualizarParcialmenteJugador(Jugador j);
}
