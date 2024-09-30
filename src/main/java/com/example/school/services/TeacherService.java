package com.example.school.services;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Arrays; // For working with arrays

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Extracurricularcourse;
import com.example.school.entities.Subject;
import com.example.school.entities.Teacher;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.repositories.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;  
    private PersonRepository personRepository;    
    @Autowired
    private RoleRepository roleRepository;       
    @Autowired
    private SubjectService subjectService;       
    @Autowired
    private ExtracurricularcourseService extracurricularcourseService; 

    public boolean associateSubjectToTeacher(Subject[] subjects, Teacher teacher) {
        // Check for duplicates and existing subjects for the teacher
        long distinctSubjectsCount = Stream.of(subjects).distinct().count();
        if (distinctSubjectsCount != subjects.length || Stream.of(subjects).anyMatch(teacher.getSubjects()::contains)) {
            return false; // Return false if there are duplicates or already assigned subjects
        }

        // Validate subjects and collect valid ones
        Set<Subject> validSubjects = Stream.of(subjects)
                .map(Subject::getName)
                .map(name -> subjectService.existsByName(name) ? subjectService.findByName(name) : null)
                .filter(Objects::nonNull) // Filter out nulls for non-existent subjects
                .collect(Collectors.toSet());

        // If valid subjects count does not match input, return false
        if (validSubjects.size() != subjects.length) {
            return false;
        }

        teacher.setSubjects(validSubjects); // Set valid subjects to teacher
        return true;
    }

    public boolean associateTeacherToExtraCurricularCourses(Extracurricularcourse[] extracurricularCourses, Teacher teacher) {
        // Check for duplicates
        long distinctCoursesCount = Stream.of(extracurricularCourses).distinct().count();
        if (distinctCoursesCount != extracurricularCourses.length) {
            return false; // Return false if duplicates exist
        }

        Set<Extracurricularcourse> teacherCourses = teacher.getExtracurricularcourses();
        if (teacherCourses != null && Stream.of(extracurricularCourses).anyMatch(teacherCourses::contains)) {
            return false; // Return false if any course is already assigned
        }

        // Validate courses and collect valid ones
        Set<Extracurricularcourse> validCourses = Arrays.stream(extracurricularCourses)
                .map(e -> extracurricularcourseService.existsByName(e.getName()) 
                        ? extracurricularcourseService.findByName(e.getName()) 
                        : null)
                .filter(Objects::nonNull) // Filter out nulls for invalid courses
                .collect(Collectors.toSet());

        // Return false if not all courses are valid
        if (validCourses.size() != extracurricularCourses.length) {
            return false;
        }

        teacher.setExtracurricularcourses(validCourses); // Set valid courses to teacher
        teacherRepository.save(teacher); // Persist changes to the database
        return true;
    }
}
