package com.generation.farmagold.controller;

import com.generation.farmagold.model.Produto;
import com.generation.farmagold.repository.ProdutoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RequestMapping("/produtos")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @GetMapping
    public ResponseEntity<List<Produto>> getAll() {
        return ResponseEntity.ok(produtoRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id) {
        return produtoRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/buscar/{nome}")
    public ResponseEntity<List<Produto>> getByNome(@PathVariable String nome) {
        return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
    }

    @PostMapping("/adicionar")
    public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
    }

    @PutMapping("/editar")
    public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
        return produtoRepository.findById(produto.getId())
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/deletar/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Produto> categoria = produtoRepository.findById(id);

        if (categoria.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        produtoRepository.deleteById(id);
    }

}