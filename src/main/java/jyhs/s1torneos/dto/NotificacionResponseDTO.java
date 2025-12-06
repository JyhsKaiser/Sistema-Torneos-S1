package jyhs.s1torneos.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class NotificacionResponseDTO {
    private Long id;
    private String origen;
    private String asunto;
    private String mensaje;
    private LocalDate fecha;
    private Boolean leida;
    private String tipo;    // danger, success, info
    private Long usuarioid;


}
