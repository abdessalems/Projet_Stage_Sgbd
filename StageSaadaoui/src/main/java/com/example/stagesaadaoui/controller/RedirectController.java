package com.example.stagesaadaoui.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class RedirectController {
    @GetMapping("")
    public String redirectToEnfantsList() {
        return "redirect:/enfants/list";
    }
}
