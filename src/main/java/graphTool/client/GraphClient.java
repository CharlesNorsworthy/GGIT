package graphTool.client;

import org.glassfish.jersey.client.ClientConfig;

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
//        //Response response = invocationBuilder.get(NodeResource.class);
//        //Invoke http POST
//        Response response = invocationBuilder.post(Entity.entity(observationTarget, MediaType.APPLICATION_JSON);
//
//
//    }
    private static String OBS_URL = "http://localhost:8025/api/observation";
    private static String KNW_URL = "http://localhost:8025/api/knowledge";
    static ClientConfig clientConfig = null;
    static Client client = null;
    static WebTarget webTarget = null;
    static Invocation.Builder builder = null;
    static Response response = null;
    static int responseCode;
    static String responseMessageFromServer = null;
    static String responseString = null;

    public static void main(String[] args){

        System.out.println("Running @GET allObservations endpoint.....\n");
        TestGETAllNodes(OBS_URL);

    }

    public static void TestGETAllNodes(String URL){

        try{
            // invoke service after setting necessary parameters
            clientConfig = new ClientConfig();
            client =  ClientBuilder.newClient(clientConfig);
            //          client.property("Content-Type", MediaType.TEXT_PLAIN);
            //          client.property("accept", MediaType.TEXT_PLAIN);
            webTarget = client.target(URL);

            // invoke service
            builder = webTarget.request().accept(MediaType.APPLICATION_JSON);
            response = builder.get();

            // get response code
            responseCode = response.getStatus();
            System.out.println("Response code: " + responseCode);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + responseCode);
            }

            // get response message
            responseMessageFromServer = response.getStatusInfo().getReasonPhrase();
            System.out.println("ResponseMessageFromServer: " + responseMessageFromServer);

            // get response string
            responseString = response.readEntity(String.class);
            System.out.println(responseString);
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        finally{
            // release resources, if any
            response.close();
            client.close();
        }
    }

    public static void TestGETSingleNode(String id){


    }
}
