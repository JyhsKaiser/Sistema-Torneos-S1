package jyhs.s1torneos.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker; // ¡Nueva importación!
import jyhs.s1torneos.client.dto.SancionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

@Service
public class SancionServiceClient {

    private final WebClient webClient;
    private static final String SANCION_SERVICE = "sancionService"; // Nombre del Circuit Breaker

    public SancionServiceClient(WebClient.Builder webClientBuilder,
                                @Value("${app.services.sancion-url}") String SancionServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(SancionServiceUrl).build();
    }

    // 💡 Paso clave: Envolvemos la llamada con el Circuit Breaker.
    // Si falla la conexión (ConnectException) o supera el umbral de fallos, llama al fallback.
    @CircuitBreaker(name = SANCION_SERVICE, fallbackMethod = "getListaSancionByjugadorIdFallback")
    public List<SancionDTO> getListaSancionByjugadorId(Long  jugadorId) {
        try {
            return webClient.get()
                    .uri("/sancion/{jugadorId}", jugadorId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SancionDTO>>() {})
                    .block();
        } catch (WebClientResponseException.NotFound ex) {
            // Manejamos el 404 (Sanciones no encontradas), devolviendo lista vacía para continuar.
            System.err.println("Sanciones no encontradas para jugador " + jugadorId + " (Status 404).");
            return Collections.emptyList();
        }
        // ¡Nota! Ya NO necesitamos el catch genérico de 'Exception ex',
        // porque las excepciones de conexión son manejadas por el Circuit Breaker,
        // que automáticamente invoca al método de fallback.
    }

    /**
     * 🟢 Método de Fallback 🟢
     * Se ejecuta cuando el Circuit Breaker se abre (servicio caído) o hay un fallo de conexión.
     * @param jugadorId El ID del jugador (mismos argumentos que el método original).
     * @param throwable La excepción que causó el fallo.
     * @return Una lista vacía para que el servicio principal pueda seguir.
     */
    public List<SancionDTO> getListaSancionByjugadorIdFallback(Long jugadorId, Throwable throwable) {
        System.err.println("🚨 [FALLBACK SANCIONES] Microservicio de Sanciones no disponible. Devolviendo lista vacía. Causa: " + throwable.getMessage());
        // ¡OBJETIVO LOGRADO! Devolvemos una lista vacía.
        return Collections.emptyList();
    }
}