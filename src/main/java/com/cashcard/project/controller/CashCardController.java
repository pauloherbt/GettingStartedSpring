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

import javax.swing.*;
import java.net.URI;
import java.util.*;

@RestController
@RequestMapping(value = "/cashcards")
public class CashCardController {
    private CashCardRepository cashCardRepository;

    public CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CashCard> findById(@PathVariable Long id){
        Optional<CashCard> out= cashCardRepository.findById(id);
        if(out.isPresent())
            return ResponseEntity.ok().body(out.get());
        return ResponseEntity.notFound().build();
    }
    @PostMapping
    public ResponseEntity<Void> insert(@RequestBody CashCard cdRequest, UriComponentsBuilder ucb){
        CashCard ccToSave=cashCardRepository.save(cdRequest);
        URI cashCardUri = ucb.path("/cashcards/{id}").buildAndExpand(ccToSave.id()).toUri();
        return ResponseEntity.created(cashCardUri).build();
    }
    @GetMapping
    public ResponseEntity<List<CashCard>> findAll(Pageable pageable) {
        Page<CashCard> page = cashCardRepository.findAll(
                PageRequest.of(pageable.getPageNumber()
                        ,pageable.getPageSize()
                        ,pageable.getSortOr(Sort.by(Sort.Direction.DESC,"amount"))));
        return ResponseEntity.ok(page.getContent());
    }
}
