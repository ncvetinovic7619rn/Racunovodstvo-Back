package raf.si.racunovodstvo.knjizenje.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import raf.si.racunovodstvo.knjizenje.converter.BazniKontoConverter;
import raf.si.racunovodstvo.knjizenje.converter.TroskovniCentarConverter;
import raf.si.racunovodstvo.knjizenje.model.BazniKonto;
import raf.si.racunovodstvo.knjizenje.model.Knjizenje;
import raf.si.racunovodstvo.knjizenje.model.Konto;
import raf.si.racunovodstvo.knjizenje.model.TroskovniCentar;
import raf.si.racunovodstvo.knjizenje.repositories.BazniKontoRepository;
import raf.si.racunovodstvo.knjizenje.repositories.KnjizenjeRepository;
import raf.si.racunovodstvo.knjizenje.repositories.TroskovniCentarRepository;
import raf.si.racunovodstvo.knjizenje.responses.TroskovniCentarResponse;
import raf.si.racunovodstvo.knjizenje.services.impl.ITroskovniCentarService;

import java.util.List;
import java.util.Optional;

@Service
public class TroskovniCentarService implements ITroskovniCentarService {

    private final TroskovniCentarRepository troskovniCentarRepository;
    private final BazniKontoRepository bazniKontoRepository;
    private final KnjizenjeRepository knjizenjeRepository;
    private TroskovniCentarConverter troskovniCentarConverter;
    private BazniKontoConverter bazniKontoConverter;

    public TroskovniCentarService(TroskovniCentarRepository troskovniCentarRepository, BazniKontoRepository bazniKontoRepository, KnjizenjeRepository knjizenjeRepository, TroskovniCentarConverter troskovniCentarConverter, BazniKontoConverter bazniKontoConverter) {
        this.troskovniCentarRepository = troskovniCentarRepository;
        this.bazniKontoRepository = bazniKontoRepository;
        this.knjizenjeRepository = knjizenjeRepository;
        this.troskovniCentarConverter = troskovniCentarConverter;
        this.bazniKontoConverter = bazniKontoConverter;
    }

    @Override
    public TroskovniCentar save(TroskovniCentar troskovniCentar) {
        return troskovniCentarRepository.save(troskovniCentar);
    }

    @Override
    public Optional<TroskovniCentar> findById(Long id) {
        return troskovniCentarRepository.findById(id);
    }

    @Override
    public List<TroskovniCentar> findAll() {
        return troskovniCentarRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        troskovniCentarRepository.deleteById(id);
    }

    @Override
    public Page<TroskovniCentar> findAll(Pageable sort) {
        return troskovniCentarRepository.findAll(sort);
    }

    @Override
    public TroskovniCentar updateTroskovniCentar(TroskovniCentar troskovniCentar) {
        double ukupanTrosak = 0.0;
        if(troskovniCentar.getKontoList() != null)
        for(BazniKonto k : troskovniCentar.getKontoList()){
            bazniKontoRepository.save(k);
            ukupanTrosak += k.getDuguje()-k.getPotrazuje();
        }
        if(troskovniCentar.getTroskovniCentarList() != null)
        for(TroskovniCentar tc : troskovniCentar.getTroskovniCentarList()){
            ukupanTrosak += tc.getUkupniTrosak();
        }
        troskovniCentar.setUkupniTrosak(ukupanTrosak);
        updateTrosak(troskovniCentar);
        return troskovniCentarRepository.save(troskovniCentar);
    }
    @Override
    public TroskovniCentar addKontosFromKnjizenje(Knjizenje knjizenje, TroskovniCentar troskovniCentar) {
        Optional<Knjizenje> optionalKnjizenje = knjizenjeRepository.findById(knjizenje.getKnjizenjeId());
        double ukupanTrosak = troskovniCentar.getUkupniTrosak();
        for(Konto k : optionalKnjizenje.get().getKonto()){
            BazniKonto bazniKonto = bazniKontoConverter.convert(k);
            bazniKonto.setBazniCentar(troskovniCentar);
            bazniKontoRepository.save(bazniKonto);
            ukupanTrosak += bazniKonto.getDuguje()-bazniKonto.getPotrazuje();
            troskovniCentar.getKontoList().add(bazniKonto);
        }
        troskovniCentar.setUkupniTrosak(ukupanTrosak);
        updateTrosak(troskovniCentar);
        return troskovniCentarRepository.save(troskovniCentar);
    }

    @Override
    public List<TroskovniCentarResponse> findAllTroskovniCentriResponse() {
        return troskovniCentarConverter.convert(troskovniCentarRepository.findAll()).getContent();
    }

    @Override
    public void deleteBazniKontoById(Long bazniKontoId) {
        bazniKontoRepository.deleteById(bazniKontoId);
    }

    @Override
    public Optional<BazniKonto> findBazniKontoById(Long bazniKontoId) {
        return bazniKontoRepository.findById(bazniKontoId);
    }

    private void updateTrosak(TroskovniCentar tc){
        TroskovniCentar parent = tc.getParentTroskovniCentar();
        while(parent != null){
            parent.setUkupniTrosak(parent.getUkupniTrosak()+tc.getUkupniTrosak());
            tc = parent;
            troskovniCentarRepository.save(parent);
            parent = tc.getParentTroskovniCentar();
        }
    }
}
