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
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class StageSaadaouiApplication {


    // Real names for enfants
    private static final List<String> ENFANT_NAMES = Arrays.asList(
            "Saadaoui", "Jane", "Mike", "Emily", "Daniel",
            "Sophia", "Matthew", "Olivia", "David", "Emma"
    );

    // Real last names for enfants
    private static final List<String> LAST_NAMES = Arrays.asList(
            "Abdessalem", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Garcia", "Rodriguez", "Wilson"
    );

    // Real names for stages
    private static final List<String> STAGE_NAMES = Arrays.asList(
            "Summer Camp", "Winter Workshop", "Web Development Bootcamp",
            "Art and Craft Class", "Music Festival", "Science Fair",
            "Dance Workshop", "Coding Camp", "Sports Camp", "Cooking Class"
    );

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

            // Add 10 enfants
            for (int i = 0; i < 10; i++) {
                Enfant enfant = new Enfant();
                enfant.setNom(ENFANT_NAMES.get(i));
                enfant.setPrenom(LAST_NAMES.get(i));
                enfant.setSexe(Gender.MALE); // Set gender as needed
                enfant.setDateNaiss(LocalDate.of(2010, 5, 20)); // Set birth date as needed
                enfantRepository.save(enfant);
            }

            final List<LocalDate> STAGE_START_DATES = Arrays.asList(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 2, 15),
                    LocalDate.of(2024, 3, 07),
                    LocalDate.of(2024, 4, 11),

                    LocalDate.of(2024, 5, 10),

                    LocalDate.of(2024, 6, 05),
                    LocalDate.of(2024, 7, 15),
                    LocalDate.of(2024, 8, 29),

                    LocalDate.of(2024, 9, 15),
                    LocalDate.of(2024, 10, 20)




                    // Add more start dates as needed
            );

            final List<LocalDate> STAGE_END_DATES = Arrays.asList(
                    LocalDate.of(2024, 4, 10),

                    LocalDate.of(2024, 5, 10),

                    LocalDate.of(2024, 6, 10),

                    LocalDate.of(2024, 7, 15),
                    LocalDate.of(2024, 8, 1),
                    LocalDate.of(2024, 9, 1),
                    LocalDate.of(2024, 10, 10),

                    LocalDate.of(2024, 11, 10),

                    LocalDate.of(2025, 2, 10),

                    LocalDate.of(2025, 1, 15)

                    // Add more end dates as needed
            );
            // Add 10 stages
            for (int i = 0; i < STAGE_NAMES.size(); i++) {
                Stage stage = new Stage();
                stage.setDenom(STAGE_NAMES.get(i));
                stage.setAgeMin(5); // Set minimum age as needed
                stage.setAgeMax(10); // Set maximum age as needed
                stage.setDateDeb(Date.valueOf(STAGE_START_DATES.get(i))); // Set start date from list
                stage.setDateFin(Date.valueOf(STAGE_END_DATES.get(i))); // Set end date from list
                stageRepository.save(stage);
            }

            // Add inscriptions for each enfant and each stage
            List<Enfant> enfants = enfantRepository.findAll();
            List<Stage> stages = stageRepository.findAll();
            for (int i = 0; i < enfants.size(); i++) {
                    Inscription inscription = new Inscription();
                    inscription.setEnfant(enfants.get(i));
                    inscription.setStage(stages.get(i));
                    inscription.setPaye(i % 2 == 0); // Alternate paye status for demonstration purposes
                    inscriptionRepository.save(inscription);

            }
        };
    }
}
