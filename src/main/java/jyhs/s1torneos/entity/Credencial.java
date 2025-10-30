package jyhs.s1torneos.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long credencialId;
    private String codigo;
    private LocalDate fechaEmision;
    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;
}
