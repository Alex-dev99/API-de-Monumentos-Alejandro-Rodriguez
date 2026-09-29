package com.salesianos.dam.primerjemplo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/monument")
public class MonumentController {

    private final MonumentRepository monumentRepository;

    @GetMapping
    public ResponseEntity<List<Monument>> getAllMonuments() {
        List<Monument> monumentos = monumentRepository.findAll();

        if (monumentos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(monumentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monument> getMonumentById(@PathVariable Long id) {
        return monumentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Monument> createMonument(@RequestBody Monument monumento) {
        if (!isValid(monumento)) {
            return ResponseEntity.badRequest().build();
        }

        Monument guardado = monumentRepository.save(monumento);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Monument> updateMonument(@PathVariable Long id, @RequestBody Monument monumento) {
        if (!isValid(monumento)) {
            return ResponseEntity.badRequest().build();
        }

        return monumentRepository.findById(id)
                .map(monumentoActual -> {
                    monumentoActual.setCodigoPais(monumento.getCodigoPais());
                    monumentoActual.setNombrePais(monumento.getNombrePais());
                    monumentoActual.setCiudad(monumento.getCiudad());
                    monumentoActual.setLatitud(monumento.getLatitud());
                    monumentoActual.setLongitud(monumento.getLongitud());
                    monumentoActual.setNombre(monumento.getNombre());
                    monumentoActual.setDescripcion(monumento.getDescripcion());
                    monumentoActual.setUrlFoto(monumento.getUrlFoto());
                    return ResponseEntity.ok(monumentRepository.save(monumentoActual));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonument(@PathVariable Long id) {
        monumentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isValid(Monument monumento) {
        if (monumento == null) {
            return false;
        }

        return StringUtils.hasText(monumento.getCodigoPais())
                && monumento.getCodigoPais().length() == 2
                && StringUtils.hasText(monumento.getNombrePais())
                && StringUtils.hasText(monumento.getCiudad())
                && monumento.getLatitud() != null
                && monumento.getLongitud() != null
                && StringUtils.hasText(monumento.getNombre())
                && StringUtils.hasText(monumento.getDescripcion())
                && StringUtils.hasText(monumento.getUrlFoto());
    }
}
