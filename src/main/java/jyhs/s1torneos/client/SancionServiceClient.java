package jyhs.s1torneos.client;

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

    public SancionServiceClient(WebClient.Builder webClientBuilder,
                                @Value("${app.services.sancion-url}") String SancionServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(SancionServiceUrl).build();
    }

    public List<SancionDTO> getListaSancionByjugadorId(Long  jugadorId) {
        try {
            return webClient.get()
                    .uri("/sancion/{jugadorId}", jugadorId)
                    .retrieve()
                    // ¡SOLUCIÓN! Esperamos un Mono que contiene una Lista de SancionDTO
                    .bodyToMono(new ParameterizedTypeReference<List<SancionDTO>>() {})
                    .block(); // block() ahora devuelve List<SancionDTO>
        }catch (WebClientResponseException.NotFound ex) {
            System.err.println("Sanciones no encontradas: ");
            return null;
        }catch (Exception ex) {
            System.err.println("Error al comunicarse con el Servicio de Competicion y Resultados " + ex.getMessage());
            throw new RuntimeException("Fallo en la comunicacion con servicio externo.", ex);
        }
    }

}
