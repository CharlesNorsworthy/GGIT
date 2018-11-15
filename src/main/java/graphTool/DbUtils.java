package graphTool;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.*;

import java.io.File;
import java.util.*;


public class DbUtils
{
    private static Node root;

    private static GraphDatabaseService graphDb;

    public DbUtils(String dbPath) {
        this.connectDatabase(dbPath);
    }

    private void connectDatabase(String dbPath) {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( new File(dbPath));
        registerShutdownHook(graphDb);  //Used to shut down database if JVM is closed
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
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

    public Node initRoot() {
        try (Transaction tx = graphDb.beginTx()) {
            if(graphDb.findNodes(Const.ROOT_LABEL).hasNext()){
                root = graphDb.findNodes(Const.ROOT_LABEL).next();
            }
            else {
                root = this.graphDb.createNode(Const.ROOT_LABEL);
                root.setProperty(Const.UUID, UUID.randomUUID());
                root.setProperty(Const.NAME, "root");
                System.out.println("New root created.");
            }
            tx.success();
            return root;
        } catch (Exception e) {
            System.out.println("Unable to create 'root' node for the graph database.");
        }
        return null;
    }

    public Node createNode(Label label, HashMap<String, Object> props) {
        try ( Transaction tx = graphDb.beginTx() ) {
            //Check for duplicates first
            if(graphDb.findNode(label, Const.UUID, props.get(Const.UUID)) == null){ //If no copies in DB we create new node
                Node node = graphDb.createNode(label);
                for (String key: props.keySet()) {
                    node.setProperty(key, props.get(key));
                }
                if(!node.hasProperty(Const.UUID)){ //If no id was passed create UUID
                    node.setProperty(Const.UUID, UUID.randomUUID());
                }
                if(label == Const.OBSERVATION_LABEL) { //If creating observation link to root
                    root.createRelationshipTo(node, Const.RELATE_ROOT_OBSERVATION);
                }
                tx.success();
                tx.close();
                return  node;
            } else {
                tx.failure();
                throw new IllegalArgumentException("ERROR :: A node already exists in the database for id: " + props.get(Const.UUID));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("ERROR :: " + e.getMessage());
            return null;
        }
    }

    public Node readNode(Label label, String id){
        try ( Transaction tx = graphDb.beginTx() )
        {
            Node node = graphDb.findNode(label, Const.UUID, id);
            if (node != null) {
                tx.success();
                tx.close();
                return  node;
            } else {
                tx.failure();
                throw new NotFoundException("ERROR :: There is no node with id '" + id + "' in the database.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Node> readNodes(Label label) {
        List<Node> nodes = new ArrayList<>();
        try( Transaction tx = graphDb.beginTx())
        {
            ResourceIterator<Node> nodeIterator = graphDb.findNodes(label);
            while (nodeIterator.hasNext()) {
                try {
                    Node node = nodeIterator.next();
                    nodes.add(node);
                    System.out.println("Found " + label.name() + " node with id = " + node.getProperty(Const.UUID));
                } catch (Exception e){
                    System.out.println("ERROR :: " + e.getMessage());
                }
            }
            tx.success();
        }
        catch (Exception e)
        {
            System.out.println("An error occurred while getting all " + label + "nodes. Msg :" + e.getMessage());
        }
        return nodes;
    }

    public void updateNode(Node node, HashMap<String, Object> props){
        try ( Transaction tx = graphDb.beginTx() ) {
            if(node != null) {
                for (String prop : props.keySet()) {
                    node.setProperty(prop, props.get(prop));
                }
            }
            tx.success();
        }catch(Exception e){
            System.out.println("Unable to update node. Msg :" + e.getMessage());
        }
    }

    public void deleteNode(Label label, String id) {

        try ( Transaction tx = graphDb.beginTx()) {
            Node node = graphDb.findNode(label, Const.UUID, id);
            if(node != null) {
                if (node.hasRelationship()) {
                    node.getRelationships().forEach(rel -> rel.delete());
                }
                node.delete();
            }
            tx.success();
        } catch (Exception e) {
            System.out.println("Unable to delete node {id :" + id + "}. Msg : " + e.getMessage());
        }
    }

    public void deleteNodes(Label label) {
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

    public Relationship createRelationship(Node startNode, Node endNode, RelationshipType type){
        try(Transaction tx = graphDb.beginTx()){
            Relationship rel = startNode.createRelationshipTo(endNode, type);
            rel.setProperty(Const.UUID, UUID.randomUUID().toString());
            tx.success();
            tx.close();
            return rel;
        } catch(Exception e){
            System.out.println("ERROR :: " + e.getMessage());
            return null;
        }
    }

    public Relationship readRelationship(Node startNode, Node endNode, RelationshipType type){
        try(Transaction tx = graphDb.beginTx()){
            HashSet<Relationship> rels = new HashSet<>();
            startNode.getRelationships(type).forEach(rel -> {
                if(rel.getEndNode() == endNode)
                    rels.add(rel);
            });
            if(rels.isEmpty())
                throw new NotFoundException();
            tx.success();
            tx.close();
            return rels.iterator().next();
        } catch(Exception e){
            System.out.println("ERROR :: " + e.getMessage());
            return null;
        }
    }

    public List<Relationship> readRelationships(Node node){
        List<Relationship> rels = new ArrayList<>();
        try(Transaction tx = graphDb.beginTx()){
            node.getRelationships().iterator().forEachRemaining(rel ->{
                try {
                    System.out.println("Found relationship " + rel.getType().name() + " between " + rel.getStartNode().getLabels().toString()
                            + " node {id: " + (rel.getStartNode().getProperty(Const.UUID)) + " } and " + rel.getEndNode().getLabels().toString()
                            + " node {id: " + (rel.getEndNode().getProperty(Const.UUID)) + " }.");
                    rels.add(rel);
                }
                catch(Exception e){
                    System.out.println("Error. Msg : " + e.getMessage());
                }
                tx.success();
            });
        }        catch(Exception e){
            System.out.println("Unable to get relaionships. Msg:" + e.getMessage());
        }
        return rels;
    }

    public List<Relationship> readAllRelationships(){
        List<Relationship> rels = new ArrayList<>();
        try(Transaction tx = graphDb.beginTx())
        {
            graphDb.getAllRelationships().iterator().forEachRemaining(rel -> {
                try {
                    System.out.println("Found relationship " + rel.getType().name() + " between " + rel.getStartNode().getLabels().toString()
                            + " node {id: " + (rel.getStartNode().getProperty(Const.UUID)) + " } and " + rel.getEndNode().getLabels().toString()
                            + " node {id: " + (rel.getEndNode().getProperty(Const.UUID)) + " }.");
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

    public List<Relationship> readAllRelationships(RelationshipType type){
        List<Relationship> rels = new ArrayList<>();
        try(Transaction tx = graphDb.beginTx())
        {
            graphDb.getAllRelationships().iterator().forEachRemaining(rel -> {
                if(rel.getType() == type)
                    rels.add(rel);
            });
            tx.success();
        }
        catch(Exception e){
            System.out.println("Unable to get relaionships. Msg:" + e.getMessage());
        }
        return rels;
    }

    public void deleteRelationship(Relationship rel){
        try(Transaction tx = graphDb.beginTx()){
            rel.delete();
            tx.success();
        } catch (Exception e ){
            System.out.println("ERROR :: " + e.getMessage());
        }
    }

    public HashMap<String, Object>  readNodeProperties(Node node){
        HashMap<String, Object> nodeMap = new HashMap<>();
        try(Transaction tx = graphDb.beginTx()){
            nodeMap = (HashMap<String, Object>) node.getAllProperties();
            tx.success();
        }catch (Exception e){
            System.out.println("Unable to map node properties. Msg : " + e.getMessage());
        }
        return nodeMap;
    }

    public HashMap<String, HashMap<String, Object>> readNodeListProperties(List<Node> nodes){
        HashMap<String, HashMap<String, Object>> nodesMap = new HashMap<>();
        try(Transaction tx = graphDb.beginTx()){
            nodes.iterator().forEachRemaining(node -> {
                try {
                    nodesMap.put(node.getProperty(Const.UUID).toString(), (HashMap<String, Object>) node.getAllProperties());
                } catch (Exception e){
                    System.out.println("Unable to map node properties. Msg : " + e.getMessage());
                }
                tx.success();
                });
        }catch (Exception e){
            System.out.println("ERROR :: " + e.getMessage());
        }
        return nodesMap;
    }

    public HashMap<String, Object> readRelationshipProperties(Relationship rel){
        HashMap<String, Object> relMap = new HashMap<>();
        try(Transaction tx = graphDb.beginTx()){
            relMap.put(Const.START_NODE, rel.getStartNode().getProperty(Const.UUID));
            relMap.put(Const.END_NODE, rel.getEndNode().getProperty(Const.UUID));
            relMap.put(Const.RELATIONSHIP_TYPE, rel.getType().name());
            relMap.put(Const.UUID, rel.getProperty(Const.UUID));
            tx.success();
        } catch (Exception e){
            System.out.println("Unable to map relationship properties. Msg : " + e.getMessage());
        }
        return relMap;
    }

    public HashMap<String, HashMap<String, Object>> readRelationshipListProperties(List<Relationship> rels){
        HashMap<String, HashMap<String, Object>> relsMap = new HashMap<>();
        try(Transaction tx = graphDb.beginTx()){
            rels.iterator().forEachRemaining(rel ->{
                try{
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(Const.START_NODE, rel.getStartNode().getProperty(Const.UUID));
                    map.put(Const.END_NODE, rel.getEndNode().getProperty(Const.UUID));
                    map.put(Const.RELATIONSHIP_TYPE, rel.getType().name());
                    map.put(Const.UUID, rel.getProperty(Const.UUID));
                    relsMap.put(Const.START_NODE, map);
                } catch (Exception e){
                    System.out.println("Unable to map relationship properties. Msg : " + e.getMessage());
                }
            });
            tx.success();
        } catch (Exception e){
            System.out.println("ERROR :: " +e.getMessage());
        }
        return relsMap;
    }

    public void createDefaultNodes() {
        try ( Transaction tx = graphDb.beginTx() )
        {
            int obs = 5, knw = 3;
            // Create some observations
            for ( int id = 0; id < obs; id++ )
            {
                Node node = graphDb.createNode( Const.OBSERVATION_LABEL );
                if(root == null)
                    initRoot();
                node.createRelationshipTo(root,Const.RELATE_ROOT_OBSERVATION)
                        .setProperty(Const.UUID, UUID.randomUUID().toString());
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

                obsNodes.next()
                        .createRelationshipTo(knwNode,Const.RELATE_OBSERVATION_KNOWLEDGE)
                        .setProperty(Const.UUID, UUID.randomUUID().toString());
                count++;
            }
            System.out.println("Linked observations to knowledges.");

            tx.success();
        }
        catch (Exception e){
            System.out.println("Could not create default nodes. Msg: " + e.getMessage());
        }
    }

    public String getNodeID(Node node){
        String ID;
        try(Transaction tx = graphDb.beginTx()){
            ID = node.getProperty("ID").toString();
            if(ID == null){
                ID = "";
            }
            tx.success();
        }
        return ID;
    }

    public Node getNodeByID(Object value){
        ResourceIterator<Node> graphNodesIterator = getAllNodesIterator();
        Node currentNode;
        try(Transaction tx = graphDb.beginTx()){
            while(graphNodesIterator.hasNext()) {
                currentNode = graphNodesIterator.next();
                String currentKey = currentNode.getProperty("ID").toString();
                if (currentKey.equals(value)) {
                    return currentNode;
                }
            }
            tx.success();
        }
        return null;
    }

    public void putNodeInGraph(String id){
        try(Transaction tx = graphDb.beginTx()){
            Node newNode = graphDb.createNode();
            newNode.setProperty("ID", id);
            tx.success();
        }
    }

    public ResourceIterator<Node> getAllNodesIterator(){
        ResourceIterator<Node> allIterableNodes;
        try(Transaction tx = graphDb.beginTx()){
            ResourceIterable<Node> iterable = graphDb.getAllNodes();
            allIterableNodes = iterable.iterator();
            tx.success();
        }
        return allIterableNodes;
    }

    public ArrayList<String> getAllIDs(){
        ResourceIterator<Node> nodesItr = getAllNodesIterator();
        String currentId;
        ArrayList<String> allIds = new ArrayList<>();
        try(Transaction tx = graphDb.beginTx()){
            while(nodesItr.hasNext()){
                Node node = nodesItr.next();
                currentId = node.getProperty("ID").toString();
                allIds.add(currentId);
            }
            tx.success();
        }
        return allIds;
    }

    public Iterator<Relationship> getRelationshipIterator(Node node){
        Iterator<Relationship> relsIterator;
        try(Transaction tx = graphDb.beginTx()){
            Iterable<Relationship> rels = node.getRelationships();
            relsIterator = rels.iterator();
            tx.success();
        }
        return relsIterator;
    }

    public RelationshipType getRelationshipType(Relationship relationship){
        RelationshipType relType;
        try(Transaction tx = graphDb.beginTx()){
            //TODO: incorporate with constants
            relType = relationship.getType();
            tx.success();
        }
        return relType;
    }

    public Node[] getRelationshipNodes(Relationship relationship){
        Node[] relationshipNodes;
        try(Transaction tx = graphDb.beginTx()){
            relationshipNodes = relationship.getNodes();
            tx.success();
        }
        return relationshipNodes;
    }

    //From https://stackoverflow.com/questions/27233978/java-neo4j-check-if-a-relationship-exist
    Relationship getRelationshipBetween(Node startNode, String endNodeId){
        try(Transaction tx = graphDb.beginTx()){
            for (Relationship rel : startNode.getRelationships()){ // n1.getRelationships(type,direction)
                String otherNodeId = getNodeID(rel.getOtherNode(startNode));
                if (otherNodeId.equals(endNodeId)) return rel;
            }
            tx.success();
            return null;
        }
    }

    public void createRelationshipBetween(Node node1, Node node2, RelationshipType relType){
        try(Transaction tx = graphDb.beginTx()){
            Relationship rel = node1.createRelationshipTo(node2, relType);
            //rel.setProperty("T", "test");
            tx.success();
        }
    }

    public void dispose(){
        graphDb.shutdown();
    }
}
