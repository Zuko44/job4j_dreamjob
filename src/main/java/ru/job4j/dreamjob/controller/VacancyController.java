package ru.job4j.dreamjob.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dto.FileDto;
import ru.job4j.dreamjob.model.Vacancy;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.VacancyService;

import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/vacancies")
public class VacancyController {
    /**private final VacancyRepository vacancyRepository = MemoryVacancyRepository.getInstance();*/
    /**
     * private final VacancyService vacancyService = SimpleVacancyService.getInstance();
     */
    private final VacancyService vacancyService;
    private final CityService cityService;

    public VacancyController(VacancyService vacancyService, CityService cityService) {
        this.vacancyService = vacancyService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAll(Model model) {
        /**model.addAttribute("vacancies", vacancyRepository.findAll());*/
        model.addAttribute("vacancies", vacancyService.findAll());
        return "vacancies/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        model.addAttribute("cities", cityService.findAll());
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
    public String create(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file, Model model) {
        /**vacancyRepository.save(vacancy);*/

        /**vacancyService.save(vacancy);
         return "redirect:/vacancies";*/
        try {
            vacancyService.save(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            return "redirect:/vacancies";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }
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
        model.addAttribute("cities", cityService.findAll());
        model.addAttribute("vacancy", vacancyOptional.get());
        return "vacancies/one";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Vacancy vacancy, @RequestParam MultipartFile file, Model model) {
        /**var isUpdated = vacancyRepository.update(vacancy);
         if (!isUpdated) {
         model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
         return "errors/404";
         }*/

        /**var isUpdated = vacancyService.update(vacancy);
         if (!isUpdated) {
         model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
         return "errors/404";
         }
         return "redirect:/vacancies";*/
        try {
            var isUpdated = vacancyService.update(vacancy, new FileDto(file.getOriginalFilename(), file.getBytes()));
            if (!isUpdated) {
                model.addAttribute("message", "Вакансия с указанным идентификатором не найдена");
                return "errors/404";
            }
            return "redirect:/vacancies";
        } catch (Exception exception) {
            model.addAttribute("message", exception.getMessage());
            return "errors/404";
        }

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
