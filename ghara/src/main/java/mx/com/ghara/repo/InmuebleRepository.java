package mx.com.ghara.repo;

import mx.com.ghara.model.Inmueble;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InmuebleRepository extends JpaRepository<Inmueble, Integer> {

    Optional<Inmueble> findBySlug(String slug);
    List<Inmueble> findAllByStatusEquals(int a);
    Page<Inmueble> findAllByStatusEquals(int status, Pageable pageable);
}
