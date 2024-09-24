package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Subject;
import com.example.school.repositories.SubjectRepository;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    public boolean addSubject(Subject subject) {
        if (subjectRepository.existsByName(subject.getName())) {
            return false;
        } else {
            subjectRepository.save(subject);
            return true;
        }
    }
    public boolean existsByName(String name)
    {
    return subjectRepository.existsByName(name);
    }
    public Subject findByName(String name)
    {
    return subjectRepository.findByName(name);
    }
}
