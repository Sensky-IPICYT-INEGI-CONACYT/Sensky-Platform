package com.youilab.senskywebapp.entities;

import com.youilab.senskywebapp.util.Encoders;

import java.util.HashMap;
import java.util.Map;

public class Team implements Mappable{
    private String name, email, password, picture, leader;
    private Long initTimestamp;
    private Boolean isActive;

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Team setIsActive(Boolean active) {
        isActive = active;
        return this;
    }

    public String getLeader() {
        return leader;
    }

    public Team setLeader(String leader) {
        this.leader = leader;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Team setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public Team setEmail(String email) throws EntityException {
        if (Validations.validateEmail(email)){
            this.email = email;
            return this;
        } else throw new EntityException("INVALID_DATA");
    }

    public String getPassword() {
        return this.password;
    }

    public Team setPassword(String password) throws EntityException {
        if (Validations.validatePassword(password)){
            this.password = Encoders.toSHA256(password);
            return this;
        } else throw new EntityException("INVALID_DATA");
    }

    public String getPicture() {
        return this.picture;
    }

    public Team setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public Long getInitTimestamp() {
        return initTimestamp;
    }

    public Team setInitTimestamp(Long initTimestamp) {
        this.initTimestamp = initTimestamp;
        return this;
    }


    /**
     * Generates a JSON representation of this instance.
     * @return The JSON String that represents the current state of this instance.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if (this.getName() != null)
            builder.append("\"name\":\"").append(this.getName()).append("\",");
        if (this.getEmail() != null)
            builder.append("\"email\":\"").append(this.getEmail()).append("\",");
        if (this.getIsActive() != null)
            builder.append("\"isActive\":").append(this.getIsActive()).append(",");
        if (this.getPassword() != null)
            builder.append("\"password\":\"").append(this.getPassword()).append("\",");
        if (this.getPicture() != null)
            builder.append("\"picture-path\":\"").append(this.getPicture()).append("\",");
        if (this.getInitTimestamp() != null)
            builder.append("\"init-timestamp\":").append(this.getInitTimestamp()).append(",");
        if (this.getLeader() != null)
            builder.append("\"leader\":").append(this.getLeader()).append(",");
        return builder.substring(0, builder.length() - 1) + "}";
    }

    /**
     * Generates a Map (key/value) representation of this instance.
     * @return The Map that represents the current state of this instance.
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (this.getName() != null) map.put("name", this.getName());
        if (this.getEmail() != null) map.put("email", this.getEmail());
        if (this.getIsActive() != null) map.put("isActive", this.getIsActive());
        if (this.getPassword() != null) map.put("password", this.getPassword());
        if (this.getPicture() != null) map.put("picture-path", this.getPicture());
        if (this.getInitTimestamp() != null) map.put("init-timestamp", this.getInitTimestamp());
        if (this.getLeader() != null) map.put("leader", this.getLeader());
        return map;
    }

    /**
     * Generates a Team instance from an instance of a Map (Key/Value).
     * @return This instance with the input map data.
     */
    @Override
    public Team fromMap(Map map) {
        if (map.containsKey("name")) this.setName(map.get("name").toString());
        try {
            if (map.containsKey("email"))
                this.setEmail(map.get("email").toString());
        } catch (EntityException e) {
            System.out.println("INVALID_DATABASE_EMAIL");
        }
        if (map.containsKey("isActive")) this.setIsActive((Boolean)map.get("isActive"));
        if (map.containsKey("picture-path")) this.setPicture(map.get("picture-path").toString());
        if (map.containsKey("leader")) this.setLeader(map.get("leader").toString());
        return this;
    }
}
