package jyhs.s1torneos.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origen;
    private String asunto;
    private String mensaje;
    private LocalDate fecha;
    private Boolean leida;
    private String tipo; // danger, success, info

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
