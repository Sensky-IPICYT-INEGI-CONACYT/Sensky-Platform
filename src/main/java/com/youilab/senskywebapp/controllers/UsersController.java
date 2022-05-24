package com.youilab.senskywebapp.controllers;

import com.youilab.senskywebapp.daos.TeamDAO;
import com.youilab.senskywebapp.daos.WorkshopDAO;
import com.youilab.senskywebapp.entities.EntityException;
import com.youilab.senskywebapp.entities.Team;
import com.youilab.senskywebapp.entities.Workshop;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UsersController {

    /**
     * This route handles the login of an already registered user (the leaders of a team).
     * @param email is the e-mail that the user gave when signed up.
     * @param password is the registered password.
     * @return the redirect command according to the given credentials.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(
            @RequestParam(name = "team-email") String email,
            @RequestParam(name = "team-password") String password,
            HttpSession session, //This object holds the session data for the current request.
            ModelMap modelMap //This encapsulates the data passed from this method to the next view.
    ){
        try {
            // Tries to find a user with the given data
            if ( new TeamDAO().find( new Team().setEmail(email).setPassword(password)) != null ) {
                session.setAttribute("user-name", email);
                session.setAttribute("role", "team");
                return "redirect:/team-dashboard";
            } else {
                modelMap.addAttribute("code", "TEAM_NOT_FOUND");
                modelMap.addAttribute("type", "error");
                return "forward:/index";
            }
        } catch (EntityException e) {
            e.printStackTrace();
            modelMap.addAttribute("type", "error");
            modelMap.addAttribute("code", "INVALID_DATA");
            return "forward:/index";
        }
    }

    /**
     * This route evaluates an existing sessión and deploys the corresponding view.
     * @return the redirection to the dashboard if a session is active, if not it redirects to the home page.
     */
    @RequestMapping(value = "/team-dashboard", method = RequestMethod.GET)
    public String getTeamDashboard( HttpSession session, ModelMap modelMap ) {
        if (session.getAttribute("user-name") != null) {
            String currentTeamEmail = session.getAttribute("user-name").toString();
            List<Workshop> teamWorkshops = new WorkshopDAO().findByAttribute(new Workshop().setTeamKey(currentTeamEmail));
            return "team-dashboard";
        }
        else {
            modelMap.addAttribute("type", "message");
            modelMap.addAttribute("code", "LOGIN_FIRST");
            return "forward:/home";
        }

    }

    /**
     * This route receives the data sent by someone who filled the sign up form and tries to add these to the database.
     * @param email The given email that represent the whole team.
     * @param password The given password.
     * @param leader The name of the leader.
     * @param name The name of the team.
     * @return the redirect to the dashboard for the team if the sign up attemp was successful, otherwise to home.
     */
    @RequestMapping(value = "/sign-up", method = RequestMethod.POST)
    public String signUp(@RequestParam(name = "team-email") String email,
                         @RequestParam(name = "team-password") String password,
                         @RequestParam(name = "team-leader") String leader,
                         @RequestParam(name = "team-name") String name,
                         HttpSession session, ModelMap modelMap) {
        try {
            Team newTeam = new Team()
                    .setName(name)
                    .setPassword(password)
                    .setEmail(email)
                    .setLeader(leader);
            new TeamDAO().insert(newTeam);
            session.setAttribute("user-name", newTeam.getEmail());
            session.setAttribute("role", "team");
            return "redirect:/team-dashboard";
        } catch (EntityException e) {
            e.printStackTrace();
            modelMap.addAttribute("type", "error");
            modelMap.addAttribute("code", "INVALID_DATA");
            return "forward:/home";
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute("type", "error");
            modelMap.addAttribute("code", "DUPLICATED_EMAIL");
            return "forward:/home";
        }
    }

    /**
     * This route destroys the current session for a team and redirects to the home page.
     * @param session
     * @return A redirection command to the index.
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpSession session) {
        session.removeAttribute("user-name");
        session.removeAttribute("role");
        return "redirect:/home";
    }


    /**
     * Delivers the view for adding a new Workshop if a session is active, otherwise it redirects to the home page.
     * @return if a sessión is already set, delivers the corresponding view, other wise redirects to the home page.
     */
    @RequestMapping(value = "/new-workshop", method = RequestMethod.GET)
    public String newWorkshop(HttpSession session, ModelMap modelMap) {
        if (session.getAttribute("user-name") != null) return "new-workshop";
        else {
            modelMap.addAttribute("type", "message");
            modelMap.addAttribute("code", "LOGIN_FIRST");
            return "forward:/home";
        }
    }


    /**
     *
     * @param placeName The name of the place where the Workshop will be delivered.
     * @param timestamp The millis representation of the DateTime for the Workshop.
     * @param staffNumber The number of people that will be part of the team.
     * @param attendersNumber The number of people that will take the Workshop.
     * @return a redirection order.
     */
    @RequestMapping(value = "/create-workshop", method = RequestMethod.POST)
    public String createWorkshop (
            HttpSession session,
            @RequestParam("workshop-place-name") String placeName,
            @RequestParam("workshop-timestamp") String timestamp,
            @RequestParam("workshop-staff") String staffNumber,
            @RequestParam("workshop-attenders") String attendersNumber,
            ModelMap modelMap
    ) {
        if ( session.getAttribute("user-name") == null ){
            modelMap.addAttribute("type", "message");
            modelMap.addAttribute("code", "LOGIN_FIRST");
            return "forward:/home";
        }
        Workshop tmp = new Workshop()
                .setPlaceName(placeName)
                .setTeamKey(session.getAttribute("user-name").toString())
                .setInitTimestamp(Long.parseLong(timestamp))
                .setStaffCount(Integer.parseInt(staffNumber))
                .setAttendersCount(Integer.parseInt(attendersNumber));

        if (new WorkshopDAO().insert(tmp)) {
            modelMap.addAttribute("type", "message");
            modelMap.addAttribute("code", "WORKSHOP_CREATED");
            return "redirect:/team-dashboard";
        } else {
            modelMap.addAttribute("type", "error");
            modelMap.addAttribute("code", "CANNOT_CREATE_WORKSHOP");
            return "forward:/new-workshop";
        }
    }
}
