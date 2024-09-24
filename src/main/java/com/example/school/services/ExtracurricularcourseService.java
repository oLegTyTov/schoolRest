package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Extracurricularcourse;
import com.example.school.entities.Subject;
import com.example.school.repositories.ExtracurricularcourseRepository;

@Service
public class ExtracurricularcourseService {
@Autowired
    private ExtracurricularcourseRepository extracurricularcourseRepository;
        public boolean addExtracurricularcourse(Extracurricularcourse extracurricularcourse) {
        if (extracurricularcourseRepository.existsByName(extracurricularcourse.getName())) {
            return false;
        } else {
            extracurricularcourseRepository.save(extracurricularcourse);
            return true;
        }
    }
    public Extracurricularcourse findByName(String name)
    {
    return extracurricularcourseRepository.findByName(name);
    }
    public boolean existsByName(String name)
    {
    return extracurricularcourseRepository.existsByName(name);
    }
}
