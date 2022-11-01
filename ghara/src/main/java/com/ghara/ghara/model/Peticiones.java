package com.ghara.ghara.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Peticiones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpeticion")
    private Integer id;

}
