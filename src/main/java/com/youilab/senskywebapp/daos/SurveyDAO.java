package com.youilab.senskywebapp.daos;

import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.database.MongoDBCRUD;
import com.youilab.senskywebapp.entities.Survey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SurveyDAO implements Dao<Survey>{
    @Override
    public Survey find(Survey  searchCriteria) {
        return null;
    }

    public List<Survey> findAll(){
        return MongoDBCRUD.findDocuments(new HashMap<>(), SetUp.SURVEYS_COLLECTION).stream()
                .map( rawSurvey -> new Survey().fromMap(rawSurvey))
                .collect(Collectors.toList());
    }

    @Override
    public List<Survey> findByAttribute(Survey likeThis) {
        return null;
    }

    @Override
    public Boolean insert(Survey entity) {
        return null;
    }

}
