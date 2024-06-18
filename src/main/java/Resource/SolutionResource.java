package Resource;

import Model.Message;
import Model.Player;
import Model.Problem;
import Model.Solution;
import Repository.ProblemRepository;
import Repository.SolutionRepository;
import Repository.SolutionStepRepository;
import Repository.SolverRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.logging.Logger;

@Path("/solutions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SolutionResource {
    private static final Logger log = Logger.getLogger(SolutionResource.class.getName());

    @Inject
    ProblemRepository pRepo;
    @Inject
    SolutionRepository sRepo;
    @Inject
    SolutionStepRepository sStepRepo;

    @GET
    public List<Solution> getAllSolutions() {
        return sRepo.findAll();
    }

    @GET
    @Path("/solver/{username}")
    public List<Solution> getSolutionsBySolver(@PathParam("username") String username) {
        return sRepo.findBySolverUsername(username);
    }

    @GET
    @Path("/problem/{id}")
    public List<Solution> getSolutionsByProblem(@PathParam("id") Long id) {
        return sRepo.findByProblemId(id);
    }

    @POST
    @Transactional
    public Response createSolution(Solution solution) {
    	Problem problem = pRepo.findById(solution.getProblem().getId());
    	
    	SolverRepository solver = new SolverRepository(pRepo, sRepo, sStepRepo);
        Message mssg = solver.getSolution(problem, problem.getPlayer(), solution);
        return Response.ok(mssg).status(Response.Status.CREATED).build();
    }
}
