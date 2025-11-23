package jyhs.s1torneos.client;


import jyhs.s1torneos.client.dto.ConvocatoriaDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ConvocatoriaServiceClient {

    private final WebClient webClient;

    public ConvocatoriaServiceClient(WebClient.Builder webClientBuilder,
                                     @Value("${app.services.convocatoria-url}") String convocatoriaServiceUrl) {
        // **http://servicio-convocatorias:8081/api/v2**
        this.webClient = webClientBuilder.baseUrl(convocatoriaServiceUrl).build();
    }

    /**
     * Consulta los detalles de una convocatoria por su ID en el Servicio 2.
     * @param convocatoriaId El ID de la convocatoria.
     * @return El DTO con los detalles, o null si no existe.
     */
    public ConvocatoriaDTO getConvocatoriaById(Long convocatoriaId) {
        try {
            // Llama al endpoint GET /api/v2/convocatorias/{id}
            return webClient.get()
                    .uri("/convocatorias/{id}", convocatoriaId)
                    .retrieve()
                    .bodyToMono(ConvocatoriaDTO.class)
                    .block(); // Bloquea hasta obtener la respuesta (ideal en un flujo síncrono de servicio)

        } catch (WebClientResponseException.NotFound ex) {
            // Manejo cuando S2 devuelve 404
            System.err.println("Convocatoria no encontrada con ID: " + convocatoriaId);
            return null;
        } catch (Exception ex) {
            // Manejo de otros errores de comunicación (Timeout, 5xx, etc.)
            System.err.println("Error al comunicarse con el Servicio de Convocatorias: " + ex.getMessage());
            throw new RuntimeException("Fallo en la comunicación con servicio externo.", ex);
        }
    }

}
