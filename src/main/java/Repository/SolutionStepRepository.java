package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import Model.SolutionStep;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SolutionStepRepository {

    @PersistenceContext
    private EntityManager em;

    public List<SolutionStep> findAll() {
        return em.createQuery("SELECT ss FROM SolutionStep ss", SolutionStep.class).getResultList();
    }

    public SolutionStep findById(Long id) {
        return em.find(SolutionStep.class, id);
    }

    public List<SolutionStep> findBySolutionId(Long solutionId) {
        return em.createQuery("SELECT ss FROM SolutionStep ss WHERE ss.solution.id = :solutionId", SolutionStep.class)
                 .setParameter("solutionId", solutionId)
                 .getResultList();
    }

    @Transactional
    public void save(SolutionStep solutionStep) {
        em.merge(solutionStep);
    }
}
