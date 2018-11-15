import java.util.Date;

public class GGITNode {
    private static String graphRef;

    private static long timeStamp;

    private static String branch;

    public GGITNode(String graphRef, String branch) {
        try {
            if (graphRef != null) {
                this.graphRef = graphRef;
            } else {
                throw new IllegalArgumentException("A GGITNode's graphRef cannot be set with 'null'");
            }
            timeStamp = (new Date()).getTime();
            if (branch != null) {
                this.branch = branch;
            } else {
                this.branch = GGITConst.MASTER;
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    public String getGraphRef() {
        return this.graphRef;
    }

    public String getBranch() {
        return this.branch;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setGraphRef(String graphRef) {
        this.graphRef = graphRef;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
