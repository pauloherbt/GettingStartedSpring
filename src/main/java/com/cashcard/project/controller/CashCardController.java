package com.cashcard.project.controller;

import com.cashcard.project.entities.CashCard;
import com.cashcard.project.repository.CashCardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping(value = "/cashcards")
public class CashCardController {
    private CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id, Principal principal){
        Optional<CashCard> out= Optional.ofNullable(cashCardRepository.findByIdAndOwner(id, principal.getName()));
        if(out.isPresent())
            return ResponseEntity.ok().body(out.get());
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody CashCard cdRequest, UriComponentsBuilder ucb,Principal principal){
        CashCard newCC= new CashCard(null, cdRequest.amount(), principal.getName());
        CashCard ccToSave=cashCardRepository.save(newCC);
        URI uri = ucb.path("/cashcards/{id}").buildAndExpand(ccToSave.id()).toUri();
        return ResponseEntity.created(uri).build();
    }
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,@RequestBody CashCard ccReq,Principal principal){
        CashCard ccToUpdate = cashCardRepository.findByIdAndOwner(id,principal.getName());
        if(ccToUpdate==null)
            return ResponseEntity.notFound().build();
        CashCard newCc = new CashCard(ccToUpdate.id(),ccReq.amount(), principal.getName());
        cashCardRepository.save(newCc);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<CashCard>> findAll(Pageable pageable,Principal principal) {
        PageRequest pg = PageRequest.of(pageable.getPageNumber()
                , pageable.getPageSize()
                , pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount")));
        Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(), pg);
        return ResponseEntity.ok(page.getContent());
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,Principal principal ) {
        //versao com existsById (perfomance)
        if(cashCardRepository.existsByIdAndOwner(id,principal.getName())){ //autenticado
            cashCardRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}