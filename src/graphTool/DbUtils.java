package graphTool;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


public class DbUtils
{
    GraphDatabaseService graphDb;

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
        Observ_to_Know,Root_to_Observ

    }

    private enum Direction implements RelationshipType
    {
        OUTGOING

    }

    public void createNode(String nodeType,HashMap<String, Object> props)
    {
        int depth;
        Label label = Label.label(nodeType);

        try ( Transaction tx = graphDb.beginTx() )
        {

            //Check for duplicates first

                Node findDuplicates = graphDb.findNode(label, graphTool.Const.UUID, props.get(graphTool.Const.UUID));

            if(findDuplicates == null)//If no copies in DB we create new node
            {
                Node node = graphDb.createNode(label);

                for(String key : props.keySet())
                {
                    node.setProperty(key, props.get(key));
                }

                depth = assignDepth(node); //auto assign depth according to label
                node.setProperty("depth", depth);

            }
            else
                {

                   updateNode(findDuplicates);//Duplicate was found we just update id for now

                }

            tx.success();
        }
    }

    public void updateNode(Node node){

        try ( Transaction tx = graphDb.beginTx() )
        {

            tx.success();

        }
    }

    public int assignDepth(Node node)
    {

        String label = node.getLabels().toString();
        System.out.println(label);
        switch(label){
            case "[Root]":
                 return 0;

            case "[Observation]":
                 return 1;

            case "[Knowledge]":
                 return 2;

        }
        return 0;
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

    public void showAllNodesofProp(String nodeName,String prop)
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

    public void getNodeById(String nodeType,String prop,String id){
        Label label = Label.label(nodeType);

        try ( Transaction tx = graphDb.beginTx() )
        {
            try ( ResourceIterator<Node> users = graphDb.findNodes(label, prop, id))

            {
                ArrayList<Node> userNodes = new ArrayList<>();
                while (users.hasNext())
                {
                    userNodes.add(users.next());

                }
                for (Node node : userNodes)
                {
                    System.out.println( "The username of user " + id + " is " + node.getProperty(prop));

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

    public void deleteNodes(String nodeType)
    {
        try ( Transaction tx = graphDb.beginTx())
        {
            Label label = Label.label(nodeType);
            ResourceIterator<Node> nodes = (graphDb.findNodes(label));

            while(nodes.hasNext())
            {
                Node current = nodes.next();
                current.delete();
            }
            tx.success();
        }
    }

    public void createRelationship(String nodeType1,String node1_Id,String nodeType2,String node2_Id)
    {

        Relationship relationship;

        try( Transaction tx = graphDb.beginTx())
        {
            Label label1 = Label.label(nodeType1);
            Label label2 = Label.label(nodeType2);

            Node node1 = (graphDb.findNode(label1,"id",node1_Id));
            Node node2 = (graphDb.findNode(label2, "id",node2_Id));

            int node1_depth = (int)(node1.getProperty("depth"));
            int node2_depth =(int)(node2.getProperty("depth"));

           // printNode(node1);
           // printNode(node2);
           //showAllGraphRelationships();

            if((Math.abs(node1_depth - node2_depth) == 1))
                {
                    if (node1_depth < node2_depth)
                    {
                        RelTypes relTypes = getRelationshipType(node1, node2);
                        relationship = node1.createRelationshipTo(node2, relTypes);

                    }

                    if (node1_depth > node2_depth)
                    {
                        RelTypes relTypes = getRelationshipType(node2, node1);
                        relationship = node2.createRelationshipTo(node1, relTypes);

                    }

                }
                else
                    {
                        System.out.println("nodes cannot be connected");

                    }
                tx.success();
            }
    }

    public void printNode(Node node)
    {
        System.out.println("Label: "+ node.getLabels() + " id:" + node.getProperty("id") + " relationship:" + node.getRelationshipTypes() + " #relationships: " + node.getRelationships());
    }

    public void printNode(String labelName,String nodeType)
    {

        Label label = Label.label(labelName);
        try( Transaction tx = graphDb.beginTx())
        {
            Node node = graphDb.findNode(label,"id",nodeType);
            System.out.println("Label: "+ node.getLabels() + " id:" + node.getProperty("id") + " depth: " + node.getProperty("depth") +" relationship:" + node.getRelationshipTypes() + " #relationships: " + node.getRelationships());

            tx.success();

        }
    }

    public void showAllGraphRelationships()
    {
        RelationshipType relationship;

        try( Transaction tx = graphDb.beginTx())
        {
           // ResourceIterable<RelationshipType> allRelationships = graphDb.getAllRelationshipTypes();
           // ResourceIterator<RelationshipType> relationshipTypes = graphDb.getAllRelationshipTypes();
//            while(relationshipTypes.hasNext())
//            {
//                relationship = relationshipTypes.next();
//                System.out.println(relationship);
//            }
            //relationships.showAllGraphRelationships();

            tx.success();
        }
    }

    private RelTypes getRelationshipType(Node node1, Node node2)
    {
        if((int)node1.getProperty("depth") == 0)
        {
            return RelTypes.Root_to_Observ;

        }
        else if((int)node1.getProperty("depth") == 1)
        {
            return  RelTypes.Observ_to_Know;

        }
        else{
            return null;
        }
    }

    public void showRelationships(String nodeType1, String prop1,String nodeType2,String prop2)
    {

        Relationship relationship;

        try( Transaction tx = graphDb.beginTx())
        {

            Label label1 = Label.label(nodeType1);
            Label label2 = Label.label(nodeType2);

            Node node1 = graphDb.createNode(label1);
            Node node2 = graphDb.createNode(label2);


        }

    }

    public void getConnection()
    {
        connectDatabase();
    }

}
