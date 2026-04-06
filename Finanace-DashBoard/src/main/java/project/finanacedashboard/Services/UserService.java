package project.finanacedashboard.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.finanacedashboard.DTO.Request.UserRequest;
import project.finanacedashboard.DTO.Response.UserResponse;
import project.finanacedashboard.Entity.Role;
import project.finanacedashboard.Entity.User;
import project.finanacedashboard.ExceptionHandler.DataNotFound;
import project.finanacedashboard.ExceptionHandler.DataViolationIntegrity;
import project.finanacedashboard.ExceptionHandler.DataAlreadyExist;
import project.finanacedashboard.Repository.RoleRepository;
import project.finanacedashboard.Repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder ,  RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    //create user
    public UserResponse createUser (UserRequest userRequest){
        Optional<User>  user = userRepository.findByUsername(userRequest.getUsername());
        Optional<Role> role = roleRepository.findByRole(userRequest.getRole());

        if(user.isPresent()){
            throw new DataAlreadyExist("Username already exist");
        }

        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new DataAlreadyExist("Email already exist");
        }

        if(!role.isPresent()){
            throw new DataNotFound("Role do not exist!");
        }

        User user1 = new User();
        user1.setUsername(userRequest.getUsername());
        user1.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user1.setEmail(userRequest.getEmail());
        user1.setRole(role.get());
        user1.setCreatedAt(LocalDateTime.now());
        userRepository.save(user1);

        return new UserResponse(user1,"User created successfully");

    }

    //Login User



}
