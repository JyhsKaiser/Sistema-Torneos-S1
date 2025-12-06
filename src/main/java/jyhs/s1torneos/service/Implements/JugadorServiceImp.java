package jyhs.s1torneos.service.Implements;

import jakarta.transaction.Transactional;
import jyhs.s1torneos.client.ConvocatoriaServiceClient;
import jyhs.s1torneos.client.SancionServiceClient;
import jyhs.s1torneos.client.dto.SancionDTO;
import jyhs.s1torneos.dto.JugadorResponseDTO;
import jyhs.s1torneos.entity.Equipo;
import jyhs.s1torneos.entity.Jugador;
import jyhs.s1torneos.repository.JugadorRepository;
import jyhs.s1torneos.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class JugadorServiceImp implements JugadorService {

    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private SancionServiceClient sancionServiceClient;

    @Override
    @Transactional
    public List<JugadorResponseDTO> obtenerJugadores() {
        // 1. Obtener todas las entidades
        List<Jugador> jugadores = jugadorRepository.findAll();

        List<JugadorResponseDTO> jugadorResponseDTOs = new ArrayList<>();

        // 2. Usar un bucle for-each simple
        for (Jugador j : jugadores) {

            // 3. Convertir el jugador actual a DTO
            JugadorResponseDTO dto = convertirJugadorADTO(j);

            // 4. Obtener las sanciones SOLO para este jugador
//            List<SancionDTO> sancionDTOList = sancionServiceClient.getListaSancionByjugadorId(j.getId());

            // 5. Asignar las sanciones SOLO a este DTO
//            dto.setListaSanciones(sancionDTOList);

            // 6. Añadir el DTO completado a la lista final
            jugadorResponseDTOs.add(dto);
        }

        return jugadorResponseDTOs;
    }

    @Override
    @Transactional
    public JugadorResponseDTO obtenerJugadorPorId(Long id) {
        Jugador jugador = jugadorRepository.findById(id).orElse(null);
        if (jugador == null) {
            return null;
        }
        return convertirJugadorADTO(jugador);
    }

    @Transactional
    @Override
    public JugadorResponseDTO obtenerJugadorPorCURP(String CURP) {
        Jugador jugador = jugadorRepository.findJugadorsByCurp(CURP);
        if (jugador == null) {
            return null;
        }
        return convertirJugadorADTO(jugador);
    }

//    @Transactional
//    @Override
//    public Long obtenerEquipoDelJugador(Long jugadorId)
//    {
//        Jugador jugador = jugadorRepository.findById(jugadorId);
//        List<Equipo> equipos = new ArrayList<>(jugador.getEquipos());
//        System.out.println("equipos: " + equipos.getFirst().getId());
//        return equipos.getFirst().getId();
//    }


    @Override
    @Transactional
    public JugadorResponseDTO actualizarParcialmenteJugador(Jugador j) {
        // 1. Encontrar la entidad existente
        Jugador jugadorExistente = jugadorRepository.findById(j.getId()).orElse(null);
        if (jugadorExistente == null) {
            return null;
        }
        JugadorResponseDTO patchDTO = convertirJugadorADTO(j);

        // 2. Aplicar los cambios si existen en el DTO
        // curp
        if (patchDTO.getCurp() != null) {
            jugadorExistente.setCurp(patchDTO.getCurp());
        }

        // fechaNacimiento
        if (patchDTO.getFechaNacimiento() != null) {
            jugadorExistente.setFechaNacimiento(patchDTO.getFechaNacimiento());
        }

        // fotoUrl
        if (patchDTO.getFotoUrl() != null) {
            jugadorExistente.setFotoUrl(patchDTO.getFotoUrl());
        }

        // identificionUrl
        if (patchDTO.getIdentificionUrl() != null) {
            jugadorExistente.setIdentificionUrl(patchDTO.getIdentificionUrl());
        }

        // cartaResponsivaUrl
        if (patchDTO.getCartaResponsivaUrl() != null) {
            jugadorExistente.setCartaResponsivaUrl(patchDTO.getCartaResponsivaUrl());
        }

        // estado (Boolean)
        if (patchDTO.getInscrito() != null) {
            jugadorExistente.setInscrito(patchDTO.getInscrito());
        }



        jugadorRepository.save(jugadorExistente);
        // 3. Guardar y devolver la entidad actualizada
        return patchDTO;
    }


    public JugadorResponseDTO convertirJugadorADTO(Jugador j) {
        JugadorResponseDTO jugadorDTO = new JugadorResponseDTO();
        jugadorDTO.setId(j.getId());
        jugadorDTO.setCurp(j.getCurp());
        jugadorDTO.setInscrito(j.getInscrito());
        jugadorDTO.setFotoUrl(j.getFotoUrl());
        jugadorDTO.setFechaNacimiento(j.getFechaNacimiento());
        jugadorDTO.setCartaResponsivaUrl(j.getCartaResponsivaUrl());
        jugadorDTO.setIdentificionUrl(j.getIdentificionUrl());

        List<SancionDTO> listaSanciones = sancionServiceClient.getListaSancionByjugadorId(j.getId());
        jugadorDTO.setSanciones(listaSanciones);

        return jugadorDTO;
    }
}
