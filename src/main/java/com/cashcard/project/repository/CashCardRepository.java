package com.cashcard.project.repository;

import com.cashcard.project.entities.CashCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashCardRepository extends CrudRepository<CashCard, Long>, PagingAndSortingRepository<CashCard, Long> {
    public CashCard findByIdAndOwner(Long id,String owner);
    public Page<CashCard> findByOwner(String owner, PageRequest amount);
}
