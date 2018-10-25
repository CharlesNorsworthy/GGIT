package graphTool.client;

import graphTool.DbOps;
import graphTool.GraphApi;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import javax.annotation.PostConstruct;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

//public class RestApp extends Application {
//    String db_location = "\\C:\\Neo4J";
//    @Context DbOps dbOps;
//
//    public Set<Class<?>> getClasses() {
//        Set<Class<?>> s = new HashSet<Class<?>>();
//        s.add(GraphApi.class);
//        //s.add(MultiPartFeature.class);
//        //s.add(MultiPart.class);
//        return s;
//    }
//
//    @PostConstruct
//    public void initialize() {
//        try {
//            dbOps = new DbOps(db_location);
//            System.out.println("Neo4j startup succeeded"); // logged in tomcat catalina.out
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Neo4j startup failed"); // logged in tomcat catalina.out
//        }
//    }
//}
