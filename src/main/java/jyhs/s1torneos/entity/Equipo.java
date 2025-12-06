package jyhs.s1torneos.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Equipo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String disciplina;
    private String estatus;
    private String municipio;
    private Long convocatoriaId;
    private Long representanteId;

    private Long estadisticaId;

    // Relación Propietaria ManyToMany (1:1..* miembros)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "equipo_jugadores",
            joinColumns = @JoinColumn(name = "equipo_id"),
            inverseJoinColumns = @JoinColumn(name = "jugador_id")
    )
    private Set<Jugador> jugadores;

    // Métodos de conveniencia para añadir/eliminar jugadores
    public void addJugador(Jugador jugador) {
        this.jugadores.add(jugador);
        jugador.getEquipos().add(this); // Sincronizar el lado inverso
    }
    public void deleteJugador(Jugador jugador)
    {
        this.jugadores.remove(jugador);
        jugador.getEquipos().remove(this);
    }
}
