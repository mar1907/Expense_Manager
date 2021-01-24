package com.posd.expensemanager.controller;

import com.posd.expensemanager.model.User;
import com.posd.expensemanager.service.user.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class LoginController {

    private final AuthenticationService authenticationService;

    public LoginController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = {"/","/home"}, method = RequestMethod.GET)
    public String home() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String index(HttpSession session) {
        session.setAttribute("user",null);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, params = "action=login")
    public String login(@RequestParam String username, @RequestParam String password, Model model, HttpSession httpSession) {
        User user;
        Optional<User> optionalUser = authenticationService.login(username,password);
        if(optionalUser.isEmpty()){
            model.addAttribute("registerText","Invalid login info");
            return "redirect:/login?error";
        } else {
            user = optionalUser.get();
            httpSession.setAttribute("user",user);

            return "redirect:/mainpage";
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, params = "action=register")
    public String register(@RequestParam String username, @RequestParam String password, Model model) {
        Boolean success = authenticationService.register(username,password);
        if(!success){
            model.addAttribute("registerText","Could not register");
        } else {
            model.addAttribute("registerText","Registered!");
        }

        return "login";
    }

}
