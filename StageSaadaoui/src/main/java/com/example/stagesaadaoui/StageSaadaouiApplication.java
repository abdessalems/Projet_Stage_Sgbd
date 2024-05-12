package com.example.stagesaadaoui;

import com.example.stagesaadaoui.entity.Enfant;
import com.example.stagesaadaoui.entity.Gender;
import com.example.stagesaadaoui.entity.Stage;
import com.example.stagesaadaoui.entity.Inscription;
import com.example.stagesaadaoui.repository.EnfantRepository;
import com.example.stagesaadaoui.repository.StageRepository;
import com.example.stagesaadaoui.repository.InscriptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.time.LocalDate;

@SpringBootApplication
public class StageSaadaouiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StageSaadaouiApplication.class, args);
    }
    @Bean
    public CommandLineRunner demoData(EnfantRepository enfantRepository,
                                      StageRepository stageRepository,
                                      InscriptionRepository inscriptionRepository) {
        return args -> {
            // Clean existing data
            inscriptionRepository.deleteAll();
            enfantRepository.deleteAll();
            stageRepository.deleteAll();

            // Create Enfants
            Enfant enfant1 = new Enfant();
            enfant1.setNom("John");
            enfant1.setPrenom("Doe");
            enfant1.setSexe(Gender.MALE);
            enfant1.setDateNaiss(LocalDate.of(2010, 5, 20));
            enfantRepository.save(enfant1);

            Enfant enfant2 = new Enfant();
            enfant2.setNom("Jane");
            enfant2.setPrenom("Doe");
            enfant2.setSexe(Gender.FEMALE);
            enfant2.setDateNaiss(LocalDate.of(2012, 8, 15));
            enfantRepository.save(enfant2);

            // Create Enfants
            Enfant enfant3 = new Enfant();
            enfant3.setNom("SAADAOUI");
            enfant3.setPrenom("ABDESSALEM");
            enfant3.setSexe(Gender.MALE);
            enfant3.setDateNaiss(LocalDate.of(2000, 5, 20));
            enfantRepository.save(enfant3);

            // Create Stages
            Stage stage1 = new Stage();
            stage1.setDenom("Summer Camp");
            stage1.setAgeMin(5);
            stage1.setAgeMax(10);
            stage1.setDateDeb(Date.valueOf(LocalDate.of(2024, 7, 1)));
            stage1.setDateFin(Date.valueOf(LocalDate.of(2024, 7, 15)));
            stageRepository.save(stage1);

            Stage stage2 = new Stage();
            stage2.setDenom("Winter Workshop");
            stage2.setAgeMin(8);
            stage2.setAgeMax(12);
            stage2.setDateDeb(Date.valueOf(LocalDate.of(2024, 12, 20)));
            stage2.setDateFin(Date.valueOf(LocalDate.of(2025, 1, 5)));
            stageRepository.save(stage2);


            Stage stage3 = new Stage();
            stage3.setDenom(" dev web");
            stage3.setAgeMin(8);
            stage3.setAgeMax(12);
            stage3.setDateDeb(Date.valueOf(LocalDate.of(2025, 12, 20)));
            stage3.setDateFin(Date.valueOf(LocalDate.of(2026, 1, 5)));
            stageRepository.save(stage3);

            // Create Inscriptions
            Inscription inscription1 = new Inscription();
            inscription1.setEnfant(enfant1);
            inscription1.setStage(stage1);
            inscription1.setPaye(true);
            inscriptionRepository.save(inscription1);

            Inscription inscription2 = new Inscription();
            inscription2.setEnfant(enfant2);
            inscription2.setStage(stage2);
            inscription2.setPaye(false);
            inscriptionRepository.save(inscription2);
        };
    }

}
