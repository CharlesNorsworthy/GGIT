package graphTool;

import com.fasterxml.jackson.core.type.TypeReference;
//import graphTool.client.RestApp;

import javax.json.Json;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.HashMap;
import java.util.Map;

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
        this.dbOps = new DbOps("\\C:\\Neo4J");
    }

    @Path("/observation")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void createObservation(Json propsJson) {
        new TypeReference<Map<String, String>>(){};
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            HashMap<String, Object> props = mapper.readValue(propsJson.toString(), new TypeReference<Map<String, Object>>(){});
//            dbOps.createObservation(props);
//
//            //return Response.status(201).entity(GraphApi.class).build();
//        }
//        catch (Exception e) {
//            //return Response.status(400).entity(e.getMessage()).build();
//        }
    }

    @Path("/observation")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readObservations(){
        try {
            HashMap<String, HashMap<String, Object>> observations = dbOps.getAllObservations();
            return Response.status(200).entity(observations).build();
        }
        catch (Exception e){
            System.out.println("Error msg : " + e.getMessage());
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @Path("/observation/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readObservation(@PathParam("id") String id) {
        try {
            HashMap<String, Object> observation = dbOps.getObservation(id);
            return Response.status(200).entity(observation).build();
        }
        catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @Path("/observation/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateObservation(@FormParam("id") String id, HashMap<String, Object> props) {
        dbOps.updateObservation(id, props);
    }

    @Path("/observation/{id}")
    @DELETE
    public Response deleteObservation(@HeaderParam("id") String id) {
        try {
            dbOps.deleteObservation(id);
            return Response.status(200).entity(GraphApi.class).build();
        }
        catch (Exception e){
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
        try {
            HashMap<String, HashMap<String, Object>> knowledges = dbOps.getAllKnowledges();
            return Response.status(200).entity(knowledges).build();
        }
        catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @Path("/knowledge/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response readKnowledge(@PathParam("id") String id) {
        try {
            HashMap<String, Object> knowledge = dbOps.getKnowledge(id);
            return Response.status(200).entity(knowledge).build();
        }
        catch (Exception e){
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
        try {
            dbOps.deleteKnowledge(id);
            return Response.status(200).entity(GraphApi.class).build();
        }
        catch (Exception e){
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
