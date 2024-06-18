package Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import Model.Player;

@ApplicationScoped
public class PlayerRepository {
    @Inject
    EntityManager em;

   
    public List<Player> findAll() {
        Query query = em.createNativeQuery("SELECT * FROM Player", Player.class);
        List<Player> players = query.getResultList();
        return players;
    }


    public List<Player> findByUsername(String username) {
    	Query query = em.createNativeQuery("SELECT * FROM Player WHERE username = :username", Player.class);
    	query.setParameter("username", username);
        List<Player> players = query.getResultList();
        return players;
    }

    @Transactional
    public void save(Player player) {
        em.merge(player);
    }
}

