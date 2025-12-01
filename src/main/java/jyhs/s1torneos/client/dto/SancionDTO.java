package jyhs.s1torneos.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public record SancionDTO (
     Long id,
     String descripcion,
     String tipo,
     LocalDate fecha,
     Long jugadorId
) {}
