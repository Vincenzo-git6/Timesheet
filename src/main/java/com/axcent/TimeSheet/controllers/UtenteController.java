package com.axcent.TimeSheet.controllers;

import com.axcent.TimeSheet.services.CustomUtenteRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/utente")
@RequiredArgsConstructor
public class UtenteController
{
    private final CustomUtenteRepositoryImpl customUtenteRepository;

    @GetMapping("/nome/cognome/{id}")
    public Map<String, Object> getNomeCognomeById(@PathVariable Long id)
    {
        return customUtenteRepository.getUtenteNomeCognome(id);
    }
}
