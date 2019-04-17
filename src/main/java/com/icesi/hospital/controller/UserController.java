package com.icesi.hospital.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.icesi.hospital.model.AdmininistrationType;
import com.icesi.hospital.model.InventaryMedicine;
import com.icesi.hospital.model.Medicine;
import com.icesi.hospital.model.Patient;
import com.icesi.hospital.model.State;
import com.icesi.hospital.model.Supply;
import com.icesi.hospital.model.UrgencyAttention;
import com.icesi.hospital.repositories.IMedicineRepository;
import com.icesi.hospital.repositories.IPatientRepository;
import com.icesi.hospital.services.IInventaryService;
import com.icesi.hospital.services.ISupplyService;
import com.icesi.hospital.services.IUrgencyService;
import com.icesi.hospital.view.ViewConsultByDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * AppController
 */
@Controller
public class UserController {

 

    @Autowired
    private IUrgencyService urgencyService;

    @Autowired
    private IPatientRepository patientRepository;

    @Autowired
    private IInventaryService inventaryService;

    @Autowired
    private IMedicineRepository medicineRepository;

    @Autowired
    private ISupplyService supplyService;

    @PostConstruct
    public void setup() {

        setupPatients();

        setupMedicines();

        setupInventaries();

    }

    private void setupPatients() {

        Patient p1 = new Patient("91680", "Isabel", "Kuhn", State.ACTIVO);
        Patient p2 = new Patient("9248", "Shawn", "Ritchie", State.ACTIVO);
        Patient p3 = new Patient("15125", "Kallie", "Oberbrunner", State.ACTIVO);
        Patient p4 = new Patient("29427", "Eugenia", "Hansen", State.INACTIVO);

        patientRepository.save(p1);
        patientRepository.save(p2);
        patientRepository.save(p3);
        patientRepository.save(p4);
    }

    private void setupMedicines() {

        Medicine m1 = new Medicine("91091", "laborum", "mollitia", "Lead Assurance Labs",
                AdmininistrationType.INYECCION, "indicaciones 1");
        Medicine m2 = new Medicine("37485", "voluptatem", "reiciendis", "Dynamic Tactics Labs",
                AdmininistrationType.CAPSULA, "indicaciones 2");
        Medicine m3 = new Medicine("5533", "explicabo", "qui", "Central Lab Strategist", AdmininistrationType.INYECCION,
                "");
        Medicine m4 = new Medicine("25181", "placeat", "sequi", "Human Lab", AdmininistrationType.CAPSULA, "");

        medicineRepository.save(m1);
        medicineRepository.save(m2);
        medicineRepository.save(m3);
        medicineRepository.save(m4);

    }

    private void setupInventaries() {
        for (Medicine m : medicineRepository.findAll()) {
            InventaryMedicine in = new InventaryMedicine(m, 20, "13518", new Date("25/03/2020"));
            inventaryService.addInventaryMedicine(in);
        }
    }

    @GetMapping(value = "/consultByDate")
    public String testConsult(Model model, @ModelAttribute ViewConsultByDate<?> viewConsult,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mapping = "";

       

        String type = viewConsult.getType().toLowerCase();
        if (type != null) {

            if (viewConsult.getDate() != null) {
                consult(viewConsult);
            }

            model.addAttribute("consult", viewConsult);

            if (type.equals("attention")) {
                mapping = "urgency/consult";
            } else if (type.equals("supply")) {
                mapping = "supply/consult";
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "error in consult");
        }

        return mapping;
    }

    private void consult(ViewConsultByDate<?> viewConsult) {

        consultByDate(viewConsult);

    }

    private void consultByDate(ViewConsultByDate viewConsult) {

        Date date = viewConsult.getDate();
        switch (viewConsult.getType().toLowerCase()) {

        case "attention": {
            List<UrgencyAttention> urgencyAttentions = urgencyService.findByDate(date);
            viewConsult.setResults(urgencyAttentions);
            break;
        }

        case "supply": {

            List<Supply> supplies = supplyService.findByDate(date);

            viewConsult.setResults(supplies);

            break;
        }
        }
    }

    @GetMapping("/")
    public String index(Authentication authentication, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request,
             @RequestParam(value = "error", required = false) String error) {
        String mapping = "";

        if (error != null) {
            model.addAttribute("error", error);
        }

        if (authentication != null) {

            model.addAttribute("userName", authentication.getName());
        }
        mapping = "index";

        return mapping;
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @GetMapping(value = "/edit-supply")
    public String editSupply(Model model, @RequestParam(value = "supplyConsecutive") String supplyConsecutive,
            @RequestParam(value = "urgencyConsecutive", required = false) String urgencyConsecutive,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mapping = "supply/add_supply";

        Supply supply = supplyService.getSupply(supplyConsecutive);

        urgencyService.removeSupply(urgencyConsecutive, supplyConsecutive);

        model.addAttribute("medicines", medicineRepository.findAll());
        model.addAttribute("supply", supply);
        model.addAttribute("editing", "editing");

        if (urgencyConsecutive != null) {
            model.addAttribute("urgencyConsecutive", urgencyConsecutive);
        } else {
            model.addAttribute("patients", patientRepository.findAll());
        }

        return mapping;
    }

    @GetMapping(value = "/remove-supply")
    public String removeSupply(Model model, @RequestParam(value = "supplyConsecutive") String supplyConsecutive,
            @RequestParam(value = "urgencyConsecutive", required = false) String urgencyConsecutive,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mapping = "";
        if (urgencyConsecutive != null) {
            urgencyService.removeSupply(urgencyConsecutive, supplyConsecutive);
            mapping = "redirect:/attend-patient?urgencyConsecutive=" + urgencyConsecutive;

        } else {
            redirectAttributes.addFlashAttribute("error", "invalid post to remove-supply");
            mapping = "redirect:/";
        }
        return mapping;
    }

    @GetMapping(value = "/add-supply")
    public String addSupply(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        String mapping = "supply/add_supply";

        model.addAttribute("supply", new Supply());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("medicines", medicineRepository.findAll());
        model.addAttribute("addressRequest", "/add-supply");
        mapping = "supply/add_supply";

        return mapping;
    }

    @PostMapping(value = "/add-supply")
    public String addSupply(Model model, @Validated @ModelAttribute(value = "supply") Supply supply,
            BindingResult bindingResult, @RequestParam(value = "action", required = false) String action) {

        String mapping = "";

        if (action.toLowerCase().equals("cancel")) {
            mapping = "redirect:/";
        } else if (bindingResult.getErrorCount() > 0) {
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("addressRequest", "/add-supply");
            mapping = "supply/add_supply";
        } else {

            switch (action.toLowerCase()) {
            case "accept": {

                if (supplyService.existSupply(supply.getConsecutive())) {
                    model.addAttribute("patients", patientRepository.findAll());
                    model.addAttribute("medicines", medicineRepository.findAll());
                    model.addAttribute("addressRequest", "/add-supply");
                    model.addAttribute("existError",
                            "An supply with consecutive: " + supply.getConsecutive() + " already exists, change it!");
                    mapping = "supply/add_supply";
                } else {

                    try {
                        supplyService.addSupply(supply);
                        mapping = "redirect:/";

                    } catch (Exception e) {
                        model.addAttribute("patients", patientRepository.findAll());
                        model.addAttribute("medicines", medicineRepository.findAll());
                        model.addAttribute("addressRequest", "/add-supply");
                        model.addAttribute("quantityError", e.getMessage());
                        mapping = "supply/add_supply";

                    }
                }

                break;
            }
            }

        }

        return mapping;
    }

    @GetMapping(value = "/attend")
    public String attendPatient(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mapping = "";

        UrgencyAttention urgency = new UrgencyAttention();

        urgency.setSupplies(new ArrayList<>());
        urgency.setForwarded(true);
        redirectAttributes.addFlashAttribute("urgency", urgency);
        mapping = "redirect:/attend-patient";

        return mapping;
    }

    @GetMapping(value = "/attend-patient")
    public String urgencyForm(Model model, @ModelAttribute(value = "urgency") UrgencyAttention urgency,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mapping = "";

        if (urgency == null || urgency.getSupplies() == null || urgency.getForwarded() == null) {
            redirectAttributes.addFlashAttribute("error", "An error has ocurred while trying to attend a patient");
            mapping = "redirect:/";
        } else {
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("urgency", urgency);
            mapping = "urgency/add_urgency";
        }

        return mapping;
    }

    @PostMapping("/attend-patient")
    public String createUrgency(Model model,
            @Validated @ModelAttribute(value = "urgency", binding = true) UrgencyAttention attention,
            BindingResult bindingResult, RedirectAttributes redirectAttributes,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "removeValue", name = "removeValue", required = false) String removeValue) {

        String mapping = "";


        if (action != null && action.toLowerCase().equals("cancel")) {
            mapping = "redirect:/";
        } else if (bindingResult.getErrorCount() > 0) {
            attention.setSupplies(new ArrayList<>());
            attention.setForwarded(true);
            model.addAttribute("patients", patientRepository.findAll());
            model.addAttribute("urgency", attention);
            mapping = "urgency/add_urgency";
        } else {

            List<Supply> correctSupplies = clearNulls(attention.getSupplies());

            attention.setSupplies(correctSupplies);
            // REVIEW: Cuando action es != Null es porque o se va a agregar un suministro,
            // cancelar la atencion
            // o crear la atencion
            if (action != null) {
                switch (action.toLowerCase()) {
                case "add": {

                    redirectAttributes.addFlashAttribute("urgency", attention);

                    mapping = "redirect:/add-supply-to-urgency";
                    break;
                }
                case "accept": {
                    prepareToUpdate(attention);

                    if (urgencyService.existAttention(attention.getConsecutive())) {
                        model.addAttribute("error", "An attention with consecutive: " + attention.getConsecutive()
                                + " already exists, change it!");
                        model.addAttribute("patients", patientRepository.findAll());
                        mapping = "urgency/add_urgency";
                    } else {

                        try {
                            urgencyService.addAttention(attention);
                            mapping = "redirect:/";

                        } catch (Exception e) {
                            model.addAttribute("error", e.getMessage());
                            model.addAttribute("patients", patientRepository.findAll());
                            mapping = "urgency/add_urgency";
                        }

                    }
                    break;
                }
                }
            } else if (removeValue != null) {
                int indexToRemove = -1;

                // REVIEW: Remove nulls from list

                for (int i = 0; i < correctSupplies.size(); i++) {
                    String consecutive = correctSupplies.get(i).getConsecutive();

                    if (consecutive.trim().equals(removeValue.trim())) {
                        indexToRemove = i;
                    }
                }

                attention.setSupplies(correctSupplies);

                if (indexToRemove == -1) {
                    model.addAttribute("error",
                            "A supply with consecutive: " + removeValue + " doesn't exist for this attention");
                } else {
                    attention.getSupplies().remove(indexToRemove);
                }

                model.addAttribute("patients", patientRepository.findAll());
                model.addAttribute("urgency", attention);

                mapping = "urgency/add_urgency";

            } else{
                attention.setSupplies(new ArrayList<>());
                attention.setForwarded(true);
                model.addAttribute("patients", patientRepository.findAll());
                model.addAttribute("urgency", attention);
            }
        }

        return mapping;
    }

    private List<Supply> clearNulls(List<Supply> supplies) {
        List<Supply> correctSupplies = new ArrayList<Supply>();
        for (int i = 0; i < supplies.size(); i++) {

            if (supplies.get(i) != null) {
                correctSupplies.add(supplies.get(i));
            }

        }

      
        return correctSupplies;
    }

    @GetMapping(value = "/add-supply-to-urgency")
    public String addSupplyToUrgency(Model model, @ModelAttribute(value = "urgency") UrgencyAttention urgency,
            RedirectAttributes redirectAttributes, HttpServletRequest request) {
        String mapping = "";

        Supply supply = new Supply();
        supply.setPatient(urgency.getPatient());
        supply.setDate(urgency.getDate());
        model.addAttribute("urgency", urgency);
        model.addAttribute("supply", supply);
        model.addAttribute("medicines", medicineRepository.findAll());
        model.addAttribute("addressRequest", "/add-supply-to-urgency");
        mapping = "supply/add_supply";

        return mapping;
    }

    @PostMapping(value = "/add-supply-to-urgency")
    public String addSupplyToUrgency(Model model, @Validated @ModelAttribute(value = "supply") Supply supply,
            BindingResult bindingResult, @ModelAttribute UrgencyAttention urgency,
            RedirectAttributes redirectAttributes, @RequestParam(value = "action", required = false) String action) {
        String mapping = "";

        if (action.toLowerCase().equals("cancel")) {
            redirectAttributes.addFlashAttribute("urgency",urgency);
            mapping = "redirect:/attend-patient";

        } else if (bindingResult.getErrorCount() > 0) {
            model.addAttribute("urgency", urgency);
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("addressRequest", "/add-supply-to-urgency");
            mapping = "supply/add_supply";
        } else {

            switch (action.toLowerCase()) {
            case "accept": {

                // REVIEW: Validate ya usa el modelo para enviar a la vista.
                if (validateSupply(model, urgency, supply)) {
                    // REVIEW: Accept add required attributes to model or redirectAttributes
                    mapping = acceptSupply(urgency, redirectAttributes, supply, model);
                } else {
                    mapping = "supply/add_supply";
                }
            }
            }

        }

        return mapping;
    }

    private boolean validateSupply(Model model, UrgencyAttention urgency, Supply supply) {

        boolean validated = true;

        validated = validateNonExist(model, urgency, supply);
        if (validated) {
            validated = validateToSupply(model, urgency, supply);
        }

        return validated;
    }

    private boolean validateNonExist(Model model, UrgencyAttention urgency, Supply supply) {
        boolean validated = true;

        validated = !supplyService.existSupply(supply.getConsecutive());

        if (validated) {
            List<Supply> supplies = urgency.getSupplies();
            int size = supplies.size();
            for (int i = 0; i < size && validated; i++) {
                Supply auxSupply = supplies.get(i);
                if (auxSupply != null) {
                    validated = !auxSupply.getConsecutive().equals(supply.getConsecutive());
                }
            }
        }

        if (!validated) {
            model.addAttribute("existError",
                    "the current urgency already has an supply with consecutive" + supply.getConsecutive());
            model.addAttribute("urgency", urgency);
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("addressRequest", "/add-supply-to-urgency");
        }

        return validated;
    }

    private boolean validateToSupply(Model model, UrgencyAttention urgency, Supply supply) {
        boolean validated = true;
        List<Supply> supplies = urgency.getSupplies();
        int size = supplies.size();
        int totalToSupply = 0;

        for (int i = 0; i < size; i++) {
            if (supplies.get(i) != null
                    && supplies.get(i).getMedicine().getConsecutive().equals(supply.getMedicine().getConsecutive())) {

                totalToSupply += supplies.get(i).getQuantity();
            }
        }

        int totalOnInventary = inventaryService.getTotalInventaryMedicine(supply.getMedicine().getConsecutive());
        validated = totalToSupply + supply.getQuantity() <= totalOnInventary;

        if (!validated) {
            model.addAttribute("quantityError", "quantity must be equal or smaller than available quantity:"
                    + totalOnInventary + " in inventary\n" + "anothers supplies was added to the total");
            model.addAttribute("urgency", urgency);
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("addressRequest", "/add-supply-to-urgency");
        }

        return validated;

    }

    private String acceptSupply(UrgencyAttention urgency, RedirectAttributes redirectAttributes, Supply supply,
            Model model) {

        String mapping = "";
        int totalOnInventary = inventaryService.getTotalInventaryMedicine(supply.getMedicine().getConsecutive());
        if (supply.getQuantity() > totalOnInventary) {
            model.addAttribute("quantityError",
                    "quantity must be equal or smaller than available quantity:" + totalOnInventary + " in inventary");
            model.addAttribute("urgency", urgency);
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("addressRequest", "/add-supply-to-urgency");
            mapping = "supply/add_supply";

        } else if (supplyService.existSupply(supply.getConsecutive())) {

            model.addAttribute("error",
                    "An supply with consecutive: " + supply.getConsecutive() + " already exists, change it!");
            model.addAttribute("urgency", urgency);
            model.addAttribute("medicines", medicineRepository.findAll());
            model.addAttribute("addressRequest", "/add-supply-to-urgency");
            mapping = "supply/add_supply";

        } else {

            urgency.getSupplies().add(supply);
            redirectAttributes.addFlashAttribute("urgency", urgency);

            mapping = "redirect:/attend-patient";

        }
        return mapping;
    }

    private void prepareToUpdate(UrgencyAttention urgency) {
        for (Supply supply : urgency.getSupplies()) {
            supply.setPatient(urgency.getPatient());
            supply.setDate(urgency.getDate());
        }
    }

  

}