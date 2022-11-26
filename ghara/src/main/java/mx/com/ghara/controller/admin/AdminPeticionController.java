package mx.com.ghara.controller.admin;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.com.ghara.controller.BaseController;
import mx.com.ghara.model.Peticion;
import mx.com.ghara.repo.PeticionRepository;
import mx.com.ghara.service.EmailSenderService;
import mx.com.ghara.web.dto.PeticionDTO;


import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/peticiones")
public class AdminPeticionController extends BaseController {

    private final PeticionRepository peticionRepository;

    @Autowired
    private EmailSenderService senderService;

    @Autowired
    public AdminPeticionController(PeticionRepository peticionRepository) {
        this.peticionRepository = peticionRepository;
    }

    @GetMapping
    Page<Peticion> index(@PageableDefault(size = 5, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Peticion> peticiones = peticionRepository.findAll(pageable);
        return peticiones;
    }

    @GetMapping("/listar")
    List<Peticion> listar() {
        List<Peticion> peticiones = peticionRepository.findAll();
        return peticiones;
    }


    @GetMapping("{id}")
    Peticion get(@PathVariable Integer id) {
        Peticion peticion = peticionRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return peticion;
    }

    @PutMapping("{id}")
    Peticion actualizar(@PathVariable Integer id, @RequestBody @Validated PeticionDTO peticionDTO) {
        Peticion peticionDesdeDb = peticionRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
        ModelMapper mapper = new ModelMapper();
        mapper.map(peticionDTO, peticionDesdeDb);

        String body="Tu solicitud ha sido respondida y fue";
        String cadena="";

        switch (peticionDesdeDb.getEstado()){
            case 0:
                cadena=" rechazada";
                break;
            case 1:
                cadena=" puesta en pendiente";
                break;
            case 2:
                cadena=" aceptada";
                break;
        }
        body=body+cadena;

        sendEmail(peticionDesdeDb.getUsuario().getEmail(),"Tu petición ha sido respondida",body);

        return peticionRepository.save(peticionDesdeDb);
    }


    public void sendEmail(String email,String subject, String body){
        senderService.sendEmail(email,subject,body);
    }
}
