package graphTool.client;

import graphTool.GraphApi;
import org.neo4j.graphdb.Node;

import javax.json.Json;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class GraphClient {

//    private static final String REST_URI = "http://localhost:8025/api/observation";
//
//    private Client client = ClientBuilder.newClient();
//
//    public Node getJsonObservation(String id){
//        return client.target(REST_URI).path(id).request(MediaType.APPLICATION_JSON).get(Node.class);
//    }
//    public static void main(String[] args){
//
//        WebTarget webTarget = client.target("http://localhost:8025");
//
//        //path to specified resource, "../observation"
//        WebTarget observationTarget = webTarget.path("/observation");
//
//        //builds an http request invocation
//        Invocation.Builder invocationBuilder = observationTarget.request(MediaType.APPLICATION_JSON);
//
//        //Invokes http GET
//        //Response response = invocationBuilder.get(GraphApi.class);
//        //Invoke http POST
//        Response response = invocationBuilder.post(Entity.entity(observationTarget, MediaType.APPLICATION_JSON);
//
//
//    }
}
