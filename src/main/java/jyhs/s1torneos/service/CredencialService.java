package jyhs.s1torneos.service;

import jyhs.s1torneos.dto.CredencialResponseDTO;
import jyhs.s1torneos.entity.Credencial;

import java.util.List;

public interface CredencialService {
    List<CredencialResponseDTO> ObtenerCredenciales();
    CredencialResponseDTO guardarCredencial(Credencial credencial, Long jugadorId);

    CredencialResponseDTO obtenerCredencialPorJugadorID(Long jugadorId);
}
