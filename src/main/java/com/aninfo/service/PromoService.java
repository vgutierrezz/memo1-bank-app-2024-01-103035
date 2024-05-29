package com.aninfo.service;

import com.aninfo.model.Promo;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.PromoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PromoService {
    @Autowired
    private PromoRepository promoRepository;
    @Autowired
    private AccountRepository accountRepository;

    public Promo createPromo(Double montoBeneficio, Long idTransaction) {
        Promo promo = new Promo();
        promo.setMonto(montoBeneficio);
        promo.setIdTransaction(idTransaction);

        return promoRepository.save(promo);
    }

    //retorna todas las promos
    public Collection<Promo> getPromos() {return promoRepository.findAll();}

    //retorna una promo según id
    public Promo getPromo(Long id) {return promoRepository.findPromoById(id);}

    //guarda una promo
    public void save(Promo promo) {promoRepository.save(promo);}

    public void deleteById(Long id){
        Optional<Promo> optionalPromo = promoRepository.findById(id);
        if (optionalPromo.isPresent()) {
            Promo promo = optionalPromo.get();
            //Elimino el beneficio
            promoRepository.delete(promo);
        }
    }
}