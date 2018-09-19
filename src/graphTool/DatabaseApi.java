package graphTool;

// Needs neo4j imported

/**
 * The DatabaseApi class will actually manipulate the graph database.
 */
public class DatabaseApi {
    private GraphDatabaseService graphDb;

    public void initDatabase(File databaseDir) {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(databaseDir);
    }

    public void createNode() {

    }

    public Node readNode() {

    }

    public void updateNode() {

    }

    public void deleteNode() {

    }
}
