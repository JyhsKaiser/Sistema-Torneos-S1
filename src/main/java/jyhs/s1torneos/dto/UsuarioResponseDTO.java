package jyhs.s1torneos.dto;

import jyhs.s1torneos.entity.Rol;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Data
//@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
//    private String password;
    private String telefono;
    private Rol rol;
//    private Long jugadorId; // ¡El DTO es el lugar perfecto para este ID!
    private JugadorResponseDTO jugador;
}