package com.cap.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cap.dao.UserCredentialRepository;
import com.cap.dao.UserInfoRepository;
import com.cap.entities.UserCredential;
import com.cap.entities.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Controller
//@RequestMapping("/api")
public class MainController {

    @Autowired
    private UserCredentialRepository credentialRepo;

    @Autowired
    private UserInfoRepository infoRepo;
    
    @GetMapping("/")
    public String getLogin()
    {
    	return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password,
                        Model model) throws JsonProcessingException {

        Optional<UserCredential> userCred = credentialRepo.findByEmailAndPassword(email, password);

        if (userCred.isPresent()) {
            Optional<UserInfo> userInfo = infoRepo.findByEmail(email);
            if (userInfo.isPresent()) {
            	
            	UserInfo info = userInfo.get();
 
            	// Prepare JSON string
                Map<String, String> jsonMap = new HashMap<>();
                jsonMap.put("firstName", info.getFirstName());
                jsonMap.put("lastName", info.getLastName());
                //jsonMap.put("userType", info.getUserType());

                ObjectMapper mapper = new ObjectMapper();
                String userJson = mapper.writeValueAsString(jsonMap);

                // Pass data to Thymeleaf view
                model.addAttribute("firstName", info.getFirstName());
                model.addAttribute("lastName", info.getLastName());
                model.addAttribute("userType", info.getUserType());
                model.addAttribute("email", info.getEmail());
                model.addAttribute("userJson", userJson);

                return "main"; // Renders main.html
            }
        }

        boolean userExists = credentialRepo.findByEmail(email).isPresent();
        if (userExists)
        {
            model.addAttribute("error", "Incorrect password.");
        } else {
            model.addAttribute("register", "User not exist - <a href='/register'>Please register</a>");
        }

        return "login"; // Back to login.html with error
    }

    
    @GetMapping("/register")
    public String getRegister()
    {
    	return "register";
    }

    @PostMapping("/create/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> userData) 
    {
        String email = userData.get("email");
        
        if (credentialRepo.findByEmail(email).isPresent()) {
            return ResponseEntity.badRequest().body("User already exists.");
        }

        UserCredential cred = new UserCredential();
        cred.setEmail(email);
        cred.setPassword(userData.get("password"));
        credentialRepo.save(cred);

        UserInfo info = new UserInfo();
        info.setEmail(email);
        info.setFirstName(userData.get("firstName"));
        info.setLastName(userData.get("lastName"));
        info.setMobileNumber(userData.get("mobileNumber"));
        
        if("Admin".equals(userData.get("userType")))
        {
            info.setUserType("A"); 
        }
        else
        {
        	 info.setUserType("U");
        }
        
        infoRepo.save(info);

        return ResponseEntity.ok("User registered successfully.");
    }
    

    @GetMapping("/show-users")
    public String getUserInfo(@RequestParam String email, Model model) {
        Optional<UserInfo> currentUser = infoRepo.findByEmail(email);

        if (currentUser.isPresent()) 
        {
            UserInfo user = currentUser.get();
            List<UserInfo> userList;

            if ("A".equals(user.getUserType())) 
            {
                userList = infoRepo.findAll();
            } 
            else 
            {
                userList = List.of(user);
            }

            // Add attributes to be used in Thymeleaf template
            model.addAttribute("userList", userList);
            model.addAttribute("userType", user.getUserType());
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("email", user.getEmail());

            return "output"; // main.html
        }

        model.addAttribute("error", "User not found");
        return "login";
    }

    @GetMapping("/download")
    public void downloadAllUsers(HttpServletResponse response) throws IOException {
        List<UserInfo> users = infoRepo.findAll();

        // Set response headers
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=users.csv");

        // Write CSV data
        PrintWriter writer = response.getWriter();
        writer.println("First Name,Last Name,Email,Mobile Number");

        for (UserInfo user : users) {
            writer.println(String.format("%s,%s,%s,%s",
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getMobileNumber()));
        }

        writer.flush();
        writer.close();
    }
	
}
