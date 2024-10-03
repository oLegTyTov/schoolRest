package com.example.school.services;

import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream; // For working with arrays

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Extracurricularcourse;
import com.example.school.entities.SchoolClass;
import com.example.school.entities.Subject;
import com.example.school.entities.Teacher;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.repositories.TeacherRepository;

@Service
public class TeacherService {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
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

    public boolean associateTeacherToExtraCurricularCourses(Extracurricularcourse[] extracurricularCourses,
            Teacher teacher) {
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

    public boolean deleteTeacher(String teacherUsername) {
        if (personRepository.existsByUsername(teacherUsername)) {
            // before remove teacher we must remove all manytomany entities asscocciated to
            // this teacher
            if(!personRepository.existsByUsername(teacherUsername))
            {
            return false;
            }
            Teacher teacher = (Teacher) personRepository.findByUsername(teacherUsername);
            Set<Extracurricularcourse> extracurricularcourses = teacher.getExtracurricularcourses();
            if (extracurricularcourses != null && !extracurricularcourses.isEmpty()) {
                teacherRepository.deleteExtracurricularcourseByTeacherId(teacher.getId());
            }
            Set<Subject> subjects = teacher.getSubjects();
            if (subjects != null && !subjects.isEmpty()) {
                teacherRepository.deleteSubjectsByTeacherId(teacher.getId());
            }
            Set<SchoolClass> schoolClasses = teacher.getSchoolClasses();
            if (schoolClasses != null && !schoolClasses.isEmpty()) {
                teacherRepository.deleteSchoolClassesByTeacherId(teacher.getId());
            }
            teacherRepository.deleteByUsername(teacherUsername);
            return true;
        } else {
            return false;
        }
    }
}
