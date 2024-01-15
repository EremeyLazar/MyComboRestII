package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.AuthService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("")
    public String adminPanelPage (Model model) {
        User user = authService.getCurrentUser();
        Set<Role> userRoles = user.getRoles();
        model.addAttribute("sayhitoadmin", user.getUsername());
        model.addAttribute("userRole", userRoles);
        List<User> resultList = authService.getAll();
        model.addAttribute("userlist", resultList);
        model.addAttribute("userreg", new User());
        model.addAttribute("allRoles", authService.getAllRoles());
        return "admin";
    }
    @PostMapping("")
    public String createUser(@ModelAttribute("userreg") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam(value = "selectedRoles", required = false) List<Integer> selectedRoleIds,
                             Model model) {
        if (authService.isUserExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "User with that name already exists!");
            model.addAttribute("activeTab", "newUser");
            model.addAttribute("userlist", authService.getAll());
            model.addAttribute("allRoles", authService.getAllRoles());
            return "admin";
        } else if (bindingResult.hasErrors()) {
            model.addAttribute("activeTab", "newUser");
            model.addAttribute("userlist", authService.getAll());
            model.addAttribute("allRoles", authService.getAllRoles());
            return "admin";
        }
        authService.createUser(user, selectedRoleIds);
        return "redirect:/admin";
    }




//    @PostMapping(value = "")
//    public String createUser(@ModelAttribute("userreg") @Valid User user,
//                             BindingResult bindingResult,
//                             @RequestParam(value = "selectedRoles", required = false) List<Integer> selectedRoleIds) {
//        if (authService.isUserExists(user.getUsername())) {
//            bindingResult.rejectValue("username", "error.user", "User with that name already exists!");
//            return "admin";
//        } else if (bindingResult.hasErrors()) {
//            return "admin";
//        }
//        authService.createUser(user, selectedRoleIds);
//        return "redirect:/admin";
//    }

    @GetMapping(value = "/delete")
    public String deleteUserGetMethod (ModelMap model, @RequestParam("id") int id) {
        model.addAttribute("deluser", authService.getOne(id));
        return "delete";
    }

    @PostMapping("/delete")
    public String deleteUserPostMethod (@RequestParam("id") int userId) {
        authService.deleteUser(userId);
        return "redirect:/admin";
    }

    @GetMapping(value = "/update")
    public String updateUserGetMethod (ModelMap model, @RequestParam("id") int id) {
        model.addAttribute("upuser", authService.getOne(id));
        return "edit";
    }

    @PostMapping(value = "/update")
    public String updatePostMethod (@ModelAttribute("upuser") @Valid User updatedUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/edit";
        }
        authService.update(updatedUser, updatedUser.getId());
        return "redirect:/admin";
    }

}