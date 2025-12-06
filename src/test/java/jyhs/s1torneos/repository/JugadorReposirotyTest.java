package jyhs.s1torneos.repository;

import jakarta.transaction.Transactional;
import jyhs.s1torneos.entity.Equipo;
import jyhs.s1torneos.entity.Jugador;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // Si usas DB real (MySQL)
public class JugadorReposirotyTest {

    @Autowired
    private JugadorRepository jugadorRepository;

@Test // JUnit lo detecta y ejecuta
public void obtenerEquipoDelJugador_debeRecuperarEquipos() {
    // 1. Arrange & Act (Configurar y Ejecutar)
    // El ID 8L debe existir en tu base de datos de prueba y estar relacionado con un equipo.
    Long jugadorId = 6L;

    Optional<Jugador> jugadorOptional = jugadorRepository.findById(jugadorId);

    // 2. Assert (Verificar)
    // Verificación 1: Que el jugador existe
    assertTrue(jugadorOptional.isPresent(), "El jugador con ID " + jugadorId + " debe existir.");

    Jugador jugador = jugadorOptional.get();
    List<Equipo> equipos = new ArrayList<>(jugador.getEquipos());

    // Verificación 2: Que la colección de equipos no esté vacía
    assertFalse(equipos.isEmpty(), "El jugador debe estar asociado al menos a un equipo.");

    // Verificación 3 (Opcional): Imprimir/Verificar el ID del equipo
    System.out.println("Equipos encontrados: " + equipos.size());
    System.out.println("ID del primer equipo: " + equipos.get(0).getId());

    // Asegurarse de que el ID del primer equipo sea positivo (un valor real)
    assertNotNull(equipos.get(0).getId(), "El ID del equipo no debe ser nulo.");
}
}
