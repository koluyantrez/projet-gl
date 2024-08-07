package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.model.User;
import com.genieLogiciel.Umons.service.UserService;
import com.genieLogiciel.Umons.util.PasswordChangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/users")
@CrossOrigin("http://localhost:8080")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<String> uploadUserImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        userService.uploadUserImage(id, file);
        return ResponseEntity.ok("Image uploaded successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<String> getUserImage(@PathVariable Long id) {
        return userService.getUserImage(id);
    }

    @PutMapping("/{matricule}/address")
    public ResponseEntity<String> updateUserAddress(
            @PathVariable Long matricule,
            @RequestParam String newAddress) {
        // Log the received address for debugging
        System.out.println("Received address: " + newAddress);
        return userService.updateUserAddress(matricule, newAddress);
    }

    @PutMapping("/{matricule}/phone")
    public ResponseEntity<String> updateUserPhoneNumber(
            @PathVariable Long matricule,
            @RequestParam Long newPhoneNumber) {
        // Log the received phone number for debugging
        System.out.println("Received phone number: " + newPhoneNumber);
        return userService.updateUserPhoneNumber(matricule, newPhoneNumber);
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<String> updateUserPassword(@PathVariable Long id , @RequestParam String newPassword){
        return userService.updateUserPassword(id,newPassword);
    }
}
