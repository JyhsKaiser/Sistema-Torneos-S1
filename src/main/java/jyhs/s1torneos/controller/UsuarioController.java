package jyhs.s1torneos.controller;


import jyhs.s1torneos.dto.UsuarioResponseDTO;
import jyhs.s1torneos.entity.Usuario;
import jyhs.s1torneos.service.UsuarioService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuario")
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerUsuarios(){
        List<UsuarioResponseDTO> listaUsuarios = usuarioService.listaDeUsuarios();
        return ResponseEntity.ok(listaUsuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getUsuario(@PathVariable Long id){
        UsuarioResponseDTO usuario = usuarioService.getUsuarioById(id);
        if (usuario == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> postUsuario(@RequestBody Usuario usuario){
        UsuarioResponseDTO saveUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveUsuario);    //201
    }

    @PatchMapping
    public ResponseEntity<UsuarioResponseDTO> patchUsuario(@RequestBody Usuario usuario)
    {
        UsuarioResponseDTO usuarioResponseDTO = usuarioService.modificarUsuario(usuario);
        if (usuarioResponseDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @DeleteMapping
    public ResponseEntity<UsuarioResponseDTO> deleteUsuario(){
        if (usuarioService.eliminarTodosUsuarios() == null){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
