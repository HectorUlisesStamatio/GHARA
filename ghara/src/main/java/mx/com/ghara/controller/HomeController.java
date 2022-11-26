package mx.com.ghara.controller;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.com.ghara.model.Peticion;
import mx.com.ghara.model.Inmueble;
import mx.com.ghara.repo.PeticionRepository;
import mx.com.ghara.repo.InmuebleRepository;
import mx.com.ghara.repo.UsuarioRepository;
import mx.com.ghara.service.FileSystemStorageService;
import mx.com.ghara.web.dto.PeticionDTO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api")
public class HomeController extends BaseController {

    private final InmuebleRepository inmuebleRepository;
    private final FileSystemStorageService fileSystemStorageService;
    private final PeticionRepository peticionRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public HomeController(InmuebleRepository inmuebleRepository,
                          FileSystemStorageService fileSystemStorageService, PeticionRepository peticionRepository, UsuarioRepository usuarioRepository) {
        this.inmuebleRepository = inmuebleRepository;
        this.fileSystemStorageService = fileSystemStorageService;
        this.peticionRepository = peticionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * @return los 6 últimos libros
     */
    @GetMapping("/ultimos-espacios")
    List<Inmueble> index() {
        List<Inmueble> inmuebles = inmuebleRepository.findAllByStatusEquals(1);
        inmuebles.forEach(l -> l.setUrlPortada(buildUrlString(l.getRutaPortada())));
        return inmuebles;
    }

    @GetMapping("/espacios")
    Page<Inmueble> getLibros(@PageableDefault(sort = "titulo", size = 10) Pageable pageable) {
        Page<Inmueble> libros = inmuebleRepository.findAllByStatusEquals(1,pageable);
        libros.forEach(libro -> {
            libro.setUrlPortada(buildUrlString(libro.getRutaPortada()));
        });
        return libros;
    }

    @GetMapping("/espacios/{slug}")
    Inmueble getLibro(@PathVariable String slug) {
        Inmueble inmueble = inmuebleRepository.findBySlug(slug)
                .orElseThrow(EntityNotFoundException::new);
        inmueble.setUrlPortada(buildUrlString(inmueble.getRutaPortada()));

        return inmueble;
    }


    @PostMapping("/apartar")
    Peticion crear(@RequestBody @Validated PeticionDTO peticionDTO) {
        Peticion peticion = new ModelMapper().map(peticionDTO, Peticion.class);

        String fechaInicio = peticion.getFechaInicio();
        String[] parts = fechaInicio.split("T");
        String fechaI = parts[0];
        System.out.println(fechaI);
        String fechaFin = peticion.getFechaFin();
        String[] parts2 = fechaFin.split("T");
        String fechaF = parts2[0];




        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd");
            Date fechaInicioD = dateFormat.parse(fechaI);
            Date fechaFinD = dateFormat.parse(fechaF);
            System.out.println("Fecha-1: " + dateFormat.format(fechaInicioD));
            System.out.println("Fecha-2: " + dateFormat.format(fechaFinD));
            if(fechaFinD.before(fechaInicioD)){
                System.out.println(
                        "Error");
                return null;
            }
            if(fechaInicioD.after(fechaFinD)){
                System.out.println(
                        "Error");
                return null;
            }



        } catch (ParseException ex) {
        }

        if(peticion.getFechaInicio().equals(peticion.getFechaFin())){
            return null;
        }

        peticion.setInmueble(inmuebleRepository.getById(peticionDTO.getEspacio()));
        peticion.setUsuario(usuarioRepository.getById(peticionDTO.getUsuario()));
        peticion.setEstado(1);
        return peticionRepository.save(peticion);
    }





}
