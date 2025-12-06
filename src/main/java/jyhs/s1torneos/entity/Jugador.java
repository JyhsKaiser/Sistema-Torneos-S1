package jyhs.s1torneos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jyhs.s1torneos.client.dto.SancionDTO;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String curp;
    private LocalDate fechaNacimiento;
    private String fotoUrl;
    private String identificionUrl;
    private String cartaResponsivaUrl;
    private Boolean inscrito;


    @OneToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
//    @JsonIgnore
    private Usuario usuario;

    // SE MARCAN LAS RELACIONES
    @ManyToMany(mappedBy = "jugadores", fetch = FetchType.LAZY)
    private Set<Equipo> equipos;

    @OneToOne(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Credencial credencial;


}
