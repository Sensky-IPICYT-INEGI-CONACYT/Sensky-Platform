package com.youilab.senskywebapp.daos;

import java.util.List;

public interface Dao<T> {

    /**
     * Finds a specific object given the key attribute value ("_id" in MongoBD).
     * @param searchCriteria The corresponding entity object that contains the criteria search attributes.
     * @return the object identified by the given key.
     */
    T find( T searchCriteria );

    /**
     * Finds a set of objects that match with a given search criteria (an entity).
     * @param likeThis Object that contains the attributes to compare.
     * @return the set of objects in database matching the given criteria.
     */
    List<T> findByAttribute (T likeThis);

    /**
     * Inserts and entity object into database
     * @param entity the object that contains the set of attributes and keys to create an equivalent object in database.
     * @return true if the object insertion was successfully, false otherwise.
     */
    Boolean insert ( T entity) throws Exception;

}
