package mx.com.ghara.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CategoriaDTO {
    @NotBlank
    private String nombre;
}
