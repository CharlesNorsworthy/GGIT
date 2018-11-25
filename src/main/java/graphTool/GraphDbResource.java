package graphTool;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/db")
public class GraphDbResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String GetGraphDatabasePath(){
        return null;
    }

}
