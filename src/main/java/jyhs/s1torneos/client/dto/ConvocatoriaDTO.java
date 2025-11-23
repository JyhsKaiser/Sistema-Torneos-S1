package jyhs.s1torneos.client.dto;

public record ConvocatoriaDTO(
        Long id,
        String titulo,
        String disciplina,
        String categoria,
        String estado // Necesario para verificar si está VIGENTE (RF-06)
) {}
