package project.finanacedashboard.Controller.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import project.finanacedashboard.DTO.Request.UserRequest;
import project.finanacedashboard.DTO.Response.UserResponse;
import project.finanacedashboard.Services.UserService;

@RestController("/auth")
public class Auth {

    private  final UserService userService;
    @Autowired
    public Auth(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public UserResponse signup(@RequestBody UserRequest userRequest){
        return userService.createUser(userRequest);
    }



}
