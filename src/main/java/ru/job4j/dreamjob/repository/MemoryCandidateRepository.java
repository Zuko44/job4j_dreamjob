package ru.job4j.dreamjob.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class MemoryCandidateRepository implements CandidateRepository {
    /**
     * private static final MemoryCandidateRepository INSTANCE = new MemoryCandidateRepository();
     */
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    /**
     * private int nextId = 1;
     */
    private final AtomicInteger nextId = new AtomicInteger(0);

    private MemoryCandidateRepository() {
        save(new Candidate(0, "Intern Antilochus",
                "I’ll get a job for food (it’s possible without food)", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Junior Guidon",
                "very young and green", LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Junior+ Evlampius",
                "seasoned Jun. I can even do something.", LocalDateTime.now(), 3, 0));
        save(new Candidate(0, "Middle Kapiton",
                "king of the market. the coveted shot.", LocalDateTime.now(), 1, 0));
        save(new Candidate(0, "Middle+ Pavsikaky",
                "rates are rising.", LocalDateTime.now(), 2, 0));
        save(new Candidate(0, "Senior Satyr",
                "what should I add here? I'm good.", LocalDateTime.now(), 3, 0));
    }

    /**
     * public static MemoryCandidateRepository getInstance() {
     * return INSTANCE;
     * }
     */

    @Override
    public Candidate save(Candidate candidate) {
        /**candidate.setId(nextId++);
         candidates.put(candidate.getId(), candidate);*/
        candidate.setId(nextId.incrementAndGet());
        candidates.putIfAbsent(candidate.getId(), candidate);
        return candidate;
    }

    @Override
    public void deleteById(int id) {
        candidates.remove(id);
    }

    @Override
    public boolean update(Candidate candidate) {
        return candidates.computeIfPresent(candidate.getId(), (id, oldVacancy) -> new Candidate(oldVacancy.getId(),
                candidate.getName(), candidate.getDescription(), candidate.getCreationDate(), candidate.getCityId(),
                candidate.getFileId())) != null;
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
