package mx.com.ghara.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Scope("singleton")
public class Inmueble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlibro")
    private Integer id;

    private String titulo;
    private String slug;
    private String descripcion;
    private String rutaPortada;
    private String direccion;
    private LocalDateTime fechaCreacion;
    private int cupos;
    private int equipos;
    private int proyectores;
    private int status;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    @Transient
    private String urlPortada;

    @Transient
    private String urlArchivo;

    @ManyToOne(fetch = FetchType.LAZY) //relacion en la base de datos muchos apartados para una sala
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


    @PrePersist
    void asignarFechaCreacion() {
        fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    void asignarFechaActualizacion() {
        fechaActualizacion = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }
}
