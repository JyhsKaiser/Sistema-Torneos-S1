package jyhs.s1torneos.repository;

import jyhs.s1torneos.entity.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredencialRepository extends JpaRepository<Credencial, Long> {
    Credencial findCredencialByJugador_Id(Long jugadorId);
}
