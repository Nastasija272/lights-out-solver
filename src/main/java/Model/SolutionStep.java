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
@Table(name = "solution_step")

public class SolutionStep {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsolutionsstep")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "idsolution", nullable = true)
    private Solution solution;
    
    @Column(name = "move_order")
    private int moveOrder;
    
    @Column(name = "move", nullable = false)
    private String move;  // New field for the move column
    
    // Geter za id
    public Long getId() {
    	return id;
    }
    // Setter za id
    public void setId(Long id) {
        this.id = id;
    }
    // Getter za solution
    public Solution getSolution() {
        return solution;
    }

    // Setter za solution
    public void setSolution(Solution solution) {
        this.solution = solution;
    }

    // Getter za moveOrder
    public int getMoveOrder() {
        return moveOrder;
    }

    // Setter za moveOrder
    public void setMoveOrder(int moveOrder) {
        this.moveOrder = moveOrder;
    }
    // Getter for move
    public String getMove() {
        return move;
    }

    // Setter for move
    public void setMove(String move) {
        this.move = move;
    }
}
