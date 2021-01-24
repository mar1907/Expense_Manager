package com.posd.expensemanager.controller;

import com.posd.expensemanager.model.*;
import com.posd.expensemanager.repository.*;
import com.posd.expensemanager.service.expense.ExpenseService;
import com.posd.expensemanager.service.message.MessageService;
import com.posd.expensemanager.service.project.ProjectService;
import com.posd.expensemanager.service.travelorder.TravelOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainpageController {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ActionRepository actionRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TravelOrderService travelOrderService;
    @Autowired
    private TravelOrderRepository travelOrderRepository;
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/mainpage/logout", method = RequestMethod.GET)
    public String logout(HttpSession session)
    {
        return "redirect:/";
    }

    @RequestMapping(value = "/mainpage/report", method = RequestMethod.GET)
    public String report(Model model, HttpSession session)
    {
        if(!isLogged(session)){
            return "redirect:/";
        }

        User user = ((User)session.getAttribute("user"));
        if(!user.getRole().getName().equals("supervisor")) {
            return "redirect:/";
        }

        model.addAttribute("projectReports", projectService.getProjectReports(user));

        return "report";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.GET)
    public String index(Model model, HttpSession session) {
        if(!isLogged(session)){
            return "redirect:/";
        }

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=createproj")
    public String createProject(@RequestParam String pname,
                                @RequestParam String budget,
                                @RequestParam String budget_fraction,
                                @RequestParam String budget_fraction_travel,
                                @RequestParam String clerk,
                                Model model,
                                HttpSession session)
    {
        User user = ((User)session.getAttribute("user"));

        Boolean success = projectService.addProject(
                pname, Integer.parseInt(budget), Double.parseDouble(budget_fraction),
                Double.parseDouble(budget_fraction_travel), clerk, user);

        if (success)
            model.addAttribute("projectResult", "Success!");
        else
            model.addAttribute("projectResult", "Failed to add project");

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=addusertoproj")
    public String addUserToProject(@RequestParam String project_for_add, @RequestParam String user_for_add,
                                   Model model, HttpSession session)
    {
        String result = projectService.addUserToProject(user_for_add, project_for_add);

        model.addAttribute("projectAddUserResult", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=createtravel")
    public String createTravelOrder(@RequestParam String budgettravel, @RequestParam String destination, @RequestParam String project, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        String result = travelOrderService.addTravelOrder(Integer.parseInt(budgettravel), destination, project, user);

        model.addAttribute("travelResult", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=approvetravelclerk")
    public String approveTravelClerk(@RequestParam String toid, Model model, HttpSession session){
        User user = ((User) session.getAttribute("user"));

        String result = travelOrderService.approveClerkTravelOrder(Integer.parseInt(toid), user);

        model.addAttribute("approve_travel_clerk_result", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=rejecttravelclerk")
    public String rejectTravelClerk(@RequestParam String toid, @RequestParam String rejectreason_travelclerk, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        String result = travelOrderService.rejectClerkTravelOrder(Integer.parseInt(toid), rejectreason_travelclerk, user);

        model.addAttribute("approve_travel_clerk_result", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=approvetravelsupervisor")
    public String approveTravelSupervisor(@RequestParam String toid_s, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        String result = travelOrderService.approveSupervisorTravelOrder(Integer.parseInt(toid_s), user);

        model.addAttribute("approve_travel_clerk_result", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=rejecttravelsupervisor")
    public String rejectTravelSupervisor(@RequestParam String toid_s, @RequestParam String rejectreason_travelsupervisor, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        String result = travelOrderService.rejectSupervisorTravelOrder(Integer.parseInt(toid_s), rejectreason_travelsupervisor, user);

        model.addAttribute("approve_travel_supervisor_result", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=createexpense")
    public String createExpense(@RequestParam String expensedocs, @RequestParam String torder, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        Integer travelOrderId = Integer.parseInt(torder.split(" ")[0]);

        String result = expenseService.addExpense(travelOrderId, expensedocs, user);

        model.addAttribute("expenseResult", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=approveexpense")
    public String approveExpense(@RequestParam String exp, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        String result = expenseService.approveExpense(Integer.parseInt(exp), user);

        model.addAttribute("approve_expense", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=rejectexpense")
    public String rejectExpense(@RequestParam String exp, @RequestParam String rejectreason_expenseclerk, Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        String result = expenseService.rejectExpense(Integer.parseInt(exp), rejectreason_expenseclerk, user);

        model.addAttribute("approve_expense", result);

        addData(model, session);

        return "mainpage";
    }

    @RequestMapping(value = "/mainpage", method = RequestMethod.POST, params = "action=markasread")
    public String markAsRead(@RequestParam String msg, Model model, HttpSession session) {
        messageService.markAsRead(msg);

        addData(model, session);

        return "mainpage";
    }

    private void addData(Model model, HttpSession session) {
        User user = ((User) session.getAttribute("user"));

        // define which sections are visible, based on the role+action
        for (Action a: actionRepository.findAll()) {
            model.addAttribute(a.getName(), "false");
        }
        for (Action a: user.getRole().getActionList()) {
            model.addAttribute(a.getName(), "true");
        }

        // add list of projects as appropriate
        model.addAttribute("projects",
                projectService.findProjectsForUser(user).stream().
                        map(Project::toCompleteString).
                        collect(Collectors.toList()));

        // add list of clerks
        Role clerkRole = roleRepository.findByName("clerk").get(0);
        model.addAttribute("clerks", userRepository.findByRole(clerkRole));

        // add list of users and projects
        Role userRole = roleRepository.findByName("user").get(0);
        model.addAttribute("user_for_add_list", userRepository.findByRole(userRole));
        model.addAttribute("project_for_add_list", projectRepository.findAll());

        // add list of projects for travel order creation
        model.addAttribute("projects_string", projectService.findProjectsForUser(user));

        // add list of user's travel orders
        model.addAttribute("travels", travelOrderRepository.findByRequester(user));

        // add list of travel orders for clerk
        List<String> formattedTravelOrdersForClerk = travelOrderService.getFormattedTravelOrdersForClerk(user);
        model.addAttribute("travels_clerk", formattedTravelOrdersForClerk);

        // add list of travel order ids for clerk
        model.addAttribute("travel_order_ids_clerk",
                formattedTravelOrdersForClerk.stream().map(s -> s.split(" ")[0]).collect(Collectors.toList()));

        // add list of travel orders for supervisor
        List<String> formattedTravelOrdersForSupervisor = travelOrderService.getFormattedTravelOrdersForSupervisor(user);
        model.addAttribute("travels_supervisor", formattedTravelOrdersForSupervisor);

        // add list of travel order ids for clerk
        model.addAttribute("travel_order_ids_supervisor",
                formattedTravelOrdersForSupervisor.stream().map(s -> s.split(" ")[0]).collect(Collectors.toList()));

        // add list of travel orders for which expenses can be created
        model.addAttribute("torders", travelOrderService.findByRequesterAndStatusNoExpense(user, "approved"));

        // add list of expense of a user
        model.addAttribute("expenses", expenseService.findAllForUser(user));

        // add list of expenses to be reviewed by clerk
        List<String> formattedExpenses = expenseService.getFormattedExpenses(user);
        model.addAttribute("expenses_clerk", formattedExpenses);

        // add list of expense ids for clerk
        model.addAttribute("expenses_clerk_ids",
                formattedExpenses.stream().map(s -> s.split(" ")[0]).collect(Collectors.toList()));

        // add list of messages for the user
        model.addAttribute("messages", messageService.getMessagesForUser(user));

        // add ids of messages to be marked as read
        model.addAttribute("messages_mark", messageService.getMessagesIds(user));
    }

    private boolean isLogged(HttpSession session){
        User user = (User) session.getAttribute("user");
        return user != null;
    }

}
