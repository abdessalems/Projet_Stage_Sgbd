package com.example.stagesaadaoui.controller;

import com.example.stagesaadaoui.entity.Enfant;
import com.example.stagesaadaoui.entity.Inscription;
import com.example.stagesaadaoui.entity.Stage;
import com.example.stagesaadaoui.repository.EnfantRepository;
import com.example.stagesaadaoui.repository.InscriptionRepository;
import com.example.stagesaadaoui.repository.StageRepository;
import jakarta.transaction.Transactional;
import org.springframework.validation.BindingResult;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/enfants")
public class stageController {
    @Autowired
    private EnfantRepository enfantRepository;
    @Autowired
    private InscriptionRepository inscriptionRepository; // Autowire InscriptionRepository

    @Autowired
    private StageRepository stageRepository;


    // EnfantController


    // List enfants
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("enfants", enfantRepository.findAll());
        return "enfant-list";
    }


    // Add enfant form
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("enfant", new Enfant());
        model.addAttribute("stages", stageRepository.findAll());
        return "enfant-add";
    }


    // Submit add enfant
    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute Enfant enfant, BindingResult bindingResult,
                            @RequestParam(value = "stageId", required = false) Long stageId, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return back to the form
            model.addAttribute("enfant", enfant);
            model.addAttribute("stages", stageRepository.findAll());
            return "enfant-add";
        }

        // Fetch the selected stage from stageId if it's not null
        Stage stage = null;
        if (stageId != null) {
            stage = stageRepository.findById(stageId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + stageId));
        }

        // Save the Enfant entity explicitly
        enfantRepository.save(enfant);

        // Create a new inscription and associate the child with the selected stage if available
        if (stage != null) {
            Inscription inscription = new Inscription();
            inscription.setEnfant(enfant);
            inscription.setStage(stage);
            inscription.setPaye(false); // Assuming default value for paye
            inscriptionRepository.save(inscription);
        }

        return "redirect:/enfants/list";
    }


    // Edit enfant form
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Enfant enfant = enfantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enfant Id:" + id));
        model.addAttribute("enfant", enfant);
        return "enfant-edit";
    }


    // Update enfant
    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @Valid @ModelAttribute Enfant enfant, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return back to the edit form
            model.addAttribute("enfant", enfant);
            return "enfant-edit";
        }

        // Make sure the ID of the enfant object matches the ID from the path
        if (!enfant.getId().equals(id)) {
            throw new IllegalArgumentException("Enfant ID mismatch");
        }

        enfantRepository.save(enfant);
        return "redirect:/enfants/list";
    }


    // Delete enfant
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Model model) {
        Enfant enfant = enfantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enfant Id:" + id));

        // Check if the enfant has associated inscriptions
        List<Inscription> inscriptions = inscriptionRepository.findByEnfantId(id);
        if (!inscriptions.isEmpty()) {
            // If associated inscriptions exist, return an error message
            model.addAttribute("errorMessage", "Cet enfant ne peut pas être supprimé car il est associé à des inscriptions.");
            model.addAttribute("enfants", enfantRepository.findAll());
            return "enfant-list"; // You can redirect to a suitable error page if needed
        }

        // If no associated inscriptions, proceed with deletion
        enfantRepository.delete(enfant);
        return "redirect:/enfants/list";
    }



    //InscriptionController


    // List inscription
    @GetMapping("/listInscription")
    public String listInscription(@RequestParam(name = "stageName", required = false) String stageName,
                                  @RequestParam(name = "isPaid", required = false) Boolean isPaid,
                                  Model model) {
        // Fetch the list of filtered inscriptions with enfants and stages
        List<Inscription> inscriptions;
        if (stageName != null && isPaid != null) {
            inscriptions = inscriptionRepository.findFilteredInscriptionsByStageNameAndPaye(stageName, isPaid);
        } else if (stageName != null) {
            inscriptions = inscriptionRepository.findFilteredInscriptionsByStageName(stageName);
        } else if (isPaid != null) {
            inscriptions = inscriptionRepository.findFilteredInscriptionsByPaye(isPaid);
        } else {
            inscriptions = inscriptionRepository.findInscriptionsWithEnfantAndStage();
        }

        // Add model attributes for stages and enfants
        model.addAttribute("inscriptions", inscriptions);
        return "inscription-list";
    }

    // Display add inscription form
    @GetMapping("/addInscription")
    public String showAddInscriptionForm(Model model) {
        model.addAttribute("stages", stageRepository.findAll());
        model.addAttribute("enfants", enfantRepository.findAll());
        return "add-inscription"; // Template name corrected
    }

    // Submit add inscription
    @PostMapping("/addInscription")
    public String addInscription(@RequestParam(value = "stageId", required = false) Long stageId,
                                 @RequestParam("childId") Long childId,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            // Check if stageId is provided
            if (stageId == null) {
                throw new IllegalArgumentException("Le stage doit être sélectionné.");
            }

            // Check if childId is valid
            Enfant enfant = enfantRepository.findById(childId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid child Id:" + childId));

            // Fetch the stage based on the provided stageId
            Stage stage = stageRepository.findById(stageId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + stageId));

            // Check age requirement
            if (!isChildEligibleForStage(enfant, stage)) {
                throw new IllegalArgumentException("L'enfant ne répond pas aux exigences d'âge pour ce stage.");
            }

            // Check if the child is already enrolled in the stage
            if (inscriptionRepository.existsByEnfantAndStage(enfant, stage)) {
                throw new IllegalArgumentException("L'enfant est déjà inscrit à ce stage.");
            }

            // Create new Inscription entity and save it
            Inscription inscription = new Inscription();
            inscription.setStage(stage);
            inscription.setEnfant(enfant);
            inscription.setPaye(false); // Assuming default value for paye
            inscriptionRepository.save(inscription);

            redirectAttributes.addFlashAttribute("successMessage", "Inscription ajoutée avec succès.");
            return "redirect:/enfants/listInscription"; // Redirect to proper URL after successful submission
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            // Add necessary attributes for form repopulation if needed
            model.addAttribute("stages", stageRepository.findAll());
            model.addAttribute("enfants", enfantRepository.findAll());
            return "add-inscription";
        }
    }






    //Method to check if child is eligible for stage
    private boolean isChildEligibleForStage(Enfant enfant, Stage stage) {
        // Get today's date
        LocalDate currentDate = LocalDate.now();

        // Calculate the age of the child
        Period age = Period.between(enfant.getDateNaiss(), currentDate);

        // Compare the child's age with the minimum age required for the stage
        return age.getYears() >= stage.getAgeMin();
    }





    // Delete inscription
    @PostMapping("/inscription/delete/{id}")
    @Transactional
    public String deleteInscription(@PathVariable("id") Long id, Model model) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inscription Id:" + id));

        inscriptionRepository.delete(inscription);

        return "redirect:/enfants/listInscription";
    }




                 // StageController

    // List stages
    @GetMapping("/stages")
    public String listStages(Model model) {
        model.addAttribute("stages", stageRepository.findAll());
        return "stage-list";
    }


    // Add stage form
    @GetMapping("/stages/add")
    public String addStageForm(Model model) {
        model.addAttribute("stage", new Stage());
        model.addAttribute("denom", ""); // Add empty string for other attributes if needed
        model.addAttribute("ageMin", 0); // Add default values for other attributes if needed
        model.addAttribute("ageMax", 0);
        model.addAttribute("dateDeb", "");
        model.addAttribute("dateFin", "");
        return "stage-add";
    }



    // Submit add stage
    @PostMapping("/stages/add")
    public String addStageSubmit(@Valid @ModelAttribute("stage") Stage stage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "stage-add"; // Return to the form with error messages if validation fails
        }

        Date startDate = stage.getDateDeb();
        Date endDate = stage.getDateFin();

        if (endDate.before(startDate)) {
            bindingResult.rejectValue("dateFin", "stage.dateFin.invalid", "La date de fin doit être postérieure à la date de début.");
            return "stage-add";
        }

        int minAge = stage.getAgeMin();
        int maxAge = stage.getAgeMax();

        if (minAge < 3 || minAge > 18 || maxAge < 3 || maxAge > 18 || minAge > maxAge) {
            bindingResult.rejectValue("ageMin", "stage.age.invalid", "L'âge minimum et maximum doivent être compris entre 3 et 18 ans, et l'âge maximum doit être supérieur à l'âge minimum.");
            return "stage-add";
        }

        stageRepository.save(stage);
        return "redirect:/enfants/stages";
    }


    // Edit stage form
    @GetMapping("/enfants/stages/edit/{id}")
    public String editStageForm(@PathVariable("id") Long id, Model model) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + id));
        model.addAttribute("stage", stage);
        return "stage-edit";
    }

    // Update stage
    @PostMapping("/enfants/stages/update/{id}")
    public String updateStage(@PathVariable("id") Long id, @Valid @ModelAttribute Stage stage, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("stage", stage);
            return "stage-edit";
        }

        // Update the stage in the database
        Stage updatedStage = stageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + id));
        updatedStage.setDenom(stage.getDenom());
        updatedStage.setAgeMin(stage.getAgeMin());
        updatedStage.setAgeMax(stage.getAgeMax());
        updatedStage.setDateDeb(stage.getDateDeb());
        updatedStage.setDateFin(stage.getDateFin());
        stageRepository.save(updatedStage);

        return "redirect:/enfants/stages"; // Redirect to the list of stages
    }




    // Delete stage
    @GetMapping("/stages/delete/{id}")
    public String deleteStage(@PathVariable("id") Long id, Model model) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + id));

        // Check if the stage has associated inscriptions
        List<Inscription> inscriptions = inscriptionRepository.findByStageId(id);
        if (!inscriptions.isEmpty()) {
            // If associated inscriptions exist, return an error message
            model.addAttribute("errorMessage", "Ce stage ne peut pas être supprimé car il est associé à des inscriptions.");
            model.addAttribute("stages", stageRepository.findAll());
            return "stage-list"; // You can redirect to a suitable error page if needed
        }

        // If no associated inscriptions, proceed with deletion
        stageRepository.delete(stage);
        return "redirect:/enfants/stages";
    }





}
