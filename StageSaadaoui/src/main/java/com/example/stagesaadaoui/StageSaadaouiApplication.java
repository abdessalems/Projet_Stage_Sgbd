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


    // Noms réels pour enfants
    private static final List<String> NOMS_ENFANTS = Arrays.asList(
            "Saadaoui", "Jane", "Mike", "Emily", "Daniel",
            "Sophia", "Matthew", "Olivia", "David", "Emma"
    );

    // Noms de famille réels pour enfants
    private static final List<String> NOMS_DE_FAMILLE = Arrays.asList(
            "Abdessalem", "Johnson", "Williams", "Brown", "Jones",
            "Miller", "Davis", "Garcia", "Rodriguez", "Wilson"
    );

    // Noms réels pour stages
    private static final List<String> NOMS_STAGES = Arrays.asList(
            "Camp d'été", "Atelier d'hiver", "Bootcamp de développement Web",
            "Cours d'art et d'artisanat", "Festival de musique", "Foire scientifique",
            "Atelier de danse", "Camp de codage", "Camp de sport", "Cours de cuisine"
    );

    public static void main(String[] args) {
        SpringApplication.run(StageSaadaouiApplication.class, args);
    }

    @Bean
    public CommandLineRunner demoData(EnfantRepository enfantRepository,
                                      StageRepository stageRepository,
                                      InscriptionRepository inscriptionRepository) {
        return args -> {
            // Nettoyer les données existantes
            inscriptionRepository.deleteAll();
            enfantRepository.deleteAll();
            stageRepository.deleteAll();

            // Ajouter 10 enfants
            for (int i = 0; i < 10; i++) {
                Enfant enfant = new Enfant();
                enfant.setNom(NOMS_ENFANTS.get(i));
                enfant.setPrenom(NOMS_DE_FAMILLE.get(i));
                enfant.setSexe(Gender.MALE); // Définir le sexe selon les besoins
                enfant.setDateNaiss(LocalDate.of(2010, 5, 20)); // Définir la date de naissance selon les besoins
                enfantRepository.save(enfant);
            }

            final List<LocalDate> DATES_DEBUT_STAGES = Arrays.asList(
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




                    // Ajouter plus de dates de début au besoin
            );

            final List<LocalDate> DATES_FIN_STAGES = Arrays.asList(
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

                    // Ajouter plus de dates de fin au besoin
            );
            // Ajouter 10 stages
            for (int i = 0; i < NOMS_STAGES.size(); i++) {
                Stage stage = new Stage();
                stage.setDenom(NOMS_STAGES.get(i));
                stage.setAgeMin(5); // Définir l'âge minimum selon les besoins
                stage.setAgeMax(10); // Définir l'âge maximum selon les besoins
                stage.setDateDeb(Date.valueOf(DATES_DEBUT_STAGES.get(i))); // Définir la date de début à partir de la liste
                stage.setDateFin(Date.valueOf(DATES_FIN_STAGES.get(i))); // Définir la date de fin à partir de la liste
                stageRepository.save(stage);
            }

            // Ajouter des inscriptions pour chaque enfant et chaque stage
            List<Enfant> enfants = enfantRepository.findAll();
            List<Stage> stages = stageRepository.findAll();
            for (int i = 0; i < enfants.size(); i++) {
                Inscription inscription = new Inscription();
                inscription.setEnfant(enfants.get(i));
                inscription.setStage(stages.get(i));
                inscription.setPaye(i % 2 == 0); // Alterner le statut de paiement à des fins de démonstration
                inscriptionRepository.save(inscription);

            }
        };
    }
}
