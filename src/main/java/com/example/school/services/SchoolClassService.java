package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.SchoolClass;
import com.example.school.entities.Subject;
import com.example.school.repositories.SchoolClassRepository;

@Service
public class SchoolClassService {
    @Autowired
private SchoolClassRepository schoolClassRepository;
    public boolean addSchoolClass(SchoolClass schoolClass) {
        if (schoolClassRepository.existsByName(schoolClass.getName())) {
            return false;
        } else {
            schoolClassRepository.save(schoolClass);
            return true;
        }
    }
}
