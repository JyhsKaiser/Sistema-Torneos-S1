package jyhs.s1torneos.service.Implements;

import jakarta.transaction.Transactional;
import jyhs.s1torneos.dto.CredencialResponseDTO;
import jyhs.s1torneos.entity.Credencial;
import jyhs.s1torneos.entity.Jugador;
import jyhs.s1torneos.repository.CredencialRepository;
import jyhs.s1torneos.repository.JugadorRepository;
import jyhs.s1torneos.service.CredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CredencialServiceImp implements CredencialService {

    @Autowired
    private CredencialRepository credencialRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Override
    @Transactional
    public List<CredencialResponseDTO> ObtenerCredenciales() {
        List<Credencial> credencial = credencialRepository.findAll();
        List<CredencialResponseDTO> listaCredenciales = new ArrayList<>();
        if (credencial.isEmpty()) {
            return null;
        }
        credencial.forEach(c -> {
            CredencialResponseDTO credencialResponseDTO = convertirCredencialGuardadaADTO(c);
            listaCredenciales.add(credencialResponseDTO);
        });
        return listaCredenciales;
    }

    @Override
    @Transactional
    public CredencialResponseDTO obtenerCredencialPorJugadorID(Long jugadorId)
    {
        Credencial credencial = credencialRepository.findCredencialByJugador_Id(jugadorId);
        if (credencial == null)
        {
            return null;
        }
        return convertirCredencialGuardadaADTO(credencial);
    }

    @Override
    @Transactional
    public CredencialResponseDTO guardarCredencial(Credencial credencialRequest, Long jugadorId) {


        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new RuntimeException("Jugador no encontrado con id: " + jugadorId)); // Usa una excepción más específica si la tienes


        Credencial credencialParaGuardar = new Credencial();



        credencialParaGuardar.setCodigo(credencialRequest.getCodigo());
        credencialParaGuardar.setFechaEmision(credencialRequest.getFechaEmision());
        credencialParaGuardar.setFotoUrl(credencialRequest.getFotoUrl());


        credencialParaGuardar.setJugador(jugador);


        Credencial credencialGuardada = credencialRepository.save(credencialParaGuardar);


        return convertirCredencialGuardadaADTO(credencialGuardada);
    }

    // Necesitarás un método auxiliar como este
    private CredencialResponseDTO convertirCredencialGuardadaADTO(Credencial credencial) {
        CredencialResponseDTO dto = new CredencialResponseDTO();
        dto.setId(credencial.getId());
        dto.setCodigo(credencial.getCodigo());
        dto.setFechaEmision(credencial.getFechaEmision());
        dto.setFotoUrl(credencial.getFotoUrl());
        if (credencial.getJugador() != null) {
            dto.setJugadorId(credencial.getJugador().getId());
        }
        return dto;
    }


    private CredencialResponseDTO convertirCredencialesADTO(Credencial credencial, Long jugadorId)
    {
        CredencialResponseDTO credencialResponseDTO = new CredencialResponseDTO();
        credencialResponseDTO.setId(credencial.getId());
        credencialResponseDTO.setCodigo(credencial.getCodigo());
        credencialResponseDTO.setFechaEmision(credencial.getFechaEmision());
        credencialResponseDTO.setFotoUrl(credencial.getFotoUrl());
        credencialResponseDTO.setJugadorId(jugadorId);
        return credencialResponseDTO;
    }

}
