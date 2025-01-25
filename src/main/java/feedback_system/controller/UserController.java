package feedback_system.controller;

import java.security.Principal;
import java.util.Map;

import feedback_system.dto.RoleDto;
import feedback_system.dto.UserDto;
import feedback_system.entity.User;
import feedback_system.service.UserService;
import feedback_system.utility.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    @Autowired
    private UserDetailsService userDetailsService;

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/login")
    public ApiResponse login(@RequestHeader Map<String,String> header,@RequestBody UserDto userDto) {
       String name =  header.get("username");
       System.out.println("/login called");
        //UserDetails userDetails = userDetailsService.loadUserByUsername(header.get("username"));
        return userService.login(userDto);
    }

    @PostMapping("/user/create")
    public ApiResponse createUser(@RequestBody UserDto userDto) {
        System.out.println(userDto.getRoles());
        return userService.createUserByAdmin(userDto);
    }

    @PostMapping("/user/delete")
    public ApiResponse delete(@RequestParam(name = "id") Long id) {
        System.out.println(" /delete call for id : " + id);
        return userService.deleteUser(id);
    }

    @PostMapping("/user/update")
    public ApiResponse updateUser(@RequestBody UserDto userDto) {
        System.out.println(" /update call");
        return userService.updateUser(userDto);
    }

    @PostMapping("/user/assign-role")
    public ApiResponse assignRole(@RequestBody RoleDto roleDto) {

        return userService.addRoleToUser(roleDto);
    }

    @GetMapping("/user/fetch")
    public ApiResponse fetchAllUser() {

        return userService.findAllUsers();
    }
    @PostMapping("/user/change-password")
    public ApiResponse changePassword(@RequestBody UserDto userDto) {

        return userService.changePassword(userDto);
    }

}