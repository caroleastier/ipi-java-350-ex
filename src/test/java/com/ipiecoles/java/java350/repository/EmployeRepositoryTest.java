package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNull;


    @ExtendWith(SpringExtension.class)
    @DataJpaTest
//@SrpingBootTest

    class EmployeRepositoryTest {


        @Autowired
        private EmployeRepository employeRepository;

        // @BeforeEach
        // @AfterEach
   /* public void setup(){
        employeRepository.deleteAll();
    } */


        @Test
        public void testFindLastMatricule(){
            //Given

            //When
            String lastMatricule = employeRepository.findLastMatricule();

            //Then
            //Assertions.assertEquals(null, lastMatricule)
            assertNull(lastMatricule);
        }

        @Test
        public void testFindLastMatriculeSingle(){
            //Given
            Employe e = new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);
            employeRepository.save(e);

            //When
            String lastMatricule = employeRepository.findLastMatricule();

            //Then
            Assertions.assertEquals("12345",lastMatricule);
        }

        @Test
        public void testFindLastMatriculeMultiple() {
            //Given
            employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
            employeRepository.save(new Employe("Doe", "Jane", "M40325", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
            employeRepository.save(new Employe("Doe", "Jim", "C06432", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));

            //When
            String lastMatricule = employeRepository.findLastMatricule();

            //Then
            Assertions.assertEquals("40325", lastMatricule);
        }
    }

