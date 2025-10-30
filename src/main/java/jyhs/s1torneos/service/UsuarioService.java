package jyhs.s1torneos.service;

import jyhs.s1torneos.entity.Usuario;

public interface UsuarioService {
    Usuario getUsuarioById(Long id);

    Usuario postUsuario(Usuario usuario);
}
