package com.youilab.senskywebapp.daos;

import com.mongodb.MongoWriteException;
import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.database.MongoDBCRUD;
import com.youilab.senskywebapp.entities.Workshop;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class WorkshopDAO implements Dao<Workshop>{


    @Override
    public Workshop find(Workshop searchCriteria) {
        List<Map<String, Object>> result = MongoDBCRUD.findDocuments(searchCriteria.toMap(), SetUp.WORKSHOPS_COLLECTION);
        if (result.size() > 0)
            return new Workshop().fromMap(result.get(0));
        else
            return null;
    }

    @Override
    public List<Workshop> findByAttribute(Workshop likeThis) {
        return MongoDBCRUD.findDocuments(likeThis.toMap(), SetUp.WORKSHOPS_COLLECTION).stream()
                .map(rawObject -> new Workshop().fromMap(rawObject))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean insert(Workshop workshop) {
        boolean repeatedKey = false;
        boolean result = false;
        do {
            workshop.setKey(stringGenerator(6));
            try {
                result = MongoDBCRUD.insertDocument(workshop.toMap(), SetUp.WORKSHOPS_COLLECTION);
            } catch (Exception e) {
                if (e.getMessage() == "DUPLICATED_KEY") repeatedKey = true;
                else {
                    repeatedKey = false;
                    result = false;
                    e.printStackTrace();
                }
            }
        } while (repeatedKey);
        return result;
    }

    private static String stringGenerator(int length) {
        int leftLimit = 48; // character '0' in the ASCII table
        int rightLimit = 90; // character 'Z' in the ASCII table
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> i <= 57 || i >= 65)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


}
