package com.youilab.senskywebapp.daos;

import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.database.MongoDBCRUD;
import com.youilab.senskywebapp.entities.Team;

import java.util.List;
import java.util.Map;

public class TeamDAO implements Dao<Team>{


    @Override
    public Team find(Team searchCriteria) {
        List<Map<String, Object>> result = MongoDBCRUD.findDocuments(searchCriteria.toMap(), SetUp.TEAMS_COLLECTION);
        if (result.size() > 0)
            return new Team().fromMap(result.get(0));
        else return null;
    }

    @Override
    public List<Team> findByAttribute(Team likeThis) {
        return null;
    }

    @Override
    public Boolean insert(Team entity) throws Exception {
            return MongoDBCRUD.insertDocument(entity.toMap(), SetUp.TEAMS_COLLECTION);
    }
}
