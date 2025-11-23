package jyhs.s1torneos.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CredencialResponseDTO {
    private Long id;
    private String codigo;
    private LocalDate fechaEmision;
    private String fotoUrl;
    private Long jugadorId;
}
