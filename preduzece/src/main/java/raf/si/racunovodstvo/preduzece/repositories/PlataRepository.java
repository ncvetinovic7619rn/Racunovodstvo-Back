package raf.si.racunovodstvo.preduzece.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raf.si.racunovodstvo.preduzece.model.Plata;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlataRepository extends JpaRepository<Plata, Long> {

    public List<Plata> findByZaposleniZaposleniId(Long zaposleniId);

    public Optional<Plata> findByPlataId(Long plataId);

    public List<Plata> findAll(Specification<Plata> spec);
}