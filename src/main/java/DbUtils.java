import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;

import java.io.File;
import java.util.ArrayList;


public class DbUtils
{
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;

    private void connectDatabase()
    {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File("\\C:\\Neo4J"));
        registerShutdownHook(graphDb);  //Used to shut down database if JVM is closed
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }


    private enum RelTypes implements RelationshipType
    {
        KNOWS
    }

    public void createNode(String nodeType,String prop, String propVal, int num)
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label(nodeType);

            // Create some users
            for ( int id = 0; id < num; id++ )
            {
                Node userNode = graphDb.createNode( label );
                userNode.setProperty( prop, propVal );
            }
            System.out.println( nodeType +  " created" );
            tx.success();
        }
    }

    public void createDefaultNodes(String name ,int num)
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label(name);

            // Create some users
            for ( int id = 0; id < num; id++ )
            {
                Node userNode = graphDb.createNode( label );
                userNode.setProperty( "username", "user" + id + "@neo4j.org" );
            }
            System.out.println( "Users created" );
            tx.success();
        }
    }

    public void showAllNodes(String nodeName,String prop)
    {

        Label label = Label.label(nodeName);

        try ( Transaction tx = graphDb.beginTx() )
        {
            try ( ResourceIterator<Node> users =
                          graphDb.findNodes( label ) )
            {
                ArrayList<Node> userNodes = new ArrayList<>();
                while ( users.hasNext() )
                {
                    userNodes.add( users.next() );

                }

                for ( Node node : userNodes )
                {
                    System.out.println( "The property of node " + nodeName + " is " + node.getProperty( prop ) );

                }
            }
            tx.success();
        }

    }

    public void getNodeById(String nodeName,String prop, String id){

        Label label = Label.label(nodeName);

        try ( Transaction tx = graphDb.beginTx() )
        {
            try ( ResourceIterator<Node> users =
                          graphDb.findNodes( label, prop, id) )
            {
                ArrayList<Node> userNodes = new ArrayList<>();
                while ( users.hasNext() )
                {
                    userNodes.add( users.next() );

                }

                for ( Node node : userNodes )
                {
                    System.out.println(
                            "The username of user " + id + " is " + node.getProperty( prop ) );
                }
            }
            tx.success();
        }

    }

    public void deleteNodes(String NodeType,String prop, String propVal)
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label( NodeType );
            ResourceIterator<Node> users = ( graphDb.findNodes( label, prop, propVal ) );
                    //  Node[] node = graphDb.findNodes(label,prop,propVal);
//                      for(Node user : users ){
//                          user.delete();
//                      }
            while(users.hasNext()){
                Node user = users.next();
                user.delete();
            }
            tx.success();
        }
    }

//    public void updateNodeProperty()
//    {
//        try ( Transaction tx = graphDb.beginTx() )
//        {
//            Label label = Label.label( "User" );
//            int idToFind = 45;
//            String nameToFind = "user" + idToFind + "@neo4j.org";
//
//            ResourceIterator<Node>loop;
//            for ( Node node : loop =( graphDb.findNodes( label, "username", nameToFind ) ) )
//            {
//                node.setProperty( "username", "user" + (idToFind + 1) + "@neo4j.org" );
//            }
//            tx.success();
//        }
//    }

    public void getConnection()
    {
        connectDatabase();
    }
//    private final Driver driver;
//
//    public DbConnection()
//    {
//
//        driver = GraphDatabase.driver("bolt://localhost:7687",AuthTokens.basic( "neo4j", "password"));
//    }
//
//
//    public DbConnection( String uri, String user, String password )
//    {
//        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
//    }
//
//    @Override
//    public void close() throws Exception
//    {
//        driver.close();
//    }
//
//    public void addNode(String[] args){
//        //Create a node with 1 basic property
//        try ( Session session = driver.session() )
//        {
//            session.run("CREATE " + "(" + args[0] + ":" + args[1] + "{" + args[2] + ":$" + args[2] + "})",
//                        parameters( args[2], args[3] ) );
//        }
//    }
//
//    public StatementResult showNodes(String[] args){
//           Session session = driver.session();
//
//        {
//            StatementResult result = session.run( "MATCH (" + args[0] + ":" + args[1] +") RETURN " + args[0] +"."+ args[2]);
//            while(result.hasNext() )
//            {
//                Record record = result.next();
//                System.out.println( String.format( record.get(args[0]+"."+ args[2]).asString()) );
//            }
//            return result;
//        }
//
//    }
//
//    public void queryDatabase(String query, String[] params) {
//        //String[] split = params.;
//        try ( Session session = driver.session() )
//        {
//            session.run(query, parameters( ) );
//
//        }
//    }
//
//    public void printGreeting( final String message )
//    {
//        try ( Session session = driver.session() )
//        {
//            String greeting = session.writeTransaction( new TransactionWork<String>()
//            {
//                @Override
//                public String execute( Transaction tx )
//                {
//                    StatementResult result = tx.run( "CREATE (a:Greeting) " +
//                                    "SET a.message = $message " +
//                                    "RETURN a.message + ', from node ' + id(a)",
//                                     parameters( "message", message ) );
//                    return result.single().get( 0 ).asString();
//                }
//            } );
//            System.out.println( greeting );
//        }
//    }
//
//    public static void main( String... args ) throws Exception
//    {
//        try ( DbConnection greeter = new DbConnection( "bolt://localhost:7687", "neo4j", "password" ) )
//        {
//            greeter.printGreeting( "hello, world" );
//        }
//    }
}