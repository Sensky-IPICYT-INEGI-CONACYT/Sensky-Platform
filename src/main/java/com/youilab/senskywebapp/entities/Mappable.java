package com.youilab.senskywebapp.entities;

import java.util.Map;

/**
 * Defines the base methods that a Mappable instance must contain.
 * @param <T> The class used to specify the generic methods.
 */
public interface Mappable<T> {

    /**
     * This method transforms an instance into a Map (Key/Value) representation.
     * @return The map representation of an instance.
     */
    Map<String, Object> toMap ();

    /**
     * This method transforms an input map into a T class instance.
     * @param map The input Key/Value map.
     * @return The instance resulting of the transformation.
     */
    T fromMap( Map<String, Object> map);
}
