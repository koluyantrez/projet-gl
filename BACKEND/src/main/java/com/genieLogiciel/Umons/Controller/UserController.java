package com.genieLogiciel.Umons.Controller;

import com.genieLogiciel.Umons.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/{id}/image")
    public ResponseEntity<String> getUserImage(@PathVariable Long id) {
        return userService.getUserImage(id);
    }

    @PutMapping("/{matricule}/address")
    public ResponseEntity<String> updateUserAddress(@PathVariable Long matricule, @RequestBody String newAddress) {
        // Log the received address for debugging
        System.out.println("Received address: " + newAddress);
        return userService.updateUserAddress(matricule, newAddress);
    }

    @PutMapping("/{matricule}/phone")
    public ResponseEntity<String> updateUserPhoneNumber(@PathVariable Long matricule, @RequestBody Long newPhoneNumber) {
        // Log the received phone number for debugging
        System.out.println("Received phone number: " + newPhoneNumber);
        return userService.updateUserPhoneNumber(matricule, newPhoneNumber);
    }

}
