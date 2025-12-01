package jyhs.s1torneos.repository;


import jyhs.s1torneos.dto.EquipoResponseDTO;
import jyhs.s1torneos.entity.Equipo;
import jyhs.s1torneos.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Object> findByRepresentanteId(Long representanteId);

    Equipo findEquipoById(Long id);

    EquipoResponseDTO deleteEquipoById(Long id);

    // Busca todos los equipos que tienen un jugador con el id dado.
// JPA sabe que debe unirse a la tabla 'equipo_jugadores' para encontrar la coincidencia.
    List<Equipo> findByJugadores_Id(Long jugadorId);
}
