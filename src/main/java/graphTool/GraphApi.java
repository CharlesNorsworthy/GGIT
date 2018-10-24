package graphTool;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;

/**
 * The GraphApi class will handle the outward facing api.
 * Possibly utilze Swagger
 */
@Path("/graphTool")
public class GraphApi {

    DbOps dbOps;
    String path;

    public GraphApi(){
        path = "\\C:\\Neo4J";
        dbOps = new DbOps(path);
    }

//    @Path("/observation")
//    @POST
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
//    public void createObservation(HashMap<String, Object> props) {
//        dbOps.createObservation(props);
//    }

    @Path("/observation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, HashMap<String, Object>> readObservations(){
        return (dbOps.readAllObservations());
    }

    @Path("/observation/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, Object> readObservation(@PathParam("id") String id) {
        return (dbOps.readObservation(id));
    }

//    @Path("/observation/{id}")
//    @PUT
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
//    public void updateObservation(@PathParam("id") String id, HashMap<String, Object> props) {
//        dbOps.updateObservation(id, props);
//    }
//
//    @Path("/observation/{id}")
//    @DELETE
//    public void deleteObservation(@PathParam("id") String id) {
//        dbOps.deleteObservation(id);
//    }
//
//    @Path("/knowledge")
//    @POST
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
//    public void createKnowledge(String id, HashMap<String, Object> props) {
//        dbOps.createKnowledge(id, props);
//    }
//
//    @Path("/knowledge")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public HashMap<String, HashMap<String, Object>> readKnowledges(){
//        return (dbOps.readAllKnowledges());
//    }
//
//    @Path("/knowledge/{id}")
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public HashMap<String, Object> readKnowledge(@PathParam("id") String id) {
//        return (dbOps.readKnowledge(id));
//    }
//
//    @Path("/knowledge/{id}")
//    @PUT
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
//    public void updateKnowledge(@PathParam("id") String id, HashMap<String, Object> props) {
//        dbOps.updateKnowledge(id, props);
//    }
//
//    @Path("/knowledge/{id}")
//    @DELETE
//    public void deleteKnowledge(@PathParam("id") String id) {
//        dbOps.deleteObservation(id);
//    }
}
