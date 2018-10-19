import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class DbUtils
{
    GraphDatabaseService graphDb;

    private void connectDatabase(String pathName)
    {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(pathName));
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


    enum RelTypes implements RelationshipType
    {
        KNOWS
    }

    public void createNode(String nodeType,String prop, String propVal, int num)
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            Label label = Label.label(nodeType);

            // Create some users
            for ( int id = 1; id <= num; id++ )
            {
                Node userNode = graphDb.createNode( label );
                userNode.setProperty( prop, propVal );
            }
            System.out.println( nodeType +  " created" );
            tx.success();
        }
    }

    static void putNodeInGraph(GraphDatabaseService graph, String id){
        try(Transaction tx = graph.beginTx()){ //TODO: put in relationships
            Node newNode = graph.createNode();
            //String id = getNodeID(graph, node);
            newNode.setProperty("ID", id);

            tx.success();
        }
    }

    static RelationshipType getRelationshipType(GraphDatabaseService graph, Node node, Relationship relationship){
        RelationshipType relType;
        try(Transaction tx = graph.beginTx()){
            relType = relationship.getType();
            tx.success();
        }
        return relType;
    }

    static void connectNodeInGraphByRelationship(GraphDatabaseService graph, Node node, Iterator<Relationship> relsIter){
        try(Transaction tx = graph.beginTx()){
            Relationship relationship;
            while (relsIter.hasNext()){
                relationship = relsIter.next();
                RelationshipType relType = getRelationshipType(graph, node, relationship);
                Node[] relNodes = relationship.getNodes();
                for(Node relNode : relNodes){
                    node.createRelationshipTo(relNode, relType);
                }
            }
            tx.success();
        }
    }

    static void createRelationshipBetween(GraphDatabaseService graph, Node node1, Node node2, RelationshipType relType){
        try(Transaction tx = graph.beginTx()){
            node1.createRelationshipTo(node2, relType);
            tx.success();
        }
    }

    static Iterator<Relationship> getRelationshipIterator(GraphDatabaseService graph, Node node){
        Iterator<Relationship> relsIterator;
        try(Transaction tx = graph.beginTx()){
            Iterable<Relationship> rels = node.getRelationships();
            relsIterator = rels.iterator();
            tx.success();
        }
        return relsIterator;
    }

    static void printRelationships(GraphDatabaseService graph, Node node, Iterator<Relationship> relsIter){

    }

    static Node[] getRelationshipNodes(GraphDatabaseService graph, Relationship relationship){
        Node[] relationshipNodes;
        try(Transaction tx = graph.beginTx()){
            relationshipNodes = relationship.getNodes();
            tx.success();
        }
        return relationshipNodes;
    }

    static String getNodeID(GraphDatabaseService graph, Node node){
        String ID;
        try(Transaction tx = graph.beginTx()){
            ID = node.getProperty("ID").toString();
            //ID = String.valueOf(node.getId());
            tx.success();
        }
        return ID;
    }

    public static boolean testIteratorHasNext(GraphDatabaseService graph, ResourceIterable<Node> iterator){
        boolean result;
        try(Transaction tx = graph.beginTx()){
            result = iterator.iterator().hasNext();
            tx.success();
        }
        return result;
    }

    public static Node getNextNodeFromIterator(GraphDatabaseService graph, ResourceIterable<Node> iterator){
        Node nextNode;
        try(Transaction tx = graph.beginTx()){
            nextNode = iterator.iterator().next();
            tx.success();
        }
        return nextNode;
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

    public void showAllNodes(String nodeName, String prop)
    {

        Label label = Label.label(nodeName);

        try ( Transaction tx = graphDb.beginTx() )
        {
            try ( ResourceIterator<Node> users = graphDb.findNodes( label ) )

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

    static ResourceIterator<Node> getAllNodesIterator(GraphDatabaseService graph){
        ResourceIterator<Node> allIterableNodes;
        try(Transaction tx = graph.beginTx()){
            ResourceIterable<Node> iterable = graph.getAllNodes();
            allIterableNodes = iterable.iterator();

            tx.success();
        }

        return allIterableNodes;
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

    public void deleteNodes(String nodeType,String prop, String propVal)
    {
        try ( Transaction tx = graphDb.beginTx())
        {
            Label label = Label.label( nodeType );
            ResourceIterator<Node> users = ( graphDb.findNodes( label, prop, propVal ) );

            while(users.hasNext())
            {
                Node user = users.next();
                user.delete();
            }
            tx.success();
        }
    }

    public void createRelationship(String nodeType1, String nodeType2)
    {
        Relationship relationship;
        try( Transaction tx = graphDb.beginTx())
        {
            Label label1 = Label.label(nodeType1);
            Label label2 = Label.label(nodeType2);

            ResourceIterator<Node> node = (graphDb.findNodes(label1));
            ResourceIterator<Node> node2 = (graphDb.findNodes(label2));

            while(node.hasNext()){
                Node firstNode = node.next();
                Node secondNode = node2.next();

                relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
                relationship.setProperty( "link", "we are related " );
            }
        }
    }



    public void showRelationships(String nodeType1, String prop1,String nodeType2,String prop2){
        Relationship relationship;
        try( Transaction tx = graphDb.beginTx())
        {

             Label label1 = Label.label("a");
             Label label2 = Label.label("b");
             Node node1 = graphDb.createNode(label1);
             Node node2 = graphDb.createNode(label2);
            node1.setProperty("prop","Patrick hates");
            node2.setProperty("prop", "neo4j");
            relationship = node1.createRelationshipTo( node2, RelTypes.KNOWS );
            relationship.setProperty( "message", "relationships in" );

            System.out.println(node1.getProperty("prop"));
            System.out.println(relationship.getProperty("message"));
            System.out.println(node2.getProperty("prop"));
//            Label label1 = Label.label(nodeType1);
//            Label label2 = Label.label(nodeType2);
//
//            ResourceIterator<Node> node = (graphDb.findNodes(label1));
//            ResourceIterator<Node> node2 = (graphDb.findNodes(label2));
//
//            while(node.hasNext()){
//                Node firstNode = node.next();
//               // Node secondNode = node2.next();
//
//                System.out.println(firstNode.getProperties(prop1));
//                System.out.println(firstNode.getSingleRelationship(RelTypes.KNOWS,Direction.OUTGOING));
//               // System.out.println(secondNode.getProperties(prop2));
//                //System.out.println(secondNode.getRelationships());
//
//            }
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

    public void getConnection(String pathName)
    {
        connectDatabase(pathName);
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