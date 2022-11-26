package mx.com.ghara.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PeticionDTO {
    @NotBlank
    private String fechaInicio;

    @NotBlank
    private String fechaFin;

    private int espacio;

    private int usuario;

    private int estado;
}
