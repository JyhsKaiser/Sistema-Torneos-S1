package jyhs.s1torneos.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EquipoAddJugadoresDTO {
    private List<Long> jugadorIds;
}
