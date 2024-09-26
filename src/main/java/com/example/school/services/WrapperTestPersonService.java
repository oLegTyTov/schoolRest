package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.school.dtos.WrapperTestStudentWithRecoveredTypeDTO;
import com.example.school.dtos.WrapperTestWithStudentDTO;
import com.example.school.entities.Mark;
import com.example.school.entities.Person;
import com.example.school.entities.RecoveredType;
import com.example.school.entities.SchoolDebt;
import com.example.school.entities.SchoolTest;
import com.example.school.entities.Studente;
import com.example.school.entities.WrapperTestWithStudent;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RecoveredTypeRepository;
import com.example.school.repositories.SchoolTestRepository;
import com.example.school.repositories.WrapperTestPersonRepository;

@Service
public class WrapperTestPersonService {
    @Autowired
    private RecoveredTypeRepository recoveredTypeRepository;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SchoolTestRepository schoolTestRepository;
    @Autowired
    private SchoolDebtService schoolDebtService;
    @Autowired
    WrapperTestPersonRepository wrapperTestPersonRepository;
    @Autowired
    private RecoveredTypeService recoveredTypeService;
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
            if (wrapperTestWithStudentDTO.getMark() < minPassMark) {
                SchoolDebt schoolDebt = new SchoolDebt();
                schoolDebt.setStudente(studente);
                schoolDebt.setWrapperlTestWithStudent(wrapperTestWithStudentInContext);
                schoolDebt.setRecoveredType(recoveredTypeRepository.findByName("notdoneyet"));
                schoolDebtService.addSchoolDebt(schoolDebt);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean removeStudentDebit(WrapperTestStudentWithRecoveredTypeDTO wrapperTestStudentWithRecoveredTypeDTO) {
        Person person = personRepository.findByUsername(wrapperTestStudentWithRecoveredTypeDTO.getStudentUsername())
                .get();
        if (person instanceof Studente) {
            WrapperTestWithStudent wrapperTestWithStudent = wrapperTestPersonRepository.findByStudenteAndSchoolTest(
                    (Studente) personRepository
                            .findByUsername(wrapperTestStudentWithRecoveredTypeDTO.getStudentUsername()).get(),
                    schoolTestRepository.findByName(wrapperTestStudentWithRecoveredTypeDTO.getSchoolTestName()));
            if (wrapperTestWithStudent == null) {
                return false;
            }
            SchoolDebt schoolDebt = wrapperTestWithStudent.getSchoolDebt();
            RecoveredType recoveredType = recoveredTypeService
                    .findByName(wrapperTestStudentWithRecoveredTypeDTO.getRecoveredType());
            if (recoveredType == null) {
                return false;
            }
            schoolDebt.setRecoveredType(recoveredType);
            schoolDebtService.updateDebt(schoolDebt);
            return true;
        } else {
            return false;
        }
    }
}
