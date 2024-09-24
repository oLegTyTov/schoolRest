package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Studente;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.repositories.StudentRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired private RoleRepository roleRepository;

}
