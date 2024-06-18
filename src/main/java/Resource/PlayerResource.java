package Resource;

import Repository.PlayerRepository;
import Model.Player;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.microprofile.openapi.annotations.Operation;

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


@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PlayerResource {
    private static final Logger LOGGER = Logger.getLogger(PlayerResource.class.getName());

    @Inject
    private PlayerRepository playerRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "GET /players vrne vse shranjene uporabnike")
    public List<Player> getAllPlayers() {
        List<Player> players = playerRepository.findAll();
        return players;
    }
    

    @GET
    @Path("/{username}")
    @Operation(description = "GET /players/{username} vrne uporabnike z username")
    public List<Player> getPlayerByUsername(@PathParam("username") String username) {
        return playerRepository.findByUsername(username);
    }

    @POST
    @Operation(description = "POST /players ustvari novega uporabnika")
    @Transactional
    public Response createPlayer(Player player) {
        playerRepository.save(player);
        return Response.ok(player).status(Response.Status.CREATED).build();
    }
}
