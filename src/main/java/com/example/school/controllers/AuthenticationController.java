package com.example.school.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.example.school.entities.MainTokens;
import com.example.school.entities.Person;
import com.example.school.entities.Studente;
import com.example.school.entities.Teacher;
import com.example.school.services.PersonService;
import com.example.school.services.StudentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.school.services.TeacherService;
import com.example.school.utils.JwtUtils;

@RestController
public class AuthenticationController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private PersonService personService;
    @Autowired
    private JwtUtils jwtUtils;
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Person person) {
        if (personService.addPerson(person)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Person registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error registering student");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {

        MainTokens mainTokens = personService.login(username, password);
        if (mainTokens == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error login");
        }
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("Refresh Token:").append(mainTokens.getRefreshToken()).append("\nAccess Token:").append(mainTokens.getAccessToken());
        return ResponseEntity.status(HttpStatus.OK).body(stringBuilder.toString());
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String refreshToken) {
        // Перевіряємо, чи є refreshToken валідним
        if (jwtUtils.validateRefreshToken(refreshToken)) {
            String username = jwtUtils.extractUsernameOfRefreshToken(refreshToken);
            Person person = personService.findByUsername(username);
            
            if (person == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }

            // Генеруємо новий accessToken
            String newAccessToken = jwtUtils.generateAccessToken(username);

            // Повертаємо новий accessToken
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("Access Token:"+newAccessToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(stringBuilder.toString());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
