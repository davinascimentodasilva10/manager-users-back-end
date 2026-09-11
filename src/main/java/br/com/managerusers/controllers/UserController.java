package br.com.managerusers.controllers;

import br.com.managerusers.dtos.LoginDTO;
import br.com.managerusers.dtos.UserDTO;
import br.com.managerusers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(value = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDTO> getUsers() {
        return userService.getUsers();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userService.createUser(userDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(user);
    }

    @PostMapping(value = "login")
    public ResponseEntity<LoginDTO> login(@RequestBody UserDTO userDTO) throws UnsupportedEncodingException {
        LoginDTO loginDTO = userService.login(userDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loginDTO);
    }

}
