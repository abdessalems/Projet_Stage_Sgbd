package com.example.stagesaadaoui.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class RedirectController {
    @GetMapping("/")
    public String redirectToEnfantsList() {
        return "redirect:enfants/list";
    }
}
