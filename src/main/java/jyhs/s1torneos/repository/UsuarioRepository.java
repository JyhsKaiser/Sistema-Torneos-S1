package jyhs.s1torneos.repository;

import jyhs.s1torneos.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario getUsuarioById(Long id);

    Usuario findUsuarioById(Long id);

    boolean existsByEmail(String email);


//    Usuario findById(Long id);

//    Usuario getUsuarioByUserId(Long userId);
}
