package com.ipiecoles.java.java350.com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import com.ipiecoles.java.java350.service.EmployeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;

    @Mock
    private EmployeRepository employeRepository;

    @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBts() throws EmployeException{
        //Given
        String nom ="John";
        String prenom = "Doe";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);


        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save((employeArgumentCaptor).capture());
        Employe e = employeArgumentCaptor.getValue();

        //Then
        //Si employé existe dans la base avec les bonnes infos
        Assertions.assertEquals("T00001", e.getMatricule());
        Assertions.assertEquals(nom,e.getNom());
        Assertions.assertEquals(prenom,e.getPrenom());
        Assertions.assertEquals(LocalDate.now(),e.getDateEmbauche());
        Assertions.assertEquals(Entreprise.PERFORMANCE_BASE,e.getPerformance());
        //1521.22*1,2*1 = 1825.46
        Assertions.assertEquals(1825.46,(double)e.getSalaire());
        Assertions.assertEquals(tempsPartiel,e.getTempsPartiel());


    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster() throws EmployeException {
        //Given
        String nom ="John";
        String prenom = "Doe";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("00345");
        Mockito.when(employeRepository.findByMatricule("M00346")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        //Si employé existe dans la base avec les bonnes infos
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("M00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.4 * 0.5
        Assertions.assertEquals(1064.85, employeArgumentCaptor.getValue().getSalaire().doubleValue());

    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterLastMatricule99999() {
        //Given
        String nom ="John";
        String prenom = "Doe";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");



        //When
        EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("Limite des 100000 matricules atteinte !", e.getMessage());


    }

    @Test
    public void testEmbaucheEmploye() {
        //Given
        String nom ="Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("M00001")).thenReturn(new Employe());

        //When/Then
       /* EntityExistsException e = Assertions.assertThrows(EntityExistsException.class, () ->employeService.embaucheEmploye(nom,prenom,poste,niveauEtude,tempsPartiel));
        Assertions.assertEquals("L'employé de matricule M00001 existe déjà en BDD", e.getMessage()); */


        EntityExistsException e = Assertions.assertThrows(EntityExistsException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("L'employé de matricule M00001 existe déjà en BDD", e.getMessage());


    }
}
