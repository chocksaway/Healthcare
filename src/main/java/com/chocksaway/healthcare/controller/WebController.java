package com.chocksaway.healthcare.controller;

import com.chocksaway.healthcare.dto.ActionDTO;
import com.chocksaway.healthcare.dto.PatientDTO;
import com.chocksaway.healthcare.service.PatientService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class WebController {
    private final PatientService patientService;
    public WebController(PatientService patientService){ this.patientService = patientService; }

    @GetMapping("/")
    public String index(Model model){
        long invited = patientService.countInvited();
        long registered = patientService.countRegistered();
        long discharged = patientService.countDischarged();
        model.addAttribute("invited", invited);
        model.addAttribute("registered", registered);
        model.addAttribute("discharged", discharged);
        return "index";
    }

    @GetMapping("/patients")
    public String patients(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        // incoming `page` is 1-based for the UI; convert to 0-based for paging
        int pageIndex = (page <= 0) ? 0 : page - 1;
        Page<PatientDTO> p = patientService.listPage(pageIndex, 10);
        model.addAttribute("patientsPage", p);
        // keep patients attribute for compatibility (content list)
        model.addAttribute("patients", p.getContent());
        return "patients";
    }

    @GetMapping("/patients/{id}")
    public String patient(@PathVariable Long id, Model model){
        Optional<PatientDTO> pOpt = patientService.getPatient(id);
        if (pOpt.isEmpty()) return "redirect:/patients";
        PatientDTO p = pOpt.get();
        model.addAttribute("patient", p);
        List<ActionDTO> actions = patientService.getActionsForPatient(id);
        model.addAttribute("actions", actions);
        return "patient";
    }

    @GetMapping("/api/patients/search")
    @ResponseBody
    public List<PatientDTO> search(@RequestParam(name = "q", required = false) String q) {
        return patientService.search(q);
    }
}
