package jyhs.s1torneos.client.dto;

public record EstadisticaDTO(
         Long id,
         Long partidosJugados,
         Long victorias,
         Long empates,
         Long derrotas,
         Long puntos
) {
}
