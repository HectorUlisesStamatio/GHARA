package mx.com.ghara.web.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UsuarioDTO {

    @NotBlank
    private String nombres ;

    @NotBlank
    private String apellidos;

    private String nombreCompleto;

    @NotBlank
    private String email;

    @NotBlank
    private String passwordPlain;

    private String password;

    @NotBlank
    private String rol;

    @NotBlank
    private String cargo;

    private int status;
}
