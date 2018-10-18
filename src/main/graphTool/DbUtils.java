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
    private static int succeeded;

    private static Node root;

    private static GraphDatabaseService graphDb;

    public DbUtils(String dbPath) {
        this.getConnection(dbPath);
    }

    public Node initRoot() {
        try (Transaction tx = graphDb.beginTx()) {
            root = this.graphDb.createNode(Const.ROOT_LABEL);
            tx.success();
            return root;
        } catch (Exception e) {
            System.out.println("Unable to create 'root' node for the graph database.");
        }
        return null;
    }

    private void connectDatabase(String dbPath)
    {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(dbPath));
        registerShutdownHook(graphDb);  //Used to shut down database if JVM is closed
    }

    /**
     * Handle the success and failure for a database transaction
     * @param tx
     */
    private static void handleTx(Transaction tx) {
        if (succeeded == 0) {
            tx.success();
        } else {
            tx.failure();
        }
        tx.close();
        succeeded = 0;
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

    private RelationshipType getRelationshipType(Node node)
    {
        if (node.hasLabel(Const.ROOT_LABEL))
        {
            return Const.RELATE_ROOT_OBSERVATION;

        }
        else if (node.hasLabel(Const.OBSERVATION_LABEL))
        {
            return  Const.RELATE_OBSERVATION_KNOWLEDGE;

        }
        else{
            return null;
        }
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
                tx.close();
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

    public void updateNode(Node node, String id, Object idVal){

        try ( Transaction tx = graphDb.beginTx() )
        {
            node.setProperty(id,idVal);

            tx.success();

        }
    }

    public int getDepth(Label label)
    {
        if (label == Const.ROOT_LABEL) {
            return 0;
        } else if (label == Const.OBSERVATION_LABEL) {
            return 1;
        } else if (label == Const.KNOWLEDGE_LABEL) {
            return 2;
        }
        throw new IllegalArgumentException("ERROR :: The label given {" + label + "} does match any labels in the database!");
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

    public Node getNodeById(Label label, String id){
        Node node = null;

        try ( Transaction tx = graphDb.beginTx() )
        {
            node = graphDb.findNode(label, Const.UUID, id);
            if (node != null) {
                tx.success();
                return node;
            } else {
                tx.failure();
                throw new NotFoundException("ERROR :: There is no node with id '" + id + "' in the database.");
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return node;
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
//            logger.error("get by type failed", e);
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

    public void deleteNode(String id) {
    }

    public void createRelationship(Label label1, Node node1, Label label2, Node node2)
    {
        Relationship relationship;
        RelationshipType rt;
        int node1_depth = getDepth(label1);
        int node2_depth = getDepth(label2);

        try( Transaction tx = graphDb.beginTx()) {
            if ((Math.abs(node1_depth - node2_depth) == 1)) {
                if (node1_depth < node2_depth) {
                    rt = getRelationshipType(node1);
                    relationship = node1.createRelationshipTo(node2, rt);
                    tx.success();
                } else if (node1_depth > node2_depth) {
                    rt = getRelationshipType(node2);
                    relationship = node2.createRelationshipTo(node1, rt);
                    tx.success();
                }
            } else {
                tx.failure();
                throw new IllegalArgumentException("ERROR :: The nodes cannot be related!");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

//    public void printNode(Node node)
//    {
//        System.out.println("Label: "+ node.getLabels() + " id:" + node.getProperty("id") + " relationship:" + node.getRelationshipTypes() + " #relationships: " + node.getRelationships());
//    }
//
//    public void printNode(String labelName,String nodeType)
//    {
//
//        Label label = Label.label(labelName);
//        try( Transaction tx = graphDb.beginTx())
//        {
//            Node node = graphDb.findNode(label,"id",nodeType);
//            System.out.println("Label: "+ node.getLabels() + " id:" + node.getProperty("id") + " depth: " + node.getProperty("depth") +" relationship:" + node.getRelationshipTypes() + " #relationships: " + node.getRelationships());
//
//            tx.success();
//
//        }
//    }

//    public void showAllGraphRelationships()
//    {
//        RelationshipType relationship;
//
//        try( Transaction tx = graphDb.beginTx())
//        {
//           // ResourceIterable<RelationshipType> allRelationships = graphDb.getAllRelationshipTypes();
//           // ResourceIterator<RelationshipType> relationshipTypes = graphDb.getAllRelationshipTypes();
////            while(relationshipTypes.hasNext())
////            {
////                relationship = relationshipTypes.next();
////                System.out.println(relationship);
////            }
//            //relationships.showAllGraphRelationships();
//
//            tx.success();
//        }
//    }

//    public List<Relationship> getRelationships(Label label, Node node)
//    {
//        Relationship relationship;
//
//        try(Transaction tx = graphDb.beginTx())
//        {
//
//            Label label1 = Label.label(nodeType1);
//            Label label2 = Label.label(nodeType2);
//
//            Node node1 = graphDb.createNode(label1);
//            Node node2 = graphDb.createNode(label2);
//
//
//        }
//
//    }

    public void getConnection(String dbPath)
    {
        connectDatabase(dbPath);
    }

}
