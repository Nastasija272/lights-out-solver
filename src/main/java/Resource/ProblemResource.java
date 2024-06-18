package Resource;

import Model.Message;
import Model.Problem;
import Repository.PlayerRepository;
import Repository.ProblemRepository;
import Repository.SolutionRepository;
import Repository.SolutionStepRepository;
import Repository.SolverRepository;

import java.util.List;
import java.util.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/problems")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ProblemResource {

    @Inject
    ProblemRepository pRepo;
    @Inject
    SolutionRepository sRepo;
    @Inject
    SolutionStepRepository sStepRepo;

    @GET
    public List<Problem> getAllProblems() {
        return pRepo.findAll();
    }

    @GET
    @Path("/{id}")
    public Problem getProblemById(@PathParam("id") Long id) {
        return pRepo.findById(id);
    }

    @GET
    @Path("/creator/{username}")
    public List<Problem> getProblemsByCreator(@PathParam("username") String username) {
        return pRepo.findByCreatorUsername(username);
    }

    @POST
    public Response createProblem(Problem problem) {        
    	SolverRepository solver = new SolverRepository(pRepo, sRepo, sStepRepo);
        Message mssg = solver.getSolution(problem, problem.getPlayer(), null);
        return Response.ok(mssg).status(Response.Status.CREATED).build();
    }
}
