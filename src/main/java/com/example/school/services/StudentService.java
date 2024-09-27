package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.Person;
import com.example.school.entities.Studente;
import com.example.school.repositories.StudentRepository;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PersonService personService;

    public boolean deleteStudent(String username) {
        Person person= personService.findByUsername(username);
        if(person instanceof Studente && person!=null)
        {
            Studente studente=(Studente)person;
            if(!studente.getExtracurricularcourses().isEmpty())
            {
                studentRepository.deleteStudentCoursesByStudentId(studente.getId());
            }
                studentRepository.deleteById(studente.getId());
                return true;
        }
        return false;
    }
}
