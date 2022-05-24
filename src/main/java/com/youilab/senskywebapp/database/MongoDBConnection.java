package com.youilab.senskywebapp.database;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * This class aims to provide an access point to the database, based on the singleton pattern design
 */

public class MongoDBConnection {
    private final static String DB_NAME = "SenSkyDEV2";

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
}

