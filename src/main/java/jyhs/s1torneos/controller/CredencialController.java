package jyhs.s1torneos.controller;

import jyhs.s1torneos.dto.CredencialResponseDTO;
import jyhs.s1torneos.entity.Credencial;
import jyhs.s1torneos.service.CredencialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/credencial")
public class CredencialController {

    @Autowired
    private CredencialService credencialService;

    @GetMapping
    public ResponseEntity<List<CredencialResponseDTO>> getCredenciales(){
        List<CredencialResponseDTO> credencial = credencialService.ObtenerCredenciales();
        if (credencial == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(credencial,HttpStatus.OK);
    }

    @GetMapping("/{jugadorId}")
    public ResponseEntity<?> obtenerCredencialesPorJugadorId(@PathVariable Long jugadorId){
        CredencialResponseDTO credencialResponseDTO = credencialService.obtenerCredencialPorJugadorID(jugadorId);
        if (credencialResponseDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(credencialResponseDTO);
    }

    @PostMapping("/{jugadorId}")
    public ResponseEntity<CredencialResponseDTO> postCredencial(@RequestBody Credencial credencial, @PathVariable Long jugadorId){
        CredencialResponseDTO credencialResponseDTO = credencialService.guardarCredencial(credencial, jugadorId);
        if (credencialResponseDTO == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(credencialResponseDTO,HttpStatus.CREATED);
    }


}
