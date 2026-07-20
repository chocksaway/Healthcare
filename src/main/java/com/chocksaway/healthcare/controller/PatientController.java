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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Controller
public class PatientController {
    private final PatientService patientService;
    public PatientController(PatientService patientService){ this.patientService = patientService; }

    @GetMapping("/")
    public String listInvitedRegisteredDischargedPatients(Model model){
        long invitedPatients = patientService.countInvitedPatients();
        long registeredPatients = patientService.countRegisteredPatients();
        long dischargedPatients = patientService.countDischargedPatients();
        model.addAttribute("invitedPatients", invitedPatients);
        model.addAttribute("registeredPatients", registeredPatients);
        model.addAttribute("dischargedPatients", dischargedPatients);
        return "index";
    }

    @GetMapping("/patients")
    public String listPatientsPaged(Model model, @RequestParam(name = "page", defaultValue = "1") int page){
        // incoming `page` is 1-based for the UI; convert to 0-based for paging
        int pageIndex = (page <= 0) ? 0 : page - 1;
        Page<PatientDTO> patientsPage = patientService.listPage(pageIndex, 10);
        model.addAttribute("patientsPage", patientsPage);
        // keep patients attribute for compatibility (content list)
        model.addAttribute("patients", patientsPage.getContent());
        return "searchAndViewPatientsPaged";
    }

    @GetMapping("/patient/{id}")
    public String getPatient(@PathVariable Long id, Model model){
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
        if (q != null && q.length() > 256) {
            throw new ResponseStatusException(BAD_REQUEST, "q length > 256");
        }
        return patientService.search(q);
    }
}
