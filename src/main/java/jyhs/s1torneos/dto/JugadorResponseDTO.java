package jyhs.s1torneos.dto;

import jyhs.s1torneos.client.dto.SancionDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class JugadorResponseDTO {
    private Long id;
    private String curp;
    private LocalDate fechaNacimiento;
    private String fotoUrl;
    private String identificionUrl;
    private String cartaResponsivaUrl;
    private Boolean estado;

    private List<SancionDTO> sanciones;
//    private Long usuarioId;

//    private List<SancionDTO> listaSanciones;
}
