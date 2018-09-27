package graphTool;
import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.string.DuplicateInputIdException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DbUtils
{
    Node root;

    GraphDatabaseService graphDb;

    public Node init() {
        this.root = this.graphDb.createNode(Const.ROOT_LABEL);
        return this.root;
    }

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

    public Node createNode(Label label,HashMap<String, Object> props)
    {
        Node node;

        try ( Transaction tx = graphDb.beginTx() )
        {

            //Check for duplicates first
            if(graphDb.findNode(label, Const.UUID, props.get(Const.UUID)) == null)//If no copies in DB we create new node
            {
                node = graphDb.createNode(label);

                for (String key: props.keySet()) {
                    node.setProperty(key, props.get(key));
                }
                tx.success();
                return node;
            } else {
                tx.failure();
                throw new IllegalArgumentException("ERROR :: A node already exists in the database for id: " + props.get(Const.UUID));
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        return null;
    }

    public void updateNode(Node node, String id, String idVal){

        try ( Transaction tx = graphDb.beginTx() )
        {
            node.setProperty(id,idVal);

            tx.success();

        }
    }

el    public int checkDepth(Label label)
    {
        switch(label){
            case Const.ROOT_LABEL:
                 return 0;

            case Const.OBSERVATION_LABEL:
                 return 1;

            case Const.KNOWLEDGE_LABEL:
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

    public List<Node> getNodesByType(Label type) {
        try
        {
            List<Node> results = new ArrayList<>();
            int depth = 0;
            if (type.equals(Const.OBSERVATION_LABEL))
            {
                depth = 1;
            }
            else if (type.equals(Const.KNOWLEDGE_LABEL))
            {
                depth = 2;
            }
            TraversalDescription td = graphDb.traversalDescription()
                    .breadthFirst()
                    .relationships(Const.RELATE_ROOT_OBSERVATION, Direction.OUTGOING)
                    .relationships(Const.RELATE_OBSERVATION_KNOWLEDGE, Direction.OUTGOING)
                    .evaluator(Evaluators.toDepth(depth));
            Traverser traverser = td.traverse(this.root);
            for (Path path : traverser)
            {
                if (path.length() == depth && path.endNode().hasLabel(type))
                {
                    results.add(path.endNode());
                }
            }
            return results;
        }
        catch (Exception e)
        {
            logger.error("get by type failed", e);
            return new ArrayList<>();
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

    public void createRelationship(Label label1, Node node1, Label label2, Node node2)
    {

        Relationship relationship;

        try( Transaction tx = graphDb.beginTx())
        {

            int node1_depth = checkDepth(label1);
            int node2_depth = checkDepth(label2);

           showAllGraphRelationships();

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
