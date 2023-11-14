package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;

import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/candidates")
public class CandidateController {
    /**
     * private final CandidateRepository candidateRepository = MemoryCandidateRepository.getInstance();
     */
    /**
     * private final CandidateService candidateService = SimpleCandidateService.getInstance();
     */
    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public String getAll(Model model) {
        /**model.addAttribute("candidates", candidateRepository.findAll());*/
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates/list";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        try {
            candidateService.save(candidate, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "candidates/create";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        /**var candidateOptional = candidateRepository.findById(id);
         if (candidateOptional.isEmpty()) {
         model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
         return "errors/404";
         }
         model.addAttribute("candidate", candidateOptional.get());*/
        var candidateOptional = candidateService.findById(id);
        if (candidateOptional.isEmpty()) {
            model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
            return "errors/404";
        }
        model.addAttribute("candidate", candidateOptional.get());
        return "candidates/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Candidate candidate, @RequestParam MultipartFile file, Model model) {
        /**var isUpdated = candidateRepository.update(candidate);
         if (!isUpdated) {
         model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
         return "errors/404";
         }*/

        /**var isUpdated = candidateService.update(candidate);
         if (!isUpdated) {
         model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
         return "errors/404";
         }
         return "redirect:/candidates";*/

        try {
            var isUpdated = candidateService.update(candidate, new FileDto(file.getOriginalFilename(),
                    file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
                return "errors/404";
            }
            return "redirect:/candidates";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        /**candidateRepository.deleteById(id);
         Optional<Candidate> isDeleted = candidateRepository.findById(id);*/
        candidateService.deleteById(id);
        Optional<Candidate> isDeleted = candidateService.findById(id);
        if (isDeleted.isEmpty()) {
            model.addAttribute("message", "Резюме с указанным идентификатором не найдено");
            return "errors/404";
        }
        return "redirect:/candidates";
    }
}
