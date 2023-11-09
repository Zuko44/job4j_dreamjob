package ru.job4j.dreamjob.model.vacancy;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Vacancy;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryVacancyRepository implements VacancyRepository {
    /**
     * private static final MemoryVacancyRepository INSTANCE = new MemoryVacancyRepository();
     */
    private final Map<Integer, Vacancy> vacancies = new ConcurrentHashMap<>();
    /**
     * private int nextId = 1;
     */
    private final AtomicInteger nextId = new AtomicInteger(0);

    private MemoryVacancyRepository() {
        save(new Vacancy(0, "Intern Java Developer",
                "Fresh meat is urgently needed, interns - work quickly!", LocalDateTime.now()));
        save(new Vacancy(0, "Junior Java Developer",
                "Young padawan needed to work for pennies", LocalDateTime.now()));
        save(new Vacancy(0, "Junior+ Java Developer",
                "not a young padawan is required to work for pennies", LocalDateTime.now()));
        save(new Vacancy(0, "Middle Java Developer",
                "A dynamically developing gallery needs an experienced proger", LocalDateTime.now()));
        save(new Vacancy(0, "Middle+ Java Developer",
                "we'll hire you for cookies", LocalDateTime.now()));
        save(new Vacancy(0, "Senior Java Developer",
                "ok, so be it, let's add coffee to the agreement", LocalDateTime.now()));
    }

    /**
     * public static MemoryVacancyRepository getInstance() {
     * return INSTANCE;
     * }
     */

    @Override
    public Vacancy save(Vacancy vacancy) {
        /**vacancy.setId(nextId++);
         vacancies.put(vacancy.getId(), vacancy);*/
        vacancy.setId(nextId.incrementAndGet());
        vacancies.putIfAbsent(vacancy.getId(), vacancy);
        return vacancy;
    }

    @Override
    public void deleteById(int id) {
        vacancies.remove(id);
    }

    @Override
    public boolean update(Vacancy vacancy) {
        return vacancies.computeIfPresent(vacancy.getId(), (id, oldVacancy) -> new Vacancy(oldVacancy.getId(),
                vacancy.getTitle(), vacancy.getDescription(), vacancy.getCreationDate())) != null;
    }

    @Override
    public Optional<Vacancy> findById(int id) {
        return Optional.ofNullable(vacancies.get(id));
    }

    @Override
    public Collection<Vacancy> findAll() {
        return vacancies.values();
    }
}
