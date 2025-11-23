package jyhs.s1torneos.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    @Enumerated(EnumType.STRING)
    private Rol rol;

    // Relación Inversa (1:0..1 perfilJugador)
    // El 'Jugador' es el lado propietario (tiene la clave foránea 'usuario_id')
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Jugador perfilJugador;

    // Relación Inversa (1:0..* notificaciones)
    // La 'Notificacion' es el lado propietario (tiene la clave foránea 'usuario_id')
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Notificacion> notificaciones;
}
