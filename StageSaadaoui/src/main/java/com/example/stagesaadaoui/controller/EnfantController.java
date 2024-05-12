package com.example.stagesaadaoui.controller;

import com.example.stagesaadaoui.entity.Enfant;
import com.example.stagesaadaoui.entity.Inscription;
import com.example.stagesaadaoui.entity.Stage;
import com.example.stagesaadaoui.repository.EnfantRepository;
import com.example.stagesaadaoui.repository.InscriptionRepository;
import com.example.stagesaadaoui.repository.StageRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/enfants")
public class EnfantController {
    @Autowired
    private EnfantRepository enfantRepository;
    @Autowired
    private InscriptionRepository inscriptionRepository; // Autowire InscriptionRepository

    @Autowired
    private StageRepository stageRepository;

    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("enfants", enfantRepository.findAll());
        return "enfant-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("enfant", new Enfant());
        model.addAttribute("stages", stageRepository.findAll()); // Add stages model attribute
        return "enfant-add";
    }


    @PostMapping("/add")
    public String addSubmit(@Valid @ModelAttribute Enfant enfant, BindingResult bindingResult, @RequestParam(value = "stageId", required = false) Long stageId, Model model) {
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





    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Enfant enfant = enfantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enfant Id:" + id));
        model.addAttribute("enfant", enfant);
        return "enfant-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") Long id, @ModelAttribute Enfant enfant, Model model) {
        enfant.setId(id);
        enfantRepository.save(enfant);
        return "redirect:/enfants/list";
    }

    @GetMapping("/delete/{id}")
    @Transactional
    public String delete(@PathVariable("id") Long id, Model model) {
        Enfant enfant = enfantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid enfant Id:" + id));

        // Delete associated inscriptions first
        inscriptionRepository.deleteByEnfant(enfant);

        // Then delete the enfant
        enfantRepository.delete(enfant);

        return "redirect:/enfants/list";
    }


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
        model.addAttribute("stages", stageRepository.findAll());
        model.addAttribute("enfants", enfantRepository.findAll());

        return "inscription-list";
    }

    @PostMapping("/addInscription")
    public String addInscription(@RequestParam("stageId") Long stageId, @RequestParam("childId") Long childId) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + stageId));
        Enfant enfant = enfantRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid child Id:" + childId));

        Inscription inscription = new Inscription();
        inscription.setStage(stage);
        inscription.setEnfant(enfant);
        inscription.setPaye(false); // Assuming default value for paye
        inscriptionRepository.save(inscription);

        return "redirect:/enfants/listInscription";
    }

    @PostMapping("/inscription/delete/{id}")
    @Transactional
    public String deleteInscription(@PathVariable("id") Long id, Model model) {
        Inscription inscription = inscriptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid inscription Id:" + id));

        inscriptionRepository.delete(inscription);

        return "redirect:/enfants/listInscription";
    }



    @GetMapping("/stages")
    public String listStages(Model model) {
        model.addAttribute("stages", stageRepository.findAll());
        return "stage-list";
    }

    @GetMapping("/stages/add")
    public String addStageForm(Model model) {
        model.addAttribute("stage", new Stage());
        return "stage-add";
    }

    @PostMapping("/stages/add")
    public String addStageSubmit(@Valid @ModelAttribute Stage stage, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "stage-add";
        }        stageRepository.save(stage);
        return "redirect:/enfants/stages";
    }

    @GetMapping("/stages/edit/{id}")
    public String editStageForm(@PathVariable("id") Long id, Model model) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + id));
        model.addAttribute("stage", stage);
        return "stage-edit";
    }

    @PostMapping("/stages/update/{id}")
    public String updateStage(@PathVariable("id") Long id, @ModelAttribute Stage stage) {
        stage.setId(id);
        stageRepository.save(stage);
        return "redirect:/enfants/stages";
    }

    @GetMapping("/stages/delete/{id}")
    public String deleteStage(@PathVariable("id") Long id) {
        Stage stage = stageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid stage Id:" + id));
        stageRepository.delete(stage);
        return "redirect:/enfants/stages";
    }

}