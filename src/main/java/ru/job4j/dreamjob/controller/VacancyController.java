package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.service.VacancyService;

import java.util.Optional;

@Controller
@RequestMapping("/vacancies")
public class VacancyController {
    /**private final VacancyRepository vacancyRepository = MemoryVacancyRepository.getInstance();*/
    /**
     * private final VacancyService vacancyService = SimpleVacancyService.getInstance();
     */
    private final VacancyService vacancyService;

    public VacancyController(VacancyService vacancyService) {
        this.vacancyService = vacancyService;
    }

    @GetMapping
    public String getAll(Model model) {
        /**model.addAttribute("vacancies", vacancyRepository.findAll());*/
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage() {
        return "vacancies/create";
    }

    /**
     * @PostMapping("/create") public String create(HttpServletRequest request) {
     * var title = request.getParameter("title");
     * var description = request.getParameter("description");
     * vacancyRepository.save(new Vacancy(0, title, description, LocalDateTime.now()));
     * return "redirect:/vacancies";
     * }
     */

    @PostMapping("/create")
    public String create(@ModelAttribute Vacancy vacancy) {
        /**vacancyRepository.save(vacancy);*/
        vacancyService.save(vacancy);
        return "redirect:/vacancies";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        /**var vacancyOptional = vacancyRepository.findById(id);
         if (vacancyOptional.isEmpty()) {
         model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
         return "errors/404";
         }
         model.addAttribute("vacancy", vacancyOptional.get());*/
        var vacancyOptional = vacancyService.findById(id);
        if (vacancyOptional.isEmpty()) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        model.addAttribute("vacancy", vacancyOptional.get());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, Model model) {
        /**var isUpdated = vacancyRepository.update(vacancy);
         if (!isUpdated) {
         model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
         return "errors/404";
         }*/
        var isUpdated = vacancyService.update(vacancy);
        if (!isUpdated) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        /**vacancyRepository.deleteById(id);
         Optional<Vacancy> isDeleted = vacancyRepository.findById(id);*/
        vacancyService.deleteById(id);
        Optional<Vacancy> isDeleted = vacancyService.findById(id);
        if (isDeleted.isEmpty()) {
            model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
            return "errors/404";
        }
        return "redirect:/vacancies";
    }
}
