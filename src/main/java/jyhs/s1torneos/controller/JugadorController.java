package jyhs.s1torneos.controller;

import jyhs.s1torneos.client.dto.SancionDTO;
import jyhs.s1torneos.dto.JugadorResponseDTO;
import jyhs.s1torneos.entity.Jugador;
import jyhs.s1torneos.service.JugadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jugador")
public class JugadorController {

    @Autowired
    private JugadorService jugadorService;

    @GetMapping
    public ResponseEntity<List<JugadorResponseDTO>> getListaJugadores(){
        List<JugadorResponseDTO> jugadores = jugadorService.obtenerJugadores();
        if (jugadores.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(jugadores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> getJugadorPorId(@PathVariable Long id){
        JugadorResponseDTO jugador = jugadorService.obtenerJugadorPorId(id);
        if(jugador == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jugador);
    }

    @PatchMapping
    ResponseEntity<JugadorResponseDTO> actualizarJugadorSanciones(@RequestBody Jugador jugador)
    {
        JugadorResponseDTO jugadorResponseDTO = jugadorService.actualizarParcialmenteJugador(jugador);
        if (jugador == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jugadorResponseDTO);
    }
//    @PostMapping
//    public ResponseEntity<Jugador> postJugador(@RequestBody Jugador jugador){
//        Jugador newJugador = jugadorService.nuevoJugador(jugador);
//        return ResponseEntity.ok(newJugador);
//    }
}
