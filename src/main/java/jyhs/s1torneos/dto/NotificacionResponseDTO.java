package jyhs.s1torneos.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class NotificacionResponseDTO {
    private Long id;
    private String destinatario;
    private String asunto;
    private String mensaje;
    private Boolean leido;
    private Long usuarioid;
}
