package mx.com.ghara.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;

@Data
@Entity
@Scope("singleton")
public class Peticion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPeticion")
    private Integer id;
    private String fechaInicio;
    private String fechaFin;

    @ManyToOne(fetch = FetchType.LAZY) //relacion en la base de datos muchos apartados para una sala
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "inmueble_id")
    private Inmueble inmueble;

    @ManyToOne(fetch = FetchType.LAZY) //relacion en la base de datos muchos apartados para una sala
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private int estado;
}
