package com.youilab.senskywebapp.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Workshop implements Mappable<Workshop>{

    private String placeName, teamKey, key, reportId;
    private Long initTimestamp;
    private Integer staffCount, attendersCount;
    private List<Survey> surveys = new ArrayList<>();

    public String getKey() {
        return this.key;
    }

    public Workshop setKey(String key) {
        this.key = key;
        return this;
    }

    public String getReportId() {
        return reportId;
    }

    public Workshop setReportId(String reportId) {
        this.reportId = reportId;
        return this;
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public Workshop setPlaceName(String placeName) {
        this.placeName = placeName;
        return this;
    }

    public String getTeamKey() {
        return this.teamKey;
    }

    public Workshop setTeamKey(String teamKey) {
        this.teamKey = teamKey;
        return this;
    }

    public Long getInitTimestamp() {
        return this.initTimestamp;
    }

    public Workshop setInitTimestamp(Long initTimestamp) {
        this.initTimestamp = initTimestamp;
        return this;
    }

    public Integer getStaffCount() {
        return this.staffCount;
    }

    public Workshop setStaffCount(Integer staffCount) {
        this.staffCount = staffCount;
        return this;
    }

    public Integer getAttendersCount() {
        return this.attendersCount;
    }

    public Workshop setAttendersCount(Integer attendersCount) {
        this.attendersCount = attendersCount;
        return this;
    }

    public List<Survey> getSurveys() {
        return this.surveys;
    }

    public Workshop addSurvey(Survey survey) {
        this.surveys.add(survey);
        return this;
    }

    /**
     * Generates a JSON representation of this instance.
     * @return The JSON String that represents the current state of this instance.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if (this.getKey() != null)
            builder.append("\"key\":\"").append(this.getKey()).append("\",");
        if (this.getPlaceName() != null)
            builder.append("\"place-name\":\"").append(this.getPlaceName()).append("\",");
        if (this.getTeamKey() != null)
            builder.append("\"team-key\":\"").append(this.getTeamKey()).append("\",");
        if (this.getInitTimestamp() != null)
            builder.append("\"init-timestamp\":").append(this.getInitTimestamp()).append(",");
        if (this.getAttendersCount() != null)
            builder.append("\"attenders-count\":").append(this.getAttendersCount()).append(",");
        if (this.getStaffCount() != null)
            builder.append("\"staff-count\":").append(this.getStaffCount()).append(",");
        if (this.getReportId() != null)
            builder.append("\"report-id\":\"").append(this.getReportId()).append("\",");
        return builder.substring(0, builder.length() - 1) + "}";
    }

    /**
     * Generates a Map (key/value) representation of this instance.
     * @return The Map that represents the current state of this instance.
     */
    public Map<String, Object> toMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        if (this.getKey() != null)
            map.put("_id", this.getKey());
        if (this.getPlaceName() != null)
            map.put("place-name", this.getPlaceName());
        if (this.getTeamKey() != null)
            map.put("team-key", this.getTeamKey());
        if (this.getInitTimestamp() != null)
            map.put("init-timestamp", this.getInitTimestamp());
        if (this.getAttendersCount() != null)
            map.put("attenders-count", this.getAttendersCount());
        if (this.getStaffCount() != null)
            map.put("staff-count", this.getStaffCount());
        if (this.getReportId() != null)
            map.put("report-id", this.getReportId());
        return map;
    }

    /**
     * Generates a Workshop instance from an instance of a Map (Key/Value).
     * @return This instance with the input map data.
     */
    public Workshop fromMap(Map<String, Object> map) {
        if (map.containsKey("_id")) this.setKey( map.get("_id").toString() );
        if (map.containsKey("place-name")) this.setPlaceName( map.get("place-name").toString() );
        if (map.containsKey("team-key")) this.setTeamKey( map.get("team-key").toString() );
        if (map.containsKey("init-timestamp")) this.setInitTimestamp( (Long) map.get("init-timestamp") );
        if (map.containsKey("attenders-count")) this.setAttendersCount((int)Double.parseDouble(map.get("attenders-count").toString()));
        if (map.containsKey("staff-count")) this.setStaffCount((int)Double.parseDouble(map.get("staff-count").toString()));
        if (map.containsKey("report-id")) this.setReportId( map.get("report-id").toString() );
        return this;
    }

}
