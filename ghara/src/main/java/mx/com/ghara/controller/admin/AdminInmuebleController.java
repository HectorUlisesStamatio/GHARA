package mx.com.ghara.controller.admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.com.ghara.controller.BaseController;
import mx.com.ghara.model.Inmueble;
import mx.com.ghara.repo.InmuebleRepository;
import mx.com.ghara.web.dto.InmuebleDTO;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/inmueble")
public class AdminInmuebleController extends BaseController {

    private final InmuebleRepository inmuebleRepository;

    @Autowired
    public AdminInmuebleController(InmuebleRepository inmuebleRepository) {
        this.inmuebleRepository = inmuebleRepository;
    }

    @GetMapping
    Page<Inmueble> index(@PageableDefault(sort = "titulo", size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Inmueble> libros = inmuebleRepository.findAll(pageable);

        libros.forEach(libro -> {
            libro.setUrlPortada(buildUrlString(libro.getRutaPortada()));
        });

        return libros;
    }

    @GetMapping("/listar")
    List<Inmueble> listar() {
        List<Inmueble> inmuebles = inmuebleRepository.findAll();

        inmuebles.forEach(libro -> {
            libro.setUrlPortada(buildUrlString(libro.getRutaPortada()));
        });
        return inmuebles;
    }


    @GetMapping("{id}")
    Inmueble get(@PathVariable Integer id) {
        Inmueble inmueble = inmuebleRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        inmueble.setUrlPortada(buildUrlString(inmueble.getRutaPortada()));

        return inmueble;
    }

    @PostMapping
    Inmueble crear(@RequestBody @Validated InmuebleDTO inmuebleDTO) {
        Inmueble inmueble = new ModelMapper().map(inmuebleDTO, Inmueble.class);
        inmueble.setStatus(1);
        return inmuebleRepository.save(inmueble);
    }

    @PutMapping("{id}")
    Inmueble actualizar(@PathVariable Integer id, @RequestBody @Validated InmuebleDTO inmuebleDTO) {
        Inmueble inmuebleFromDb = inmuebleRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        ModelMapper mapper = new ModelMapper();
        mapper.map(inmuebleDTO, inmuebleFromDb);

        return inmuebleRepository.save(inmuebleFromDb);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{id}")
    void eliminar(@PathVariable Integer id) {

        Inmueble inmueble = inmuebleRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
        inmueble.setStatus(0);
        inmuebleRepository.save(inmueble);
    }


}
