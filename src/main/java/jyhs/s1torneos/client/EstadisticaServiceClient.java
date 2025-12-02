package jyhs.s1torneos.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jyhs.s1torneos.client.dto.EstadisticaDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class EstadisticaServiceClient {

    private WebClient webClient;
    private static final String ESTADISTICA_SERVICE = "estadisticaService";

    public EstadisticaServiceClient(WebClient.Builder webClientBuilder,
                                    @Value("${app.services.estadistica-url}") String EstadisticaServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(EstadisticaServiceUrl).build();
    }

    @CircuitBreaker(name = ESTADISTICA_SERVICE, fallbackMethod = "getEstadisticaByIdFallback")
    public EstadisticaDTO getEstadisticaById(Long estadisticaId) {
        try{
            return webClient.get()
                    .uri("/{estadisticaId}", estadisticaId)
                    .retrieve()
                    .bodyToMono(EstadisticaDTO.class)
                    .block();
        }catch (WebClientResponseException.NotFound ex){
            System.err.println("Estadistica no encontrado por Id: " + estadisticaId);
            return null;
        }
    }


    public EstadisticaDTO getEstadisticaByIdFallback(Long estadisticaId, Throwable throwable) {
        System.err.println("🚨 [FALLBACK EQUIPO] Microservicio de Estadistica no disponible. Causa: " + throwable.getMessage());
        return null;
    }
}
