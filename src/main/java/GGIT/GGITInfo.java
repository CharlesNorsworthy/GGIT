package GGIT;

public class GGITInfo {
    public GGITGraph repo;

    public GGITNode currentNode;

    GGITInfo(GGITGraph repo, GGITNode currentNode) {
        this.repo = repo;
        this.currentNode = currentNode;
    }

}
