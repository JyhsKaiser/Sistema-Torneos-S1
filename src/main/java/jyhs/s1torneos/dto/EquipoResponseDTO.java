package jyhs.s1torneos.dto;

import jyhs.s1torneos.entity.Jugador;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class EquipoResponseDTO {
    private Long id;
    private String nombre;
    private String disciplina;
    private String estatus;
    private String municipio;
    private Long convocatoriaId;
    private Long representanteId;
    private Set<JugadorResponseDTO> jugadores; // <-- ¡CRÍTICO! Usar DTO}
}
