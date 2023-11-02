package ru.job4j.dreamjob.repository;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    /**
     * private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();
     */
    private final Map<Integer, Candidate> candidates = new HashMap<>();
    private int nextId = 1;

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Intern Antilochus",
                "I’ll get a job for food (it’s possible without food)", LocalDateTime.now()));
        save(new Candidate(0, "Junior Guidon",
                "very young and green", LocalDateTime.now()));
        save(new Candidate(0, "Junior+ Evlampius",
                "seasoned Jun. I can even do something.", LocalDateTime.now()));
        save(new Candidate(0, "Middle Kapiton",
                "king of the market. the coveted shot.", LocalDateTime.now()));
        save(new Candidate(0, "Middle+ Pavsikaky",
                "rates are rising.", LocalDateTime.now()));
        save(new Candidate(0, "Senior Satyr",
                "what should I add here? I'm good.", LocalDateTime.now()));
    }

    /**
     * public static MemoryCandidateRepository getInstance() {
     * return INSTANCE;
     * }
     */

    @Override
    public Candidate save(Candidate candidate) {
        candidate.setId(nextId++);
        candidates.put(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public void deleteById(int id) {
        candidates.remove(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldVacancy) -> new Candidate(oldVacancy.getId(),
                candidate.getName(), candidate.getDescription(), candidate.getCreationDate())) != null;
    }

    @Override
    public Optional<Candidate> findById(int id) {
        return Optional.ofNullable(candidates.get(id));
    }

    @Override
    public Collection<Candidate> findAll() {
        return candidates.values();
    }
}
