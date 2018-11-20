package GGIT;

public class GGITEdge {
    final String branch;

    public GGITEdge() {
        this.branch = GGITConst.MASTER;
    }

    public GGITEdge(String branch) {
        this.branch = branch;
    }
}
