package com.genieLogiciel.Umons.service;

import com.genieLogiciel.Umons.auth.service.EmailDomain;
import com.genieLogiciel.Umons.model.Personnel;
import com.genieLogiciel.Umons.model.Professeur;
import com.genieLogiciel.Umons.model.Student;
import com.genieLogiciel.Umons.model.User;
import com.genieLogiciel.Umons.repository.PersonnelRepository;
import com.genieLogiciel.Umons.repository.UserRepository;
import com.genieLogiciel.Umons.util.PersonnelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_@";
        StringBuilder password = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int randomIndex = random.nextInt(characters.length());
            password.append(characters.charAt(randomIndex));
        }
        return password.toString();
    }

    public ResponseEntity<String> addUser(User newUser, EmailDomain emailDomain) {
        // Vérification de l'existence de l'utilisateur
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getFirstName().equals(newUser.getFirstName()) && user.getLastName().equals(newUser.getLastName())) {
                return new ResponseEntity<>("User already exists.", HttpStatus.BAD_REQUEST);
            }
        }

        // Génération du nom et du mot de passe
        newUser.setName(newUser.getFirstName()+newUser.getLastName());
        newUser.setPassword(generatePassword());
        // Sauvegarde de l'utilisateur
        userRepository.save(newUser);
        newUser.setEmail(newUser.getMatricule() + emailDomain.getDomain());
        userRepository.save(newUser);

        return new ResponseEntity<>("User added successfully.", HttpStatus.OK);
    }

    public void uploadUserImage(Long matricule, MultipartFile file) {
        Optional<User> userOptional = userRepository.findById(matricule);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            try {
                byte[] imageData = file.getBytes();
                user.setImage(imageData);
                userRepository.save(user);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to process image", e);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }




    public ResponseEntity<String> getUserImage(Long matricule) {
        Optional<User> userOptional = userRepository.findById(matricule);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String base64Image = Base64.getEncoder().encodeToString(user.getImage());
            return new ResponseEntity<>(base64Image, HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> updateUserAddress(Long matricule, String newAddress) {
        Optional<User> userOptional = userRepository.findById(matricule);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAdresse(newAddress);
            userRepository.save(user);
            return new ResponseEntity<>("Address updated successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<String> updateUserPhoneNumber(Long matricule, Long newPhoneNumber) {
        Optional<User> userOptional = userRepository.findById(matricule);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNumero(newPhoneNumber);
            userRepository.save(user);
            return new ResponseEntity<>("Phone number updated successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }
}
