package graphTool;

import com.sun.xml.internal.ws.api.message.ExceptionHasMessage;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.string.DuplicateInputIdException;

import java.io.File;
import java.util.*;


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
            if(graphDb.findNodes(Const.ROOT_LABEL).hasNext()){
                root = graphDb.findNodes(Const.ROOT_LABEL).next();
            }
            else {
                root = this.graphDb.createNode(Const.ROOT_LABEL);
                System.out.println("New root created.");
            }
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
                if(label == Const.OBSERVATION_LABEL){
                    node.createRelationshipTo(root, Const.RELATE_ROOT_OBSERVATION);
                }

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

    public void updateNode(Node node, HashMap<String, Object> props){

        try ( Transaction tx = graphDb.beginTx() )
        {
            for (String prop : props.keySet()) {
                node.setProperty(prop, props.get(prop));
            }
            tx.success();

        }catch(Exception e){
            System.out.println("Unable to update node. Msg :" + e.getMessage());
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

    public void createDefaultNodes()
    {
        try ( Transaction tx = graphDb.beginTx() )
        {
            int obs = 5, knw = 3;
            // Create some observations
            for ( int id = 0; id < obs; id++ )
            {
                Node node = graphDb.createNode( Const.OBSERVATION_LABEL );
                if(root == null)
                    initRoot();
                node.createRelationshipTo(root,Const.RELATE_ROOT_OBSERVATION);
                node.setProperty( Const.UUID, UUID.randomUUID().toString() );
                node.setProperty(Const.NAME, "observation " + id);
                node.setProperty(Const.LATITUDE, new Random().nextDouble());
                node.setProperty(Const.LONGITUDE, new Random().nextDouble());
                node.setProperty(Const.DESCRIPTION, "New created observation node[" + id +"]");
            }
            System.out.println("Observations created (" + obs + ").");

            //create some knowledges
            for (int i = 0; i < knw; i++){
                Node node = graphDb.createNode(Const.KNOWLEDGE_LABEL);
                node.setProperty( Const.UUID, UUID.randomUUID().toString() );
                node.setProperty(Const.NAME, "knowledge " + i);
                node.setProperty(Const.LATITUDE, new Random().nextDouble());
                node.setProperty(Const.LONGITUDE, new Random().nextDouble());
                node.setProperty(Const.DESCRIPTION, "New created knowledge node[" + i +"]");
            }
            System.out.println("Knowledges created (" + knw + ").");

            //link observations to knowledges
            ResourceIterator<Node> obsNodes = graphDb.findNodes(Const.OBSERVATION_LABEL);
            ResourceIterator<Node> knwNodes = graphDb.findNodes(Const.KNOWLEDGE_LABEL);
            int count = 0;
            Node knwNode = null;
            while(obsNodes.hasNext()){
                if(count % 2 == 0 && knwNodes.hasNext())
                    knwNode = knwNodes.next();

                obsNodes.next().createRelationshipTo(knwNode,Const.RELATE_OBSERVATION_KNOWLEDGE);
                count++;
            }
            System.out.println("Linked observations to knowledges.");

            tx.success();
        }
        catch (Exception e){
            System.out.println("Could not create default nodes. Msg: " + e.getMessage());
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



    public List<Node> getNodesByType(Label label) {
        try( Transaction tx = graphDb.beginTx())
        {
//            List<Node> results = new ArrayList<>();
//            int depth = 0;
//            if (label == Const.OBSERVATION_LABEL)
//            {
//                depth = 1;
//            }
//            else if (label == Const.KNOWLEDGE_LABEL)
//            {
//                depth = 2;
//            }
//            TraversalDescription td = graphDb.traversalDescription()
//                    .breadthFirst()
//                    .relationships(Const.RELATE_ROOT_OBSERVATION, Direction.OUTGOING)
//                    .relationships(Const.RELATE_OBSERVATION_KNOWLEDGE, Direction.OUTGOING)
//                    .evaluator(Evaluators.toDepth(depth));
//
//            Traverser traverser = td.traverse(this.root);
//
//            for (Path path : traverser)
//            {
//                if (path.length() == depth && path.endNode().hasLabel(label))
//                {
//                    results.add(path.endNode());
//                }
//            }
//            tx.success();
//            return results;

            System.out.println("Searching for " + label.name() + " nodes...");

            ResourceIterator<Node> nodeIterator = graphDb.findNodes(label);
            if(nodeIterator != null) {
                List<Node> nodes = new ArrayList<>();
                while (nodeIterator.hasNext()) {
                    Node node = nodeIterator.next();
                    nodes.add(node);
                    System.out.println("Found " + label.name() + " node with id = " + node.getProperty(Const.UUID));
                }
                tx.success();
                return nodes;
            }
            else {
                System.out.println("No " + label.name() + " nodes found.");
                return new ArrayList<>();
            }
        }
        catch (Exception e)
        {
            System.out.println("An error occured while getting all " + label + "nodes. Msg :" + e.getMessage());
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

    public HashMap<String, Object> readNodeMap(Node node){
        HashMap<String, Object> nodeMap = new HashMap<>();
        try(Transaction tx = graphDb.beginTx())
        {
            nodeMap = (HashMap)node.getAllProperties();
            tx.success();
        }
        catch(Exception e){
            System.out.println("Unable to map node properties. Msg: " + e.getMessage());
        }
        return nodeMap;
    }

    public HashMap<String,HashMap <String,Object>> readNodesMap(List<Node> nodeList){

        HashMap<String, HashMap<String, Object>> nodes = new HashMap<>();
        try ( Transaction tx = graphDb.beginTx())
        {
            nodeList.iterator().forEachRemaining( node -> {
                try
                {
                    nodes.put( node.getProperty(Const.UUID) + "", (HashMap) node.getAllProperties());
                }
                catch(Exception e)
                {
                    System.out.println("Error mapping props. Msg:" + e.getMessage());
                }
            });
            tx.success();
        }
        catch(Exception e){
            System.out.println("Unable to map node properties. Msg: " + e.getMessage() );
        }
        return nodes;
    }

    public void deleteNodesByType(Label label)
    {
        try ( Transaction tx = graphDb.beginTx())
        {
            ResourceIterator<Node> nodes = (graphDb.findNodes(label));

            while(nodes.hasNext())
            {
                Node current = nodes.next();
                if(current.hasRelationship()){
                    current.getRelationships().forEach(rel -> {
                        if(label != Const.ROOT_LABEL || rel.getType() != Const.RELATE_ROOT_OBSERVATION) {
                            rel.delete();
                        }
                    });
                }
                System.out.println("Deleting node { id: " + current.getProperties(Const.UUID) + " , name: " + current.getProperties(Const.NAME ) + "}");
                try {
                    current.delete();
                }
                catch (NotFoundException e){
                    System.out.println(e.getMessage());
                    System.out.println("node " + current.getProperty(Const.UUID));
                }
            }
            tx.success();
        }
        catch(Exception e){
            System.out.println("Unable to delete all " + label.name() + " nodes. Msg : " +  e.getMessage());
        }
    }

    public void deleteNode(Label label, String id) {

        try ( Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.findNode(label, Const.UUID, id);
            if(node.hasRelationship()){
                node.getRelationships().forEach( rel->{
                    rel.delete();
                });
            }
            node.delete();
            tx.success();
        } catch (Exception e) {
            System.out.println("Unable to delete node {id :" + id + "}. Msg : " + e.getMessage());
        }

    }

    public void createRelationship(Label label1, Node node1, Label label2, Node node2)
    {
        RelationshipType rt;
        int node1_depth = getDepth(label1);
        int node2_depth = getDepth(label2);

        try( Transaction tx = graphDb.beginTx()) {
            if ((Math.abs(node1_depth - node2_depth) == 1)) {
                if (node1_depth < node2_depth) {
                    rt = getRelationshipType(node1);
                    node1.createRelationshipTo(node2, rt);
                    tx.success();
                } else if (node1_depth > node2_depth) {
                    rt = getRelationshipType(node2);
                    node2.createRelationshipTo(node1, rt);
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

    public List<Relationship> getRelationships(){
        List<Relationship> rels = new ArrayList<>();
        try(Transaction tx = graphDb.beginTx())
        {
            ResourceIterator<Relationship> relIterator = graphDb.getAllRelationships().iterator();
            relIterator.forEachRemaining(rel -> {
                try {
                    System.out.println("Found relationship " + rel.getType().name() + " between " + rel.getStartNode().getLabels().toString()
                            + " node {id: " + (!rel.getStartNode().hasProperty(Const.UUID) ? "[root]" : rel.getStartNode().getProperty(Const.UUID)) + " } and " + rel.getEndNode().getLabels().toString()
                            + " node {id: " + (!rel.getEndNode().hasProperty(Const.UUID) ? "[root]" : rel.getEndNode().getProperty(Const.UUID)) + " }.");
                    rels.add(rel);
                }
                catch(Exception e){
                    System.out.println("Error. Msg : " + e.getMessage());
                }
            });
            tx.success();
        }
        catch(Exception e){
            System.out.println("Unable to get relaionships. Msg:" + e.getMessage());
        }
        return rels;
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

    public void dispose(){
        graphDb.shutdown();
    }
}
