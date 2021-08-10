package com.github.slbb.ws.address.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;


@Component
@Validated
public interface SuburbRepository extends PagingAndSortingRepository<Suburb, Long> {

    @RestResource(path = "name", rel = "name")
    Page<Suburb> findByNameStartsWithIgnoreCase(@Param("name") @Size(min=3) String name, Pageable p);

    @RestResource(path = "postCode", rel = "postCode")
    Page<Suburb> findByPostCode(@Param("code") @Min(0) @Max(Integer.MAX_VALUE) Integer postCode, Pageable p);

    @RestResource(exported = true)
    @Override
    Suburb save(Suburb entity);

    @RestResource(exported = false)
    @Override
    <S extends Suburb> Iterable<S> saveAll(Iterable<S> entities);

    @RestResource(exported = true)
    @Override
    void deleteById(Long id);

    @RestResource(exported = true)
    @Override
    void delete(Suburb entity);

    @RestResource(exported = false)
    @Override
    void deleteAll(Iterable<? extends Suburb> entities);

    @RestResource(exported = false)
    @Override
    void deleteAll();

}
