package com.youilab.senskywebapp.database;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Map;

/**
 * This class is for the Connection instance with the DataLayers database.
 */
public class MongoDBSensors {
    private final static String DB_NAME = "YouiLabDataLayers";

    private static MongoDatabase instance;

    /**
     * Creates an instance for the connection with the database
     * @return The connection object.
     */
    private static MongoDatabase createInstance(){
        instance = MongoClients.create().getDatabase(DB_NAME);
        return instance;
    }

    /**
     * Retrieves a connection object. If a connection is already stablished, it's no necessary to create a new one.
     * @return A connection Object.
     */
    public static MongoDatabase getInstance(){
        if (instance != null) return instance;
        else return createInstance();
    }

    /**
     * Inserts a pollutants register into a given collection of the DataLayers database.
     * @param object The Map representation of the pollutants register object.
     * @param collectionName The name of the collection or dataset.
     * @return true if the object was inserted, false otherwise.
     * @throws Exception Thrown is the custom id is duplicated.
     */
    public static Boolean insertDocument(Map<String, Object> object, String collectionName) throws Exception {
        Document dbObject = new Document(object);
        try {
            getInstance().getCollection(collectionName).insertOne(dbObject);
            return dbObject.containsKey( "_id" );
        } catch (MongoWriteException exception){
            throw new Exception("DUPLICATED_KEY");
        }
    }
}
