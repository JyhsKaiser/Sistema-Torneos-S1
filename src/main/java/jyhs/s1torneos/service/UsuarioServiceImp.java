package jyhs.s1torneos.service;

import jyhs.s1torneos.entity.Rol;
import jyhs.s1torneos.entity.Usuario;
import jyhs.s1torneos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImp implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario getUsuarioById(Long id)
    {
        Usuario usuario = usuarioRepository.getUsuarioByUserId(id);
        if (usuario == null)
        {
            return null;
        }
        return usuario;
    }

    @Override
    public Usuario postUsuario(Usuario usuario)
    {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApellido(usuario.getApellido());
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setTelefono(usuario.getTelefono());
        nuevoUsuario.setRol(Rol.JUGADOR);

        return usuarioRepository.save(nuevoUsuario);
    }

}
