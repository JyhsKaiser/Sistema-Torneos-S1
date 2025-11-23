package jyhs.s1torneos.repository;

import jyhs.s1torneos.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador,Long> {
    Optional<Jugador> findById(Long id);
}
