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
import com.example.school.entities.Studente;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.utils.JwtUtils;

@Service
public class PersonService implements UserDetailsService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person person = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return User.builder()
                .username(person.getUsername())
                .password(person.getPassword())
                .authorities(person.getRole().getName())
                .build();
    }

    public boolean addPerson(Person person) {
        String roleName = person.getRole().getName();
        if (personRepository.existsByUsername(person.getUsername()) || !roleRepository.existsByName(roleName)) {
            return false;
        } else {
            person.setRole(roleRepository.findByName(roleName));
            // Encode the password
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            personRepository.save(person);
            return true;
        }
    }

    public MainTokens login(String username, String password) {
        MainTokens mainToken = null;
        if (checkPerson(username, password)) {
            mainToken = new MainTokens();
            mainToken.setAccessToken(jwtUtils.generateAccessToken(username));
            mainToken.setRefreshToken(jwtUtils.generateRefreshToken(username));
        }
        return mainToken;
    }

    public boolean checkPerson(String username, String password) {
        if (personRepository.existsByUsername(username)) {
            Person person = personRepository.findByUsername(username).get();
            return passwordEncoder.matches(password, person.getPassword());
        } else {
            return false;
        }
    }
    public Person findByUsername(String username)
    {
    return personRepository.findByUsername(username).get();
    }
}