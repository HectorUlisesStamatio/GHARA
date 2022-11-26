package mx.com.ghara.controller.admin;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.com.ghara.controller.BaseController;
import mx.com.ghara.model.Usuario;
import mx.com.ghara.repo.UsuarioRepository;
import mx.com.ghara.web.dto.UsuarioDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class AdminUsuarioController extends BaseController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUsuarioController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    Page<Usuario>index(@PageableDefault(sort = "titulo", size = 5, direction = Sort.Direction.ASC) Pageable pageable){
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        return usuarios;
    }

    @GetMapping("/listar")
    List<Usuario> listar(){
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios;
    }


    @GetMapping("{id}")
    Usuario get(@PathVariable Integer id){
        Usuario usuario =usuarioRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return usuario;
    }

    @PostMapping
    Usuario crear(@RequestBody @Validated UsuarioDTO usuarioDTO){
        Usuario usuario = new ModelMapper().map(usuarioDTO,Usuario.class);
        String password = passwordEncoder.encode(usuario.getPasswordPlain());
        usuario.setPassword(password);
        usuario.setStatus(1);
        return  usuarioRepository.save(usuario);
    }

    @PutMapping("{id}")
    Usuario actualizar(@PathVariable Integer id,@RequestBody @Validated UsuarioDTO usuarioDTO){
        Usuario usuarioFromDb = usuarioRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        ModelMapper mapper = new ModelMapper();
        mapper.map(usuarioDTO,usuarioFromDb);
        if (usuarioDTO.getPasswordPlain().length()<60){
            String password = passwordEncoder.encode(usuarioDTO.getPasswordPlain());
            usuarioFromDb.setPassword(password);
        }else{
            String password= usuarioDTO.getPasswordPlain();
            usuarioFromDb.setPassword(password);
        }
        return  usuarioRepository.save(usuarioFromDb);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    void eliminar(@PathVariable Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        usuario.setStatus(0);
        usuarioRepository.save(usuario);
    }
}
