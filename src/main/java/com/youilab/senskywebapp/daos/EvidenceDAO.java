package com.youilab.senskywebapp.daos;

import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.database.MongoDBCRUD;
import com.youilab.senskywebapp.entities.Evidence;
import com.youilab.senskywebapp.entities.Survey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EvidenceDAO implements Dao<Evidence>{

    @Override
    public Evidence find(Evidence searchCriteria) {
        return null;
    }

    @Override
    public List<Evidence> findByAttribute(Evidence likeThis) {
        return null;
    }

    @Override
    public Boolean insert(Evidence entity) {
        try {
            return MongoDBCRUD.insertDocument(entity.toMap(), SetUp.EVIDENCES_COLLECTION);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Evidence> findAll() {
        try {
            Map<String, Object> empty = new HashMap<>();
            List<Map<String, Object>> result = MongoDBCRUD.findDocuments(empty, SetUp.EVIDENCES_COLLECTION);
            List<Evidence> evidences = result.stream()
                    .map( rawSurvey -> new Evidence().fromMap(rawSurvey))
                    .collect(Collectors.toList());
            return evidences;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



}
