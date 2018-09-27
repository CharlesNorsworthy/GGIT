package graphTool;

import java.util.HashMap;

public class DbOps {
    DbUtils database;
    Node root = DbUtils.init();

    DbOps() {
        database = new DbUtils();
        database.getConnection();
    }

    public void createObservation(HashMap<String, Object> obs) {

    }

    public HashMap readObservation(String UUID) {
        return null;
    }

    public void updateObservation(String UUID, HashMap<String, Object> obs) {

    }

    public void deleteObservation(String UUID) {

    }

    public void createKnowledge(HashMap<String, Object> knw) {

    }

    public HashMap readKnowledge(String UUID) {
        return null;
    }

    public void updateKnowledge(String UUID, HashMap<String, Object> knw) { }

    public void deleteKnowledge(String UUID) { }
}