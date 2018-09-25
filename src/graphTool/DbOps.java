package graphTool;

public class DbOps {
    DbUtils database = new DbUtils();
    Node root = DbUtils.init();

    DbOps() {
        database = new DbUtils();
        database.getConnection();
    }

    public void createObservation() {

    }

    public void readObservation() {

    }

    public void updateObservation() {

    }

    public void deleteObservation() {

    }

    public void createKnowledge() {

    }

    public void readKnowledge() {

    }

    public void updateKnowledge() {

    }

    public void deleteKnowledge() {

    }
}