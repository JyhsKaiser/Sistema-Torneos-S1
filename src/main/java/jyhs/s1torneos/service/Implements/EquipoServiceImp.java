package jyhs.s1torneos.service.Implements;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jyhs.s1torneos.client.ConvocatoriaServiceClient;
import jyhs.s1torneos.client.EstadisticaServiceClient;
import jyhs.s1torneos.client.dto.ConvocatoriaDTO;
import jyhs.s1torneos.client.dto.EstadisticaDTO;
import jyhs.s1torneos.client.dto.SancionDTO;
import jyhs.s1torneos.dto.EquipoAddJugadoresDTO;
import jyhs.s1torneos.dto.EquipoResponseDTO;
import jyhs.s1torneos.dto.JugadorResponseDTO;
import jyhs.s1torneos.entity.Equipo;
import jyhs.s1torneos.entity.Jugador;
import jyhs.s1torneos.entity.Rol;
import jyhs.s1torneos.entity.Usuario;
import jyhs.s1torneos.repository.EquipoRepository;
import jyhs.s1torneos.repository.JugadorRepository;
import jyhs.s1torneos.repository.UsuarioRepository;
import jyhs.s1torneos.service.EquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.NotActiveException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipoServiceImp implements EquipoService {

    @Autowired
    private ConvocatoriaServiceClient convocatoriaServiceClient;
    @Autowired
    private EquipoRepository equipoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JugadorRepository jugadorRepository;

    Rol role;
    @Autowired
    private EstadisticaServiceClient estadisticaServiceClient;


    @Override
    @Transactional
    public List<EquipoResponseDTO> verListaEquipos() {
        List<Equipo> listaEquipos = equipoRepository.findAll();
        List<EquipoResponseDTO> listaEquiposDTO = new ArrayList<>();
        listaEquipos.forEach(equipo -> {
            EquipoResponseDTO equipoResponseDTO = convertirEquipoADTO(equipo);

            Set<JugadorResponseDTO> jugadoresDTO = equipo.getJugadores().stream()
                    .map(this::convertirJugadorADTO) // Aplica el método de mapeo
                    .collect(Collectors.toSet());

            equipoResponseDTO.setJugadores(jugadoresDTO); // Asignar la colección de DTOs
            listaEquiposDTO.add(equipoResponseDTO);
        });


        return listaEquiposDTO;
    }


    @Override
    @Transactional
    public EquipoResponseDTO obtenerEquipoPorId(Long id)
    {
        Equipo equipo = equipoRepository.findEquipoById(id);
        if (equipo == null)
        {
            return null;
        }
        return convertirEquipoADTO(equipo);
    }
    /**
     * Este metodo solo se usara cuando un jugador cree un nuevo equipo, en tal caso el jugador cambia de rol
     * a representante y tambien se valida que el representante no se registre dos veces a la misma convocatoria
     * <p>
     * Tambien tiene la conexion y validacion con el 2 microservicio, valida que este activo y que el estatus
     * sea VIGENTE
     *
     * @param e es el body de Equipo
     * @return nuevoEquipoDTO para el front
     */
    @Override
    @Transactional
    public EquipoResponseDTO registrarNuevoEquipo(Equipo e) {
        List<Equipo> listaEquipos = equipoRepository.findAll();
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        // ---------------- VALIDACION MICROSERVICIO 2 -----------------------
        ConvocatoriaDTO convocatoria = convocatoriaServiceClient.getConvocatoriaById(
                e.getConvocatoriaId()
        );
        if (!"VIGENTE".equals(convocatoria.estado())) {
            throw new NoResultException(
                    "La convocatoria '" + convocatoria.titulo() + "' no está VIGENTE. Estado actual: " + convocatoria.estado()
            );
        }
        // ---------------------- VALIDACION ----------------------
        if (equipoRepository.findByRepresentanteId(e.getRepresentanteId()).isPresent())
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, // Código 400
                    "El representante con ID " + e.getRepresentanteId() + " ya ha sido asignado a un equipo."
            );
        }

        Usuario u = usuarioRepository.getUsuarioById(e.getRepresentanteId());
        if (u == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El representante con ID " + e.getRepresentanteId() + " no existe!"
                    );
        }
        u.setRol(Rol.REPRESENTANTE);
        usuarioRepository.save(u);

        Equipo equipo = equipoRepository.save(e);
        EquipoResponseDTO nuevoEquipoDTO = convertirEquipoADTO(equipo);
        return nuevoEquipoDTO;
    }

    /**
     * Este metodo se usara unicamente para agregar nuevos jugadores a equipos ya validados y existentes en este
     * Se valida que los usuarios con rol de JUGADOR no puedan volver a participar en diferentes equipos y
     * que los representantes de igual forma no se puedan agregar a mas de un equipo
     *
     * @param equipoId
     * @param dto
     * @return
     */
    @Override
    @Transactional // CRÍTICO: Asegura que todo se guarde junto
    public Equipo agregarJugadores(Long equipoId, EquipoAddJugadoresDTO dto) {

        // 1. Encontrar el Equipo existente
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        // 2. Encontrar los Jugadores (solo se necesitan sus referencias)
        List<Jugador> jugadoresAAgregar = jugadorRepository.findAllById(dto.getJugadorIds());
        List<Equipo> listaEquipos = equipoRepository.findAll();
        if (jugadoresAAgregar.isEmpty()) {
            throw new RuntimeException("Ningún jugador encontrado con los IDs proporcionados.");
        }

        // ---------------------- VALIDACION ----------------------
        listaEquipos.forEach(equipos -> {
            equipos.getJugadores().forEach(jugador -> {
                if (dto.getJugadorIds().contains(jugador.getId())) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, // Código 400
                            "Alguno de los jugadores ya ha sido agregado con ID " + jugador.getId()
                    );
                }

            });
            dto.getJugadorIds().forEach(jugadorId -> {
                if (equipos.getRepresentanteId().equals(jugadorId)) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, // Código 400
                            "No se puede agregar al representante "  + jugadorId + " de equipo ya que esta ya pertenece a un equipo "
                    );
                }
            });

        });



        for (Jugador jugador : jugadoresAAgregar) {
            // Se usa el método de conveniencia para asegurar la sincronización bidireccional
            equipo.addJugador(jugador);
            // Si no usaras el método addJugador:
            // equipo.getMiembros().add(jugador);
            // jugador.getEquipos().add(equipo);
        }

        // 4. Guardar el Equipo (Hibernate insertará las filas en equipo_jugadores)
        return equipoRepository.save(equipo);
    }

    /**
     * Este JSON actualizará el nombre, el estatus, y reasignará el líder del equipo al userId 25.
     * {
     * "nombre": "Los Halcones Dorados V2",
     * "estatus": "ACTIVO",
     * "usuario": {
     * "userId": 25 // Asume que el Usuario con ID 25 ya existe en la base de datos
     * }
     * }
     * Este JSON, por sí solo, actualiza la lista de miembros. El servicio reemplazará la lista actual con esta nueva,
     * asumiendo que los jugadores con IDs 101 y 103 ya existen.
     * {
     * "miembros": [
     * { "jugadorId": 101 },
     * { "jugadorId": 103 }
     * // Cualquier otro jugador que estuviera en el equipo 5 será ELIMINADO de la tabla de unión.
     * // Solo necesitas enviar el ID del jugador dentro del objeto Jugador.
     * ]
     * }
     * Este es un ejemplo que combina la actualización de campos simples, la reasignación del líder, y el
     * reemplazo completo de la lista de miembros en una sola solicitud.
     * {
     * "nombre": "Águilas Supremas",
     * "disciplina": "Básquetbol",
     * "municipio": "Monterrey",
     * "convocatoriaId": 3,
     * "usuario": {
     * "userId": 25 // Nuevo ID del usuario líder del equipo
     * },
     * "miembros": [
     * { "jugadorId": 101 },
     * { "jugadorId": 105 },
     * { "jugadorId": 108 }
     * ]
     * }
     *
     * @return
     */
    @Override
    @Transactional // Asegura que la operación de lectura y escritura esté en una transacción
    public EquipoResponseDTO actualizarEquipo(Long equipoId, Equipo equipoDetalles) {

        Equipo equipoExistente = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new EntityNotFoundException("Equipo no encontrado con ID: " + equipoId));
        EquipoResponseDTO equipoResponseDTO =  convertirEquipoADTO(equipoDetalles);

        // 2. Aplicar las actualizaciones (lógica PATCH)

        // Campo 'nombre'
        if (equipoDetalles.getNombre() != null) {
            equipoExistente.setNombre(equipoDetalles.getNombre());
        }

        // Campo 'disciplina'
        if (equipoDetalles.getDisciplina() != null) {
            equipoExistente.setDisciplina(equipoDetalles.getDisciplina());
        }

        // Campo 'estatus'
        if (equipoDetalles.getEstatus() != null) {
            equipoExistente.setEstatus(equipoDetalles.getEstatus());
        }

        // Campo 'municipio'
        if (equipoDetalles.getMunicipio() != null) {
            equipoExistente.setMunicipio(equipoDetalles.getMunicipio());
        }

        // Campo 'convocatoriaId'
        if (equipoDetalles.getConvocatoriaId() != null) {
            equipoExistente.setConvocatoriaId(equipoDetalles.getConvocatoriaId());
        }

        // Campo 'estadisticaId'
        if (equipoDetalles.getEstadisticaId() != null) {
            equipoExistente.setEstadisticaId(equipoDetalles.getEstadisticaId());
        }

        // =========================================================
        // LÓGICA PARA LA RELACIÓN MANY-TO-MANY: 'miembros'
        // =========================================================
        if (equipoDetalles.getJugadores() != null && !equipoDetalles.getJugadores().isEmpty()) {

            // 1. Obtener los IDs de los jugadores enviados en el JSON
            Set<Long> nuevosJugadoresIds = equipoDetalles.getJugadores().stream()
                    .map(Jugador::getId)
                    .filter(java.util.Objects::nonNull) // Asegura que el ID no sea nulo
                    .collect(java.util.stream.Collectors.toSet());

            // 2. Recuperar las entidades Jugador completas y persistidas
            // Usamos findAllById para hacer una sola consulta a la base de datos
            List<Jugador> jugadoresEncontrados = jugadorRepository.findAllById(nuevosJugadoresIds);

            if (jugadoresEncontrados.size() != nuevosJugadoresIds.size()) {
                // Opcional: Manejar el caso donde se envió un ID de jugador inválido
                throw new EntityNotFoundException("Alguno de los IDs de jugador enviados no es válido o no existe.");
            }

            // 3. Reemplazar completamente el set existente
            // Esto automáticamente maneja las adiciones y eliminaciones en la tabla intermedia.
            // Si ya existen relaciones, se eliminan las que faltan y se agregan las nuevas.
            equipoExistente.setJugadores(new java.util.HashSet<>(jugadoresEncontrados));

        } else if (equipoDetalles.getJugadores() != null && equipoDetalles.getJugadores().isEmpty()) {
            // Opción para vaciar la lista de miembros si se envía un array JSON vacío:
            equipoExistente.setJugadores(java.util.Collections.emptySet());
        }

        // 4. Guardar y devolver el equipo actualizado
        equipoRepository.save(equipoExistente);
        return equipoResponseDTO;

    }

    @Override
    @Transactional
    public EquipoResponseDTO eliminarEquipoPorId(Long equipoId) {
        Equipo equipo = equipoRepository.findEquipoById(equipoId);
        if (equipo == null) {
            return null;
        }
        equipoRepository.delete(equipo);
        return convertirEquipoADTO(equipo);
    }

    // ---------------------------- UTILIDADES ---------------------------
    public EquipoResponseDTO convertirEquipoADTO(Equipo equipo) {
        EquipoResponseDTO nuevoEquipoDTO = new EquipoResponseDTO();
        nuevoEquipoDTO.setId(equipo.getId());
        nuevoEquipoDTO.setNombre(equipo.getNombre());
        nuevoEquipoDTO.setMunicipio(equipo.getMunicipio());
        nuevoEquipoDTO.setConvocatoriaId(equipo.getConvocatoriaId());
        nuevoEquipoDTO.setDisciplina(equipo.getDisciplina());
        nuevoEquipoDTO.setEstatus(equipo.getEstatus());
        nuevoEquipoDTO.setRepresentanteId(equipo.getRepresentanteId());

        EstadisticaDTO estadisticaDTO = estadisticaServiceClient.getEstadisticaById(equipo.getEstadisticaId());
        nuevoEquipoDTO.setEstadisticas(estadisticaDTO);

        if (equipo.getJugadores() != null) {
            Set<JugadorResponseDTO> listaJugadoresDTO = new HashSet<>();
            for (Jugador jugador : equipo.getJugadores()) {
                JugadorResponseDTO jugadorResponseDTO = convertirJugadorADTO(jugador);
                listaJugadoresDTO.add(jugadorResponseDTO);
            }
            nuevoEquipoDTO.setJugadores(listaJugadoresDTO);
        }

        return nuevoEquipoDTO;
    }

    public JugadorResponseDTO convertirJugadorADTO(Jugador j) {
        JugadorResponseDTO jugadorDTO = new JugadorResponseDTO();
        jugadorDTO.setId(j.getId());
        jugadorDTO.setCurp(j.getCurp());
        jugadorDTO.setEstado(j.getEstado());
        jugadorDTO.setFotoUrl(j.getFotoUrl());
        jugadorDTO.setFechaNacimiento(j.getFechaNacimiento());
//        jugadorDTO.setUsuarioId(j.getId());
        jugadorDTO.setCartaResponsivaUrl(j.getCartaResponsivaUrl());
        jugadorDTO.setIdentificionUrl(j.getIdentificionUrl());
        return jugadorDTO;
    }

}
