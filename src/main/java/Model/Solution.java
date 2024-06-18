package Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "solution")

public class Solution {
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	 	@Column(name = "idsolution", nullable = false)
	    private Long id;
	    
	    @ManyToOne
	    @JoinColumn(name = "idproblem", nullable = false)
	    private Problem problem;
	    
	 	@Column(name = "solution_description", nullable = false)
	    private String description;
	    
	    @ManyToOne
	    @JoinColumn(name = "idplayer", nullable = false)
	    private Player player;
	    
	    // Geter za id
	    public Long getId() {
	    	return id;
	    }
	    // Setter za id
	    public void setId(Long id) {
	        this.id = id;
	    }

	    // Getter za problem
	    public Problem getProblem() {
	        return problem;
	    }

	    // Setter za problem
	    public void setProblem(Problem problem) {
	        this.problem = problem;
	    }
	    
	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    // Getter za player
	    public Player getPlayer() {
	        return player;
	    }

	    // Setter za player
	    public void setPlayer(Player player) {
	        this.player = player;
	    }
	    
}
