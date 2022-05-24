package com.youilab.senskywebapp.entities;

import java.util.HashMap;
import java.util.Map;

public class Survey implements Mappable<Survey>{
    private String id, path, title, key;

    public String getTitle() {
        return this.title;
    }

    public Survey setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public Survey setId(String id) {
        this.id = id;
        return this;
    }

    public String getPath() {
        return this.path;
    }

    public Survey setPath(String path) {
        this.path = path;
        return this;
    }

    public String getKey() {
        return key;
    }

    public Survey setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * Generates a JSON representation of this instance.
     * @return The JSON String that represents the current state of this instance.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if (this.getPath() != null)
            builder.append("\"path\":\"").append(this.getPath()).append("\",");
        if (this.getTitle() != null)
            builder.append("\"title\":\"").append(this.getTitle()).append("\",");
        if (this.getKey() != null)
            builder.append("\"key\":\"").append(this.getKey()).append("\",");
        return builder.substring(0, builder.length() - 1) + "}";
    }

    /**
     * Generates a Map (key/value) representation of this instance.
     * @return The Map that represents the current state of this instance.
     */
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getPath() != null)
            map.put("path", this.getPath());
        if (this.getTitle() != null)
            map.put("title", this.getTitle());
        if (this.getKey() != null)
            map.put("key", this.getKey());
        return map;
    }

    /**
     * Generates a Survey instance from an instance of a Map (Key/Value).
     * @return This instance with the input map data.
     */
    public Survey fromMap(Map<String, Object> map){
        if (map.containsKey("path")) this.setPath(map.get("path").toString());
        if (map.containsKey("title")) this.setTitle(map.get("title").toString());
        if (map.containsKey("key")) this.setKey(map.get("key").toString());
        return this;
    }
}