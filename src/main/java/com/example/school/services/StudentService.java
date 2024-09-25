package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.school.entities.WrapperTestWithStudent;
import com.example.school.dtos.WrapperTestWithStudentDTO;
import com.example.school.entities.Mark;
import com.example.school.entities.Person;
import com.example.school.entities.SchoolDebt;
import com.example.school.entities.SchoolTest;
import com.example.school.entities.Studente;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.repositories.SchoolTestRepository;
import com.example.school.repositories.StudentRepository;
import com.example.school.repositories.WrapperTestPersonRepository;

@Service
public class StudentService {
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SchoolTestRepository schoolTestRepository;
    @Autowired
    private SchoolDebtService schoolDebtService;
    @Autowired
    WrapperTestPersonRepository wrapperTestPersonRepository;
    @Autowired
    private MarkService markService;
    @Value("${min_pass_mark}")
    private short minPassMark;

    public boolean addWrapperTestWithStudent(WrapperTestWithStudentDTO wrapperTestWithStudentDTO) {
        Person person = personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername()).get();
        if (person == null) {
            return false;
        }
        SchoolTest schoolTest = schoolTestRepository.findByName(wrapperTestWithStudentDTO.getSchoolTestName());
        if (schoolTest == null) {
            return false;
        }
        Mark mark = markService.getMarkByName(wrapperTestWithStudentDTO.getMark());
        if (mark == null) {
            return false;
        }
        if (person instanceof Studente) {
            WrapperTestWithStudent wrapperTestWithStudent = new WrapperTestWithStudent();
            wrapperTestWithStudent.setMark(mark);
            wrapperTestWithStudent.setSchoolTest(schoolTest);
            Studente studente = (Studente) person;
            wrapperTestWithStudent.setStudente(studente);
            WrapperTestWithStudent wrapperTestWithStudentInContext = wrapperTestPersonRepository
                    .save(wrapperTestWithStudent);
            if (wrapperTestWithStudentDTO.getMark() < 6) {
                SchoolDebt schoolDebt = new SchoolDebt();
                schoolDebt.setStudente(studente);
                schoolDebt.setWrapperlTestWithStudent(wrapperTestWithStudentInContext);
                schoolDebtService.addSchoolDebt(schoolDebt);
            }
            return true;
        } else {
            return false;
        }
    }
}