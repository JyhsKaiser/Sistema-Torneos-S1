package jyhs.s1torneos.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificacionId;
    private String destinatario;
    private String asunto;
    private String mensaje;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
