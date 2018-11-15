import org.jgrapht.graph.DefaultDirectedGraph;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class GGITGraph {
    private static GGITNode root = null;

    private static DefaultDirectedGraph<GGITNode, GGITEdge> graph;

    public GGITGraph() {}

    public GGITGraph(String graphRef, String branch) {
        this.graph = new DefaultDirectedGraph(new Supplier() {
            @Override
            public GGITNode get() {
                return null;
            }
        }, new Supplier() {
            @Override
            public GGITEdge get() {
                return null;
            }
        }, false);
        this.root = makeNode(graphRef, GGITConst.MASTER);
    }

    public GGITNode makeNode(String graphRef, String branch) {
        try {
            GGITNode node = new GGITNode(graphRef, branch);
            if (!this.graph.addVertex(node)) {
                throw new Exception("Could not create a node for graph!");
            }
            if (this.root != null) {
                this.connectNodes(this.root, node);
            }
            return node;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public void connectNodes(GGITNode from, GGITNode to) {
        try {
            this.graph.addEdge(from, to);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public GGITNode getNode(String graphRef, String branch) {
        GGITNode rNode;
        Set<GGITNode> nodes = this.graph.vertexSet();
        for (GGITNode node : nodes) {
            if (node.getGraphRef().equals(graphRef)) {
                rNode = node;
                return rNode;
            }
        }
        throw new IllegalArgumentException("The there is no node for '" + graphRef +"'");
    }

    public boolean containsNode(GGITNode node) {
        return this.graph.containsVertex(node);
    }
}
