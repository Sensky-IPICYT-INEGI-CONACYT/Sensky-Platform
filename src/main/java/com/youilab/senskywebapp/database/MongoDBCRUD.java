package com.youilab.senskywebapp.database;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.*;

/**
 * This class contains functions CRUD.
 */
public class MongoDBCRUD {

    /**
     *
     * @param criteria A map containing the criteria attributes.
     * @param collectionName The name of the collection containing the dataset.
     * @return The result set in the form of a List o maps.
     */
    public static List<Map<String, Object>> findDocuments(Map<String, Object> criteria, String collectionName){
        FindIterable<Document> result = MongoDBConnection.getInstance()
                .getCollection(collectionName).find(new Document(criteria));
        List<Map<String, Object>> coincidences = new ArrayList<Map<String, Object>>();
        for (Document document : result) coincidences.add(documentToMap(document));
        return coincidences;
    }

    /**
     * Use this function to insert a new document into the database.
     * @param object The map representation of the object to be inserted.
     * @param collectionName The name of the collection containing the dataset.
     * @return true if the object was inserted, false otherwise.
     * @throws Exception Thrown when the input object contains a custom id and it's duplicated in database.
     */
    public static Boolean insertDocument(Map<String, Object> object, String collectionName) throws Exception {
        Document dbObject = new Document(object);
        try {
            MongoDBConnection.getInstance().getCollection(collectionName).insertOne(dbObject);
            return dbObject.containsKey( "_id" );
        } catch (MongoWriteException exception){
            throw new Exception("DUPLICATED_KEY");
        }
    }

    /**
     * Use this function to insert an object into the database, but in the format of a Document instance.
     * @param object The Document object to be inserted.
     * @param collection The name of the collection containing the dataset.
     * @return true if it was inserted, false otheriwse.
     */
    public static String insertOneDocument(Document object, String collection) {
        try {
            MongoDBConnection.getInstance().getCollection(collection).insertOne(object);
            return object.getObjectId("_id").toString();
        } catch (MongoException e) {
            return "null";
        }
    }

    /**
     * This method tries to find coincidences in the Database according to some criteria rules.
     * @param criteria is a map that contains the criteria rules for matching in Databases.
     * @return List of coincidences where each document is given in the form of a Map.
     */
    public static Long updateDocument(Map<String, Object> criteria, Map<String, Object> update,
                                      String collectionName){
        Document toFind = new Document(criteria);
        Document updateData = new Document().append("$set", update);
        UpdateResult result = MongoDBConnection.getInstance().getCollection(collectionName).updateOne(toFind, updateData);
        return result.getModifiedCount();
    }

    /**
     * Use this function to transform a Document object to a Map of key/values.
     * @param document The Document instance to be converted.
     * @return The Map representation of the input object.
     */
    private static Map<String, Object> documentToMap(Document document){
        Set<String> keys = document.keySet();
        Map<String, Object> map = new HashMap<String, Object>();
        keys.forEach(key -> map.put(key, document.get(key)));
        return map;
    }
}
