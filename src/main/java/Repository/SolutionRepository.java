package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import Model.Problem;
import Model.Solution;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SolutionRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Solution> findAll() {
        Query query = em.createNativeQuery("SELECT * FROM Solution", Solution.class);
        List<Solution> solutions = query.getResultList();
        return solutions;
    }

    public Solution findById(Long id) {
        return em.find(Solution.class, id);
    }

    public List<Solution> findBySolverUsername(String username) {
        return em.createQuery("SELECT s FROM Solution s WHERE s.player.username = :username", Solution.class)
                 .setParameter("username", username)
                 .getResultList();
    }

    public List<Solution> findByProblemId(Long problemId) {
        return em.createQuery("SELECT s FROM Solution s WHERE s.problem.id = :problemId", Solution.class)
                 .setParameter("problemId", problemId)
                 .getResultList();
    }

    @Transactional
    public void save(Solution solution) {
        em.persist(solution);
    }
}
