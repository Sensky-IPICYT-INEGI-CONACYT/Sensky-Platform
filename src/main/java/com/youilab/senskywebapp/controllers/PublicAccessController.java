package com.youilab.senskywebapp.controllers;

import com.youilab.senskywebapp.SetUp;
import com.youilab.senskywebapp.util.FileHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Controller

public class PublicAccessController {

    // Route for the index
    @RequestMapping(value = {
            "/",
            "/home",
            "/index",
            "/start"
    })
    public String getHomePage(){
        return "index";
    }

    // Route for the Science dashboard page
    @RequestMapping(value = "science")
    public String getScienceDashboard(ModelMap model){
        model.addAttribute("surveysData", readSurveysResume());
        return "science-dashboard";
    }

    /**
     * This function retrieves the content of the surveys resume file, which has a CSV format.
     * @return The String content of the file in a single line.
     */
    private String readSurveysResume() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(SetUp.SURVEYS_RESUME));
            StringBuilder tmpString = new StringBuilder();
            String tmpLine;
            while ((tmpLine = reader.readLine()) != null) tmpString.append(tmpLine).append("||");
            return tmpString.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("[SenSky] Cannot get the surveys resume");
            return "[]";
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("[SenSky] Cannot read the surveys resume");
            return "[]";
        }
    }
}
