package graphTool;

import com.fasterxml.jackson.core.type.TypeReference;

import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The NodeResource class will handle the outward facing api.
 * Possibly utilize Swagger
 */
@Path("/nodes")
public class NodeResource {

    DbOps dbOps;
    String path = "\\C:\\Neo4J";

    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @Path("/observation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createObservation(Json propsJson) {
        dbOps = new DbOps(path);
        new TypeReference<Map<String, String>>(){};
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            HashMap<String, Object> props = mapper.readValue(propsJson.toString(), new TypeReference<Map<String, Object>>(){});
//            dbOps.createObservation(props);
//
//            //return Response.status(201).entity(NodeResource.class).build();
//        }
//        catch (Exception e) {
//            //return Response.status(400).entity(e.getMessage()).build();
//        }
    }

    @Path("/observation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readObservations(){
        dbOps = new DbOps(path);
        try {
            HashMap<String, HashMap<String, Object>> observations = dbOps.getAllObservations();
            dbOps.close();
            return Response.status(200).entity(observations).build();
        }
        catch (Exception e){
            System.out.println("Error msg : " + e.getMessage());
            dbOps.close();
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @Path("/observation/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readObservation(@PathParam("id") String id) {
        dbOps = new DbOps(path);
        try {
            HashMap<String, Object> observation = dbOps.getObservation(id);
            dbOps.close();
            return Response.status(200).entity(observation).build();
        }
        catch (Exception e){
            System.out.println("Error msg : " + e.getMessage());
            dbOps.close();
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

//    @Path("/observation/{id}")
//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void updateObservation(@FormParam("id") String id, HashMap<String, Object> props) {
//        dbOps.updateObservation(id, props);
//    }

    @Path("/observation/{id}")
    @DELETE
    public Response deleteObservation(@HeaderParam("id") String id) {
        dbOps = new DbOps(path);
        try {
            dbOps.deleteObservation(id);
            dbOps.close();
            return Response.status(200).entity(NodeResource.class).build();
        }
        catch (Exception e){
            dbOps.close();
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
//
//    @Path("/knowledge")
//    @POST
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
//    public void createKnowledge(String id, HashMap<String, Object> props) {
//        dbOps.createKnowledge(id, props);
//    }
//
    @Path("/knowledge")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readKnowledges(){
        dbOps = new DbOps(path);
        try {
            HashMap<String, HashMap<String, Object>> knowledges = dbOps.getAllKnowledges();
            dbOps.close();
            return Response.status(200).entity(knowledges).build();
        }
        catch (Exception e){
            dbOps.close();
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @Path("/knowledge/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readKnowledge(@PathParam("id") String id) {
        dbOps = new DbOps(path);
        try {
            HashMap<String, Object> knowledge = dbOps.getKnowledge(id);
            dbOps.close();
            return Response.status(200).entity(knowledge).build();
        }
        catch (Exception e){
            dbOps.close();
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
//
//    @Path("/knowledge/{id}")
//    @PUT
//    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
//    public void updateKnowledge(@PathParam("id") String id, HashMap<String, Object> props) {
//        dbOps.updateKnowledge(id, props);
//    }
//
    @Path("/knowledge/{id}")
    @DELETE
    public Response deleteKnowledge(@PathParam("id") String id) {
        dbOps = new DbOps(path);
        try {
            dbOps.deleteKnowledge(id);
            dbOps.close();
            return Response.status(200).entity(NodeResource.class).build();
        }
        catch (Exception e){
            dbOps.close();
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
