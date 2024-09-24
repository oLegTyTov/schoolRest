package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.SchoolTest;
import com.example.school.entities.Subject;
import com.example.school.repositories.SchoolTestRepository;

@Service
public class SchoolTestService {
    @Autowired
    private SchoolTestRepository schoolTestRepository;
    @Autowired
    private SubjectService subjectService;

    public boolean addSchoolTest(SchoolTest schoolTest) {
        if (schoolTestRepository.existsByTextDocumentOfTest(schoolTest.getTextDocumentOfTest())) {
            return false;
        } else {
            Subject subject = subjectService.findByName(schoolTest.getSubject().getName());
            if (subject == null) {
                return false;
            }
            schoolTest.setSubject(subject);
            schoolTestRepository.save(schoolTest);
            return true;
        }

    }
}
