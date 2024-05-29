package com.aninfo.repository;

import com.aninfo.model.Promo;
import com.aninfo.model.Transaction;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PromoRepository extends CrudRepository<Promo, Long> {
    Promo findPromoById(Long id);

    @Override
    List<Promo> findAll();

    Optional<Promo> findByidTransaction(Long idTransaction);
}
