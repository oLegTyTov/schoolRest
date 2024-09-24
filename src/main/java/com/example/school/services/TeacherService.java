package com.example.school.services;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import java.util.HashSet;

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
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ExtracurricularcourseService extracurricularcourseService;

    public boolean associateSubjectToTeacher(Subject[] subjects, Teacher teacher) {
        long distinctSubjectsCount = Stream.of(subjects).distinct().count();
        if (distinctSubjectsCount != subjects.length) {// якщо ми маємо повторення в масиві
            return false;
        }
        Set<Subject> teacherSubjects = teacher.getSubjects();
        if (teacherSubjects != null) {
            for (Subject subject : subjects) {
                if (teacherSubjects.contains(subject)) {// якщо вчитель вже має цей предмет
                    return false;
                }
            }
        }

        Set<Subject> validSubjects = new HashSet<>();
        for (Subject subject : subjects) {
            if (subjectService.existsByName(subject.getName())) {
                validSubjects.add(subjectService.findByName(subject.getName()));
            } else {
                return false;// якщо не існує такого предмета
            }
        }
        teacher.setSubjects(validSubjects);// хз чи буде працювати без прямого save
        return true;
    }

    public boolean associateTeacherToExtraCurricularCourses(Extracurricularcourse extracurricularCourses[],
            Teacher teacher) {

        // Перевірка на унікальність курсів у масиві
        long distinctCoursesCount = Stream.of(extracurricularCourses).distinct().count();
        if (distinctCoursesCount != extracurricularCourses.length) {
            // Якщо є дублікати курсів у масиві, повертаємо false
            return false;
        }

        // Отримання курсів, до яких вже приписаний вчитель
        Set<Extracurricularcourse> teacherCourses = teacher.getExtracurricularcourses();
        if (teacherCourses != null) {
            // Перевіряємо, чи вчитель вже має якісь з цих курсів
            for (Extracurricularcourse course : extracurricularCourses) {
                if (teacherCourses.contains(course)) {
                    return false; // Якщо курс вже закріплений за вчителем, повертаємо false
                }
            }
        }

        // Створюємо множину для збереження валідних курсів
        Set<Extracurricularcourse> validCourses = new HashSet<>();
        for (Extracurricularcourse course : extracurricularCourses) {
            // Перевіряємо, чи існує курс у базі даних
            if (extracurricularcourseService.existsByName(course.getName())) {
                // Додаємо курс, який знайдено у базі даних
                validCourses.add(extracurricularcourseService.findByName(course.getName()));
            } else {
                return false; // Якщо курс не знайдено, повертаємо false
            }
        }

        // Оновлюємо курси вчителя
        teacher.setExtracurricularcourses(validCourses);
        teacherRepository.save(teacher);
        // Можна додати явне збереження вчителя, якщо це необхідно
        // teacherRepository.save(teacher);
        
        return true;
    }

}
