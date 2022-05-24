package com.youilab.senskywebapp.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Evidence implements Mappable<Evidence>{
    private Double latitude, longitude, altitude;
    private String id, surveyPath, photoPath, workshopKey, surveyKey, avatar, comment;
    private List<String> tags = new ArrayList<String>();
    private Long capturedTimestamp, storedTimestamp;


    public String getAvatar() {
        return avatar;
    }

    public Evidence setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public Evidence setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public Evidence setId(String id) {
        this.id = id;
        return this;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Evidence setLatitude(Double latitude) throws EntityException {
        if (Validations.validateLatitude(latitude)){
            this.latitude = latitude;
            return this;
        } else throw new EntityException("INVALID_DATA");
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Evidence setLongitude(Double longitude) throws EntityException {
        if (Validations.validateLongitude(longitude)){
            this.longitude = longitude;
            return this;
        } else throw new EntityException("INVALID_DATA");

    }

    public Double getAltitude() {
        return this.altitude;
    }

    public Evidence setAltitude(Double altitude) {
        this.altitude = altitude;
        return this;
    }

    public String getSurveyPath() {
        return this.surveyPath;
    }

    public Evidence setSurveyPath(String surveyPath) {
        this.surveyPath = surveyPath;
        return this;
    }

    public String getPhotoPath() {
        return this.photoPath;
    }

    public Evidence setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
        return this;
    }

    public String getWorkshopKey() {
        return this.workshopKey;
    }

    public Evidence setWorkshopKey(String workshopKey) {
        this.workshopKey = workshopKey;
        return this;
    }

    public String getSurveyKey() {
        return this.surveyKey;
    }

    public Evidence setSurveyKey(String surveyKey) {
        this.surveyKey = surveyKey;
        return this;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public Evidence setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public Evidence addTag(String tag){
        this.tags.add(tag);
        return this;
    }

    public Long getCapturedTimestamp() {
        return this.capturedTimestamp;
    }

    public Evidence setCapturedTimestamp(Long capturedTimestamp) {
        this.capturedTimestamp = capturedTimestamp;
        return this;
    }

    public Long getStoredTimestamp() {
        return this.storedTimestamp;
    }

    public Evidence setStoredTimestamp(Long storedTimestamp) {
        this.storedTimestamp = storedTimestamp;
        return this;
    }

    /**
     * Generates a JSON representation of this instance.
     * @return The JSON String that represents the current state of this instance.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if (this.getLatitude() != null)
            builder.append("\"latitude\":").append(this.getLatitude()).append(",");
        if (this.getLongitude() != null)
            builder.append("\"longitude\":").append(this.getLongitude()).append(",");
        if (this.getAltitude() != null)
            builder.append("\"altitude\":").append(this.getAltitude()).append(",");
        if (this.getId() != null)
            builder.append("\"id\":\"").append(this.getId()).append("\",");
        if (this.getSurveyPath() != null)
            builder.append("\"survey-path\":\"").append(this.getSurveyPath()).append("\",");
        if (this.getPhotoPath() != null)
            builder.append("\"photo-path\":\"").append(this.getPhotoPath()).append("\",");
        if (this.getWorkshopKey() != null)
            builder.append("\"workshop-key\":\"").append(this.getWorkshopKey()).append("\",");
        if (this.getSurveyKey() != null)
            builder.append("\"survey-key\":\"").append(this.getSurveyKey()).append("\",");
        if (this.getCapturedTimestamp() != null)
            builder.append("\"captured-timestamp\":").append(this.getCapturedTimestamp()).append(",");
        if (this.getStoredTimestamp() != null)
            builder.append("\"stored-timestamp\":").append(this.getStoredTimestamp()).append(",");
        if (this.getTags().size() > 0){
            String jsonTags = this.getTags().stream()
                    .map( tag -> "\"" + tag + "\"" )
                    .collect(Collectors.joining(", ", "[", "]"));
            builder.append("\"tags\":").append(jsonTags).append(",");
        }
        if (this.getComment() != null)
            builder.append("\"comment\":\"").append(this.getComment()).append("\"");
        if (this.getAvatar() != null)
            builder.append("\"avatar\":\"").append(this.getAvatar()).append("\"");
        return builder.substring(0, builder.length() - 1) + "}";
    }


    /**
     * Generates a Map (key/value) representation of this instance.
     * @return The Map that represents the current state of this instance.
     */
    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        if (this.getLatitude() != null)
            map.put("latitude", this.getLatitude());
        if (this.getLongitude() != null)
            map.put("longitude", this.getLongitude());
        if (this.getAltitude() != null)
            map.put("altitude", this.getAltitude());
        if (this.getId() != null)
            map.put("_id", this.getId());
        if (this.getSurveyPath() != null)
            map.put("survey-path", this.getSurveyPath());
        if (this.getPhotoPath() != null)
            map.put("photo-path", this.getPhotoPath());
        if (this.getWorkshopKey() != null)
            map.put("workshop-key", this.getWorkshopKey());
        if (this.getSurveyKey() != null)
            map.put("survey-key", this.getSurveyKey());
        if (this.getCapturedTimestamp() != null)
            map.put("captured-timestamp", this.getCapturedTimestamp());
        if (this.getStoredTimestamp() != null)
            map.put("stored-timestamp", this.getStoredTimestamp());
        if (this.getTags().size() > 0)
            map.put("tags", this.getTags());
        if (this.getComment() != null)
            map.put("comment", this.getComment());
        if (this.getAvatar() != null)
            map.put("avatar", this.getAvatar());
        return map;
    }

    @Override
    public Evidence fromMap(Map<String, Object> map) {
        return null;
    }
}
