package Repository;

import Model.Message;
import Model.Player;
import Model.Problem;
import Model.Solution;
import Model.SolutionStep;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SolverRepository {
    private static final Logger log = Logger.getLogger(SolverRepository.class.getName());

    @PersistenceContext
    private EntityManager em;
    
    ProblemRepository pRepo;
    SolutionRepository sRepo;
    SolutionStepRepository sStepRepo;
	
    @Inject
    public SolverRepository(ProblemRepository pRepo,
                  SolutionRepository sRepo,
                  SolutionStepRepository sStepRepo) {
        this.pRepo = pRepo;
        this.sRepo = sRepo;
        this.sStepRepo = sStepRepo;
    }
    
	// Method to print the steps
    private void printSteps() {
        for (SolutionStep step : steps) {
            System.out.println("Move Order: " + step.getMoveOrder() + ", Move: " + step.getMove());
        }
    }
    
	private List<SolutionStep> steps = new LinkedList<>();
	private int counter = 0;
	
	public Message getSolution(Problem problem, Player player, Solution sObject) {
		
		Message mssg = new Message();
		
		log.info("get solution");
		String desc = problem.getDescription();
		
		desc = desc.replace("[", "");
		desc = desc.replace("]", "");
		String[] stringArray = desc.split(",\\s*"); 
        
        int[] initialState = new int[stringArray.length];
        
        for (int i = 0; i < stringArray.length; i++) {
        	initialState[i] = Integer.parseInt(stringArray[i]);
        }
        
        int length = initialState.length;
        int n = (int) Math.sqrt(length);
        
        System.out.print("n ="+ n);
        
        for (int i : initialState) {
            System.out.print(i + " ");
        }
		
        int[] solution = solveLightsOut(initialState, n);
        
        System.out.print("SOLUTION");
        for (int i : solution) {
            System.out.print(i + " ");
        }
        if (solution == null) {
            System.out.println("No solution is possible for the given initial state.");
            mssg.setMessage("No solution is possible for the given initial state.");
            return mssg;
        }
        
        if(sObject == null) {
            pRepo.save(problem);
            mssg.setMessage("Problem saved");
            return mssg;
        }
        
        System.out.println("Steps taken to solve:");
        
        String descSol = sObject.getDescription();
		
        descSol = descSol.replace("[", "");
        descSol = descSol.replace("]", "");
		String[] descSolArray = descSol.split(",\\s*"); 
        
		boolean correct = true;
        for (int i = 0; i < descSolArray.length; i++) {
        	if(Integer.parseInt(descSolArray[i]) != solution[i]) {
        		correct = false;
        		break;
        	}
        }
        
        if(!correct) {
        	mssg.setMessage("Solution not ok");
            return mssg;
        }
        
    	StringBuilder finalStateStr = new StringBuilder();
    	for (int tmpi = 0; tmpi < solution.length; tmpi++) {
    	    finalStateStr.append(solution[tmpi]);
    	    if (tmpi < solution.length - 1) {
    	        finalStateStr.append(", ");
    	    }
    	}
    	if(sObject.getId() != null) {
    		sObject.setId(null);
        }
        
    	sRepo.save(sObject);
    	
        //new solution
        int[] finalState = applySolution(initialState, solution, n, true);
        
        for (SolutionStep step : steps) {
        	step.setSolution(sObject);
        	sStepRepo.save(step);
        }
        
        printSteps();
        mssg.setMessage("Solution saved");
        return mssg;
	}

    public int index(int n, int i, int j) {
        return i * n + j;
    }

    public int[][] toggleMatrix(int n) {
        int[][] T = new int[n * n][n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int idx = index(n, i, j);
                T[idx][idx] = 1;
                if (i > 0) {
                    T[idx][index(n, i - 1, j)] = 1;
                }
                if (i < n - 1) {
                    T[idx][index(n, i + 1, j)] = 1;
                }
                if (j > 0) {
                    T[idx][index(n, i, j - 1)] = 1;
                }
                if (j < n - 1) {
                    T[idx][index(n, i, j + 1)] = 1;
                }
            }
        }
        return T;
    }

    public int[] gaussianElimination(int[][] A, int[] b) {
        int n = A.length;
        int m = A[0].length;
        int[][] Ab = new int[n][m + 1];

        for (int i = 0; i < n; i++) {
            System.arraycopy(A[i], 0, Ab[i], 0, m);
            Ab[i][m] = b[i];
        }

        for (int col = 0; col < m; col++) {
            int pivot = -1;
            for (int row = col; row < n; row++) {
                if (Ab[row][col] == 1) {
                    pivot = row;
                    break;
                }
            }
            if (pivot == -1) {
                continue;
            }
            if (pivot != col) {
                int[] temp = Ab[col];
                Ab[col] = Ab[pivot];
                Ab[pivot] = temp;
            }

            for (int row = 0; row < n; row++) {
                if (row != col && Ab[row][col] == 1) {
                    for (int k = 0; k < m + 1; k++) {
                        Ab[row][k] = (Ab[row][k] + Ab[col][k]) % 2;
                    }
                }
            }
        }

        int[] solution = new int[m];
        for (int i = 0; i < m; i++) {
            if (Ab[i][i] == 1) {
                solution[i] = Ab[i][m];
            }
        }

        for (int row = 0; row < n; row++) {
            if (Ab[row][row] == 0 && Ab[row][m] == 1) {
                return null;
            }
        }

        return solution;
    }

    public int[] solveLightsOut(int[] initialState, int n) {
        int[][] T = toggleMatrix(n);

        int[] targetState = new int[initialState.length];
        Arrays.fill(targetState, 1);
        int[] adjustedInitialState = new int[initialState.length];
        for (int i = 0; i < initialState.length; i++) {
            adjustedInitialState[i] = (initialState[i] + targetState[i]) % 2;
        }

        int[] solution = gaussianElimination(T, adjustedInitialState);
        if (solution == null) {
            return null;
        }

        return solution;
    }

    public int[] applySolution(int[] initialState, int[] solution, int n, boolean printSteps) {
        int[][] T = toggleMatrix(n);
        int[] finalState = Arrays.copyOf(initialState, initialState.length);
        for (int i = 0; i < solution.length; i++) {
            if (solution[i] == 1) {
                if (printSteps) {
                    System.out.println("Toggling at (" + (i / n) + ", " + (i % n) + ")");
                }
                for (int j = 0; j < T[i].length; j++) {
                    finalState[j] = (finalState[j] + T[i][j]) % 2;
                }
                if (printSteps) {
                	StringBuilder finalStateStr = new StringBuilder();
                	for (int tmpi = 0; tmpi < finalState.length; tmpi++) {
                	    finalStateStr.append(finalState[tmpi]);
                	    if (tmpi < finalState.length - 1) {
                	        finalStateStr.append(", ");
                	    }
                	}
                                        
                    counter++;
                	SolutionStep step = new SolutionStep();
                	step.setMoveOrder(counter);
                	step.setMove("["+finalStateStr.toString()+"]");
                	
                	
                	steps.add(step);
                    printMatrix(finalState, n);
                    System.out.println();
                }
            }
        }
        return finalState;
    }

    public void printMatrix(int[] matrix, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(matrix[i * n + j] + " ");
            }
            System.out.println();
        }
    }
}
