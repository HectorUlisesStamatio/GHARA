package mx.com.ghara.model;


import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;

@Data
@Entity
@Scope("singleton")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "idCategoria")
    private Integer id;
    private String nombre;

}
