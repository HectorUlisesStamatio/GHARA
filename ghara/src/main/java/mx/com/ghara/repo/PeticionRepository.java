package mx.com.ghara.repo;

import mx.com.ghara.model.Peticion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PeticionRepository extends JpaRepository<Peticion, Integer> {
    List<Peticion> findAllByUsuarioEquals(int a);
    Page<Peticion> findAll(Pageable pageable);
}
