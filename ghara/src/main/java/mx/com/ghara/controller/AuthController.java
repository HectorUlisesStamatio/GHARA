package mx.com.ghara.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.com.ghara.exception.BadRequestException;
import mx.com.ghara.model.Usuario;
import mx.com.ghara.repo.UsuarioRepository;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registrar")
    void registrarUsuario(@RequestBody @Validated Usuario usuario) {
        boolean emailExiste = usuarioRepository.existsByEmail(usuario.getEmail());

        if (emailExiste) {
            throw new BadRequestException("El email ya fue registrado para otro usuario.");
        }
        String password = passwordEncoder.encode(usuario.getPasswordPlain());
        usuario.setPassword(password);
        usuario.setRol(Usuario.Rol.USUARIO);
        usuario.setStatus(1);
        usuarioRepository.save(usuario);
    }

    @GetMapping("/verificar-email")
    Map<String, Boolean> verificarEmail(@RequestParam String email) {
        boolean emailExiste = usuarioRepository.existsByEmail(email);
        Map<String, Boolean> result = new HashMap<>();
        result.put("exists", emailExiste);

        return result;
    }

}
