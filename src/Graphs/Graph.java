import org.w3c.dom.Node;


public interface Graph {

    void init();

    void addNode();

    void removeNode();

    void addEdge();

    void revoceEdge();

    boolean isAdjacent();

    double getNodeVal();

    void setNodeVal();

    String getNodeID();

    void setNodeID();


}
