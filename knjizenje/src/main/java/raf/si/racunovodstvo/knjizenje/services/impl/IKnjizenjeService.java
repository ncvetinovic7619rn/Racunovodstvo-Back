package raf.si.racunovodstvo.knjizenje.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.KontnaGrupa;
import raf.si.racunovodstvo.knjizenje.requests.AnalitickaKarticaRequest;
import raf.si.racunovodstvo.knjizenje.responses.AnalitickaKarticaResponse;
import raf.si.racunovodstvo.knjizenje.responses.KnjizenjeResponse;

import java.util.List;


public interface IKnjizenjeService extends IService<Knjizenje, Long> {

    Page<KnjizenjeResponse> findAll(Specification<Knjizenje> spec, Pageable pageSort);

    Double getSumaPotrazujeZaKnjizenje(Long id);

    Double getSumaDugujeZaKnjizenje(Long id);

    Double getSaldoZaKnjizenje(Long id);

    List<KnjizenjeResponse> findAllKnjizenjeResponse();

    Page<AnalitickaKarticaResponse> findAllAnalitickeKarticeResponse(Specification<Knjizenje> spec, Pageable pageSort, AnalitickaKarticaRequest analitickaKarticaRequest);
}