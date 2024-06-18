package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import Model.Player;
import Model.Problem;
import Resource.ProblemResource;

import java.util.List;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ProblemRepository {
    private static final Logger log = Logger.getLogger(ProblemRepository.class.getName());

    @PersistenceContext
    private EntityManager em;
    
    public List<Problem> findAll() {
        Query query = em.createNativeQuery("SELECT * FROM Problem", Problem.class);
        List<Problem> problems = query.getResultList();
        return problems;
    }
    
    public Problem findById(Long id) {
        return em.find(Problem.class, id);
    }

    public List<Problem> findByCreatorUsername(String username) {
    	Query query = em.createQuery("SELECT p FROM Problem p join fetch p.player pr WHERE pr.username = :username", Problem.class);
    	query.setParameter("username", username);
    	List<Problem> problems = query.getResultList();
        return problems;
    }
    

   
    @Transactional    
    public void save(Problem problem) {
        if (problem.getPlayer() != null) {
        	log.info("checkpoint:"+problem.getPlayer().getId());
        	if(problem.getPlayer().getId() != null){
        		// Player already exists in the database, set the player in the problem
                Player existingPlayer = em.find(Player.class, problem.getPlayer().getId());
                if (existingPlayer != null) {
                    problem.setPlayer(existingPlayer);
                } else {
                    // player with given ID not found
                    em.merge(problem.getPlayer());
                }
            }else {
            	em.persist(problem.getPlayer());
            }
            
            if(problem.getId() != null) {
            	problem.setId(null);
            }
            // Player does not exist in the database, save the player first
            em.persist(problem);
        }
    }
    
    

}
