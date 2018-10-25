package graphTool;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;

/**
 * The GraphApi class will handle the outward facing api.
 * Possibly utilze Swagger
 */
@Path("/")
public class GraphApi {

    DbOps dbOps;
    String path;

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

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
//    public void updateObservation(@FormParam("id") String id, HashMap<String, Object> props) {
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
