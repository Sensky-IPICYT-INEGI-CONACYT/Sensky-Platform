package com.youilab.senskywebapp.api;

import com.mongodb.client.MongoCollection;
import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.daos.*;
import com.youilab.senskywebapp.database.MongoDBCRUD;
import com.youilab.senskywebapp.database.MongoDBSensors;
import com.youilab.senskywebapp.entities.*;
import com.youilab.senskywebapp.util.*;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RestController

// Allow CORS headers for http requests.
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})

@RequestMapping(value = "/api/v1")
public class APIController {

    /**
     * Use this route to get the available surveys' description, which includes title, path, id and more.
     * @return The available surveys' description in JSON format.
     */
    @GetMapping(value = "/surveys", produces = MediaType.APPLICATION_JSON)
    public String getAvailableSurveys() {
        return new SurveyDAO().findAll()
                .stream()
                .map(Survey::toString)
                .collect(Collectors.joining("," , "[","]"));
    }

    /**
     * The data related to Workshop identified by a given Key.
     * @param workshopKey The identifier of a Workshop.
     * @return The JSON representation of the Workshop.
     */
    @GetMapping("/workshop/{workshop-key}")
    public String getWorkshopData(@PathVariable("workshop-key") String workshopKey) {
        Workshop found = new WorkshopDAO().find(new Workshop().setKey(workshopKey));
        if (found != null) {
            try {
                JSONObject json = (JSONObject) new JSONParser().parse(found.toString());
                JSONArray jsonSurveys = new JSONArray();
                List<Survey> surveys = new SurveyDAO().findAll();
                for (Survey survey : surveys) jsonSurveys.add(new JSONParser().parse(survey.toString()));
                json.put("surveys", jsonSurveys);
                return json.toJSONString();
            } catch (ParseException e) {
                return "{\"error\":\"JSON_SERVER_ERROR\"}";
            }
        } else return "{\"error\":\"WORKSHOP_NOT_FOUND\"}";
    }

    /**
     * Use this route to get the Workshops registered by a given team.
     * @param teamKey The identifier of a team.
     * @return The JSON representation of a list containing each workshop registered by the given team.
     */
    @GetMapping("/{team-key}/workshops/get")
    public String getWorkshopsByTeam(@PathVariable("team-key") String teamKey) {
        String email = Encoders.fromBase64(teamKey);
        return new WorkshopDAO().findByAttribute(
                new Workshop().setTeamKey(email)
        ).stream()
                .map(Workshop::toString)
                .collect(Collectors.joining(",", "[", "]"));

    }

    /**
     * Use this route to register a new evidence in the Data Base.
     * @param file Is the XML file that contains the survey answers of a person (Optional).
     * @param photo Is an Image that represents the context of the environment.
     * @param evidenceData Is the description of the evidence, includes the geograpgical position and others.
     * @return SUCCESS if the evidence was registered, FAILED_TO_INSERT_EVIDENCE otherwise.
     */
    @PostMapping("/evidences/append")
    public String uploadEvidence(@RequestParam(name = "file", required = false) MultipartFile file,
                                 @RequestParam(name = "photo", required = false) MultipartFile photo,
                                 @RequestParam(name = "raw-data") String evidenceData) throws Exception {
        System.out.println("Evidence data => " + evidenceData);
        JSONObject data = (JSONObject) new JSONParser().parse(Encoders.fromBase64(evidenceData));
        Evidence evidence = new Evidence();
        if (file != null && file.getSize() > 0)
            evidence.setSurveyPath(new FileHandler(file, "survey", data.get("avatar").toString()).getFilePath());
        if (photo != null && photo.getSize() > 0)
            evidence.setPhotoPath(new FileHandler(photo, "image", data.get("avatar").toString()).getFilePath());
        evidence.setWorkshopKey(data.get("workshop-id").toString())
                .setCapturedTimestamp(Long.parseLong(data.get("timestamp").toString()))
                .setStoredTimestamp(System.currentTimeMillis())
                .setLatitude(Double.parseDouble(data.get("latitude").toString()))
                .setLongitude(Double.parseDouble(data.get("longitude").toString()))
                .setAvatar(data.get("avatar").toString());
        if (data.get("altitude") != null)
            evidence.setAltitude(Double.parseDouble(data.get("altitude").toString()));
        if (data.get("tags") != null)
            ((JSONArray) data.get("tags")).forEach((tag) -> evidence.addTag(tag.toString()));
        if (data.get("survey-id") != null) {
            evidence.setSurveyKey(data.get("survey-id").toString());
        }
        if (data.get("comment") != null) evidence.setComment(data.get("comment").toString());
        return (new EvidenceDAO().insert(evidence))
                ? "SUCCESS"
                : "FAILED_TO_INSERT_EVIDENCE";
    }

    /**
     * Use this route for appending a final report structure (in JSON format) to the workshop identified by a given key.
     * @param report A JSON structure representing the final report to attach.
     * @param workshopKey The identifier of the workshop.
     * @return SUCCESS if the report was inserted and linked to the workshop, FAIL otherwise.
     */
    @PostMapping("/report/{key}/append")
    public String appendReport(@RequestParam(name = "report") String report, @PathVariable("key") String workshopKey) {
        Workshop currentWorkshop = new Workshop().setKey(workshopKey);
        String stringReport = Encoders.fromBase64(report);
        stringReport = "{\"report\":" + stringReport + "}";
        Document doc = Document.parse(stringReport);
        String reportId = MongoDBCRUD.insertOneDocument(doc, SetUp.REPORTS_COLLECTION);
        if (reportId == "null") return "FAIL";
        System.out.println("DEV => Reporte insertado: " + reportId);
        MongoDBCRUD.updateDocument(
                currentWorkshop.toMap(),
                new Workshop().setReportId(reportId).toMap(),
                SetUp.WORKSHOPS_COLLECTION
        );
        System.out.println("DEV => Workshop (" + workshopKey + ") actualizado");
        return "SUCCESS";
    }

    /**
     * This route is for retrieving the last 1000 records in database for the Flow Sensors.
      * @return A JSON list containing pollutant records.
     */
    @GetMapping("/plumelabs")
    public String getPlumeLabsData() {
        MongoCollection<Document> collection = MongoDBSensors.getInstance().getCollection("PlumePollutants");
        return StreamSupport.stream(collection.find().sort(new Document("_id", -1)).limit(1000).spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * This route is for retrieving the last 1000 records in database for the custom IPICYT sensors.
     * @return A JSON list containing pollutant records.
     */
    @GetMapping("/ipiestacion")
    public String getIPIEstacionData() {
        MongoCollection<Document> collection = MongoDBSensors.getInstance().getCollection("IPIEstacion");
        return StreamSupport.stream(collection.find().sort(new Document("_id", -1)).limit(1000).spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    /**
     * This route is used by the sensors at the IPICYT, the data is registered to a data layer.
     * It's mandatory for the data to have the following attributes: Sensor, Tiempo and, at least, an extra attribute.
     * @param data A JSON List containing sensor measures.
     * @return A JSON response containing details about the data insertions.
     */
    @PostMapping(value = "/ipiestacion")
    public String postToIPIEstacion(@RequestParam(name = "data") String data) {
        try {
            StringBuilder jsonResponse = new StringBuilder().append("{ \"response\":\"RECEIVED\",\"message\":");
            JSONArray measures = ((JSONArray) new JSONParser().parse(data));
            List<Object> genericMeasures = (List<Object>) measures;
            List<JSONObject> validMeasures = genericMeasures
                    .stream()
                    .map( rawMeasure ->{
                        List<String> keysToRemove = new ArrayList<>();
                        JSONObject measure = (JSONObject) rawMeasure;
                        for (Object keyObject : measure.keySet()) {
                            String currentKey = keyObject.toString();
                            if (currentKey.contains(".")) {
                                measure.put(currentKey.replace(".", ","), measure.get(currentKey));
                                keysToRemove.add(currentKey);
                            }
                        }
                        keysToRemove.forEach(measure::remove);
                        return measure;
                    })
                    .filter( measure -> measure.containsKey("Sensor") && measure.containsKey("Tiempo") && measure.keySet().size() > 2)
                    .collect(Collectors.toList());
            validMeasures.forEach( measure -> {
                try {
                    MongoDBSensors.insertDocument(measure, "IPIEstacion");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            if (validMeasures.size() == measures.size()) jsonResponse.append("\"all done\"}");
            else jsonResponse.append("\"Some values were not processed, please check the structure.\"}");
            return jsonResponse.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "{\"response\":\"ERROR\", \"message\":\"Cannot parse, please check the JSON structure\"}";
        }
    }

    /**
     * This route is for retrieving the last 1000 records in database for the Sinaica data layer.
     * @return A JSON list containing pollutant records.
     */
    @GetMapping("/sinaica")
    public String getSinaicaData() {
        MongoCollection<Document> collection = MongoDBSensors.getInstance().getCollection("sinaica");
        return StreamSupport.stream(collection.find().sort(new Document("_id", -1)).limit(1000).spliterator(), false)
                .map(Document::toJson)
                .collect(Collectors.joining(", ", "[", "]"));
    }

}


