package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.school.entities.MainTokens;
import com.example.school.entities.Person;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.utils.JwtUtils;

@Service
public class PersonService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository; // Repository for Person entity
    @Autowired
    private JwtUtils jwtUtils; // Utility for JWT operations
    @Autowired
    private RoleRepository roleRepository; // Repository for Role management
    @Autowired
    private PasswordEncoder passwordEncoder; // For password encryption

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username);
        if(person==null)
        {
        throw new UsernameNotFoundException(username);
        }
        return User.builder()
                .username(person.getUsername())
                .password(person.getPassword())
                .authorities(person.getRole().getName())
                .build();
    }

    public boolean addPerson(Person person) {
        String roleName = person.getRole().getName();
        // Check if the username already exists or the role is invalid
        if (personRepository.existsByUsername(person.getUsername()) || !roleRepository.existsByName(roleName)) {
            return false;
        } else {
            person.setRole(roleRepository.findByName(roleName));
            // Encode the password before saving
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            personRepository.save(person);
            return true; // Person added successfully
        }
    }

    public MainTokens login(String username, String password) {
        MainTokens mainToken = null;
        if (checkPerson(username, password)) {
            mainToken = new MainTokens();
            mainToken.setAccessToken(jwtUtils.generateAccessToken(username));
            mainToken.setRefreshToken(jwtUtils.generateRefreshToken(username));
        }
        return mainToken; // Return tokens if login is successful
    }

    public boolean checkPerson(String username, String password) {
        // Check if the user exists and validate the password
        if (personRepository.existsByUsername(username)) {
            Person person = personRepository.findByUsername(username);
            return passwordEncoder.matches(password, person.getPassword());
        } else {
            return false; // User does not exist
        }
    }

    public Person findByUsername(String username) {
        // Find person by username, throwing an exception if not found
        return personRepository.findByUsername(username);
    }
}
