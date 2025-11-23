package jyhs.s1torneos.service.Implements;

import jakarta.transaction.Transactional;
import jyhs.s1torneos.dto.JugadorResponseDTO;
import jyhs.s1torneos.dto.UsuarioResponseDTO;
import jyhs.s1torneos.entity.Jugador;
import jyhs.s1torneos.entity.Rol;
import jyhs.s1torneos.entity.Usuario;
import jyhs.s1torneos.repository.JugadorRepository;
import jyhs.s1torneos.repository.UsuarioRepository;
import jyhs.s1torneos.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImp implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JugadorRepository jugadorRepository;

    @Override
    @Transactional
    public List<UsuarioResponseDTO> listaDeUsuarios() {

        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioResponseDTO> listDTO = new ArrayList<>();

        usuarios.forEach(u -> {
            UsuarioResponseDTO dto = convertirUsuarioADTO(u);

            Jugador j = jugadorRepository.findById(u.getId()).get();
            JugadorResponseDTO jugadorDTO = convertirJugadorADTO(j);

            dto.setJugador(jugadorDTO);
            listDTO.add(dto);
        });

        return listDTO;
    }

    @Override
    public UsuarioResponseDTO getUsuarioById(Long id)
    {
        Usuario usuario = usuarioRepository.getUsuarioById(id);
        if (usuario == null)
        {
            return null;
        }
        Jugador j = jugadorRepository.findById(usuario.getId()).get();
        JugadorResponseDTO jugadorDTO = convertirJugadorADTO(j);

        UsuarioResponseDTO usuarioResponseDTO = convertirUsuarioADTO(usuario);
        usuarioResponseDTO.setJugador(jugadorDTO);

        return usuarioResponseDTO;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO crearUsuario(Usuario usuario) // Recibe la entidad
    {
        // Validación de existencia de email (RNF-07)
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email " + usuario.getEmail() + " ya está registrado.");
        }

        // 1. Guarda el usuario
        Usuario nuevoUsuario = usuarioRepository.save(usuario);

        // 2. Crea y guarda el jugador
        Jugador nuevoJugador = new Jugador();
        nuevoJugador.setUsuario(nuevoUsuario);
        jugadorRepository.save(nuevoJugador);

        // 3. Mapea tu ENTIDAD a un DTO para la respuesta
        UsuarioResponseDTO nuevoUsuarioDTO = convertirUsuarioADTO(usuario);

        // 4. Devuelve el DTO (que no tiene relaciones y no puede causar bucles)
        return nuevoUsuarioDTO;
    }

    @Override
    @Transactional
    public UsuarioResponseDTO modificarUsuario(Usuario u) {

        // 1. Obtener la entidad existente.
        Usuario usuarioExistente = usuarioRepository.findById(u.getId()).orElse(null);
        if (usuarioExistente == null) {
//            throw new RuntimeException("El usuario " + u.getId() + " no existe.");
            return null;
        }

        // 2. Aplicar los cambios condicionalmente: solo si el campo NO es null en el DTO
        UsuarioResponseDTO patchDTO =  convertirUsuarioADTO(u);
        // Nombre
        if (patchDTO.getNombre() != null) {
            usuarioExistente.setNombre(patchDTO.getNombre());
        }

        // Apellido
        if (patchDTO.getApellido() != null) {
            usuarioExistente.setApellido(patchDTO.getApellido());
        }

        // Email
        if (patchDTO.getEmail() != null) {
            usuarioExistente.setEmail(patchDTO.getEmail());
        }

        // Contraseña (IMPORTANTE: Generalmente se maneja en un endpoint separado
        // y con codificación/hashing aquí)
        if (u.getPassword() != null) {
            // **Recomendación:** Usar un PasswordEncoder aquí:
            // usuarioExistente.setPassword(passwordEncoder.encode(patchDTO.getPassword()));
            usuarioExistente.setPassword(u.getPassword());
        }

        // Teléfono
        if (patchDTO.getTelefono() != null) {
            usuarioExistente.setTelefono(patchDTO.getTelefono());
        }

        // Rol
        if (patchDTO.getRol() != null) {
            usuarioExistente.setRol(patchDTO.getRol());
        }

        // 3. Persistir y devolver el objeto actualizado
        usuarioRepository.save(usuarioExistente);

        // 4. Convertir y devolver el DTO de respuesta
        // return usuarioMapper.toResponseDTO(usuarioActualizado);

        // Devuelvo un DTO ficticio para completar el ejemplo
        return patchDTO;
    }


    @Override
    @Transactional
    public UsuarioResponseDTO eliminarTodosUsuarios()
    {
        jugadorRepository.deleteAll();
        usuarioRepository.deleteAll();


        return null;
    }
    // --------------------------- UTILIDADES ----------------------------
    public JugadorResponseDTO convertirJugadorADTO(Jugador j)
    {
        JugadorResponseDTO jugadorDTO = new JugadorResponseDTO();
        jugadorDTO.setId(j.getId());
        jugadorDTO.setCurp(j.getCurp());
        jugadorDTO.setEstado(j.getEstado());
        jugadorDTO.setFotoUrl(j.getFotoUrl());
        jugadorDTO.setFechaNacimiento(j.getFechaNacimiento());
//        jugadorDTO.setUsuarioId(j.getUsuario().getId());
        jugadorDTO.setCartaResponsivaUrl(j.getCartaResponsivaUrl());
        jugadorDTO.setIdentificionUrl(j.getIdentificionUrl());
        return jugadorDTO;
    }

    public UsuarioResponseDTO convertirUsuarioADTO(Usuario u)
    {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setApellido(u.getApellido());
        dto.setEmail(u.getEmail());
        dto.setTelefono(u.getTelefono());
        dto.setRol(u.getRol());
        return dto;
    }

}
