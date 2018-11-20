package GGIT;

import org.apache.commons.cli.Options;

public class GGITInfo {
    public GGITGraph repo;

    public GGITNode currentNode;

    GGITInfo(GGITGraph repo, GGITNode currentNode) {
        this.repo = repo;
        this.currentNode = currentNode;
    }

}
