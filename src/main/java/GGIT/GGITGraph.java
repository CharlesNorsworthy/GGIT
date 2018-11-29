package GGIT;

import graphTool.Const;
import graphTool.DbUtils;
//import org.jgrapht.graph.DefaultDirectedGraph;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//import java.util.Set;
//import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class GGITGraph{
    DbUtils db;

//    HashMap<String, Object> repos;

    Node root;

    public GGITGraph(String dbPath) {
        this.db = new DbUtils(dbPath);
    }

    /** Used to initialize the root of the repository
     * @param graphRef
     */
    public String initRepo(String graphRef) {
        String uuid = UUID.randomUUID().toString();
        HashMap<String, Object> props = new HashMap<>();
        props.put(Const.UUID, uuid);
        props.put(GGITConst.GRAPH_REFERENCE, graphRef);
        props.put(GGITConst.BRANCH, GGITConst.MASTER);
        props.put(GGITConst.TIMESTAMP, (new Date()).getTime());
        this.root = this.db.createNode(GGITConst.PARENT_LABEL, props);
        return uuid;
    }

    /** Used to create a new node
     * @param uuid
     * @param graphRef
     * @param branch
     */
    public void createNode(String uuid, String graphRef, String branch) {
        HashMap<String, Object> props = new HashMap<>();
        props.put(Const.UUID, uuid);
        props.put(GGITConst.GRAPH_REFERENCE, graphRef);
        props.put(GGITConst.BRANCH, branch);
        props.put(GGITConst.TIMESTAMP, (new Date()).getTime());
        this.db.createNode(GGITConst.CHILD_LABEL, props);
    }

    /** Used to get the properties of a node
     * @param uuid
     */
    public HashMap<String, Object> readNode(String uuid) {
        Node node = db.readNode(GGITConst.CHILD_LABEL, uuid);
        return db.readNodeProperties(node);
    }

    /** Used to check if the listed "current" node is actually the most current
     * @param uuid
     */
    public long getTimeStamp(String uuid) {
        return 0;
    }

    public void listBranches() {
        if (db != null) {
            db.getLabels().forEach(label -> System.out.println(label.name()));
        }
    }

    public void closeGraph(){
        db.dispose();
    }

//    private static GGITNode root = null;
//
//    private static DefaultDirectedGraph<GGITNode, GGITEdge> graph;
//
//    public GGITGraph() {}
//
//    public GGITGraph(String graphRef, String branch) {
//        try {
//            Supplier<GGITNode> nodeSupplier = () -> new GGITNode();
//            Supplier<GGITEdge> edgeSupplier = () -> new GGITEdge();
//            graph = new DefaultDirectedGraph(nodeSupplier, edgeSupplier, false);
//            root = makeNode(graphRef, GGITConst.MASTER);
//        } catch (IllegalArgumentException e) {
//            System.out.println(e);
//        }
//    }
//
//    public GGITNode makeNode(String graphRef, String branch) {
//        try {
//            GGITNode node = new GGITNode(graphRef, branch);
//            if (!graph.addVertex(node)) {
//                throw new Exception("Could not create a node for graph!");
//            }
//            if (root != null) {
//                this.connectNodes(root, node);
//            }
//            return node;
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
//
//    public void connectNodes(GGITNode from, GGITNode to) {
//        try {
//            graph.addEdge(from, to);
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
//
//    public GGITNode getNode(String graphRef, String branch) {
//        GGITNode rNode;
//        Set<GGITNode> nodes = graph.vertexSet();
//        for (GGITNode node : nodes) {
//            if (node.getGraphRef().equals(graphRef)) {
//                rNode = node;
//                return rNode;
//            }
//        }
//        throw new IllegalArgumentException("The there is no node for '" + graphRef +"'");
//    }
//
//    public DefaultDirectedGraph<GGITNode, GGITEdge> cloneGraph() {
//        return (DefaultDirectedGraph<GGITNode, GGITEdge>) graph.clone();
//    }
//
//    public boolean containsNode(GGITNode node) {
//        return graph.containsVertex(node);
//    }
}
