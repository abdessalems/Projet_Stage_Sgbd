package com.example.stagesaadaoui.repository;

import com.example.stagesaadaoui.entity.Enfant;
import com.example.stagesaadaoui.entity.Inscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    // Method to find inscriptions by stage name

    @Query("SELECT i FROM Inscription i WHERE i.stage.denom LIKE %:stageName%")
    List<Inscription> findFilteredInscriptionsByStageName(@Param("stageName") String stageName);

    // Method to find inscriptions by payment status
    List<Inscription> findFilteredInscriptionsByPaye(Boolean paye);

    // Method to find inscriptions by both stage name and payment status
    @Query("SELECT i FROM Inscription i WHERE i.stage.denom LIKE %:stageName% AND i.paye = :paye")
    List<Inscription> findFilteredInscriptionsByStageNameAndPaye(@Param("stageName") String stageName, @Param("paye") Boolean paye);

    // Method to find inscriptions with enfants and stages
    @Query("SELECT i FROM Inscription i JOIN FETCH i.enfant e JOIN FETCH i.stage s")
    List<Inscription> findInscriptionsWithEnfantAndStage();

    void deleteByEnfant(Enfant enfant);
}
