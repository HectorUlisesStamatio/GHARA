package mx.com.ghara.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Scope("singleton")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idusuario")
    private Integer id;

    private String nombres;
    private String apellidos;
    private String nombreCompleto;

    private String password;
    private String email;
    private String telefono;
    private int status;

    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_act")
    private LocalDateTime fechaActualizacion;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Transient
    private String passwordPlain;

    private String cargo;

    public enum Rol {
        ADMIN,
        USUARIO
    }

    @PrePersist
    private void prePersist(){
        this.nombreCompleto = this.nombres + " " + this.apellidos;
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate(){
        this.nombreCompleto = this.nombres + " " + this.apellidos;
        this.fechaActualizacion = LocalDateTime.now();
    }

}
