package com.ipiecoles.java.java350.com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;


@SpringBootTest


public class EmployeServiceIntergrationTest {

    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void setup() {
        employeRepository.deleteAll();
    }

    @Test
    public void testIntegrationEmbaucheCommercialTempsPleinLicence() throws EmployeException {
        //Given
        employeRepository.save(new Employe("Doe", "John", "C12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.COMMERCIAL;
        NiveauEtude niveauEtude = NiveauEtude.LICENCE;
        Double tempsPartiel = 1.0;

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);


        //Then
        Employe employe = employeRepository.findByMatricule("C12345");

        Assertions.assertNull(employe);
        Assertions.assertEquals(nom, employe.getNom());
        Assertions.assertEquals(prenom, employe.getPrenom());
        Assertions.assertEquals(LocalDate.now(), employe.getDateEmbauche());
        Assertions.assertEquals("C12345", employe.getMatricule());
        Assertions.assertEquals(tempsPartiel, employe.getTempsPartiel());

        Assertions.assertEquals(1825.46, employe.getSalaire().doubleValue());
    }

}

