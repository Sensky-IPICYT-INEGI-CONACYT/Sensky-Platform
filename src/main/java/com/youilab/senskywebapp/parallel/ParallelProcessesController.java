package com.youilab.senskywebapp.parallel;

import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.util.FileHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class ParallelProcessesController {

    private ScheduledExecutorService executor;
    private final Integer delay = 15; //15 seg
    private final Integer period = 1800; //30 min

    // [ DISABLED ]
    // When application started, this creates parallel threads for executing the data collection scripts.
    // @PostConstruct
    public void init() {
        System.out.println("[SenSky] Starting parallel processes");
        executor = Executors.newScheduledThreadPool(1);
        // Read parallel processes file
        String json = new FileHandler().readFile(SetUp.PARALLEL_PROCESSES);
        try {
            JSONArray tasksList = (JSONArray) new JSONParser().parse(json);
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(tasksList.size());
            tasksList.forEach( rawTask -> {
                JSONObject task = (JSONObject) rawTask;
                StringBuilder command = new StringBuilder();
                command.append(task.get("command").toString()).append(" ");
                command.append(task.get("path").toString()).append(" ");
                ((JSONArray) task.get("params")).forEach( param -> command.append(param.toString()).append(" "));
                ParallelTask parallelTask = new ParallelTask(command.toString(), task.get("name").toString());
                scheduler.scheduleAtFixedRate(parallelTask, delay, period, TimeUnit.SECONDS);
            });
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("[SenSky] Cannot parse JSON from parallel tasks");
        }



    }

    // [ DISABLED ]
    // When application stops, the creation of parallel threads is disabled too.
    // @PreDestroy
    public void destroy() {
        System.out.println("[SenSky] Shutting down parallel processes");
    }
}
