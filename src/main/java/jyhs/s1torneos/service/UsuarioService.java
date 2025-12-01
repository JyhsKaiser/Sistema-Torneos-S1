package jyhs.s1torneos.service;

import jyhs.s1torneos.dto.UsuarioResponseDTO;
import jyhs.s1torneos.entity.Usuario;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO getUsuarioById(Long id);
    List<UsuarioResponseDTO> listaDeUsuarios();
    UsuarioResponseDTO crearUsuario(Usuario usuario);
    UsuarioResponseDTO modificarUsuario(Usuario u);
    UsuarioResponseDTO eliminarTodosUsuarios();
    // ------------------------UTILIDADES---------------------
    UsuarioResponseDTO convertirUsuarioADTO(Usuario u);
}
