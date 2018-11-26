package GGIT;

import graphTool.Const;
import graphTool.DatabaseBuilder;
import graphTool.DbUtils;
//import org.jgrapht.graph.DefaultDirectedGraph;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

//import java.util.Set;
//import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class GGITGraph implements DatabaseBuilder {
    DbUtils db;

    HashMap<String, Object> repos;

    Node root;

    public GGITGraph(String dbPath) {
        db = new DbUtils(dbPath);
        root = db.initRoot();
        createRepo(dbPath);
    }

    @Override
    public Label getLabel(Relationship rel) {
        return null;
    }

    //Utilize Neo4j to build out GGITGraph
    public void createRepo(String graphRef) {
        repos = new HashMap<>();
        HashMap<String, Object> props = new HashMap<>();
        props.put(Const.UUID, UUID.randomUUID().toString());
        props.put(GGITConst.GRAPH_REFERENCE, graphRef);
        props.put(GGITConst.BRANCH, GGITConst.MASTER);
        Node root = db.createNode(Const.ROOT_LABEL, props);
        repos.put(graphRef, root);
        db.updateNode(root, repos);
    }

    public void listBranches() {
        List<Label> labels = db.getLabels();

        labels.forEach(label -> System.out.println(label.name()));
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
