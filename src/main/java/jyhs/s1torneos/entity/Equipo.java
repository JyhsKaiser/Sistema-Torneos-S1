package jyhs.s1torneos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equipoId;
    private String nombre;
    private String diciplina;
    private String estatus;
    private String municipio;
    private Long convocatoriaId;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
