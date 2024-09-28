package com.example.school.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;
import com.example.school.dtos.WrapperTestStudentWithRecoveredTypeDTO;
import com.example.school.dtos.WrapperTestWithStudentDTO;
import com.example.school.entities.Extracurricularcourse;
import com.example.school.entities.Person;
import com.example.school.entities.SchoolClass;
import com.example.school.entities.SchoolTest;
import com.example.school.entities.Subject;
import com.example.school.entities.Teacher;
import com.example.school.services.PersonService;
import com.example.school.services.SchoolClassService;
import com.example.school.services.SchoolTestService;
import com.example.school.services.TeacherService;
import com.example.school.services.WrapperTestPersonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/teacher")
@Secured("TEACHER")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private WrapperTestPersonService wrapperTestPersonService;
    @Autowired
    private PersonService personService;
    @Autowired
    private SchoolTestService schoolTestService;
    @Autowired
    private SchoolClassService schoolClassService;
    @PostMapping("/associateSubjectToTeacher")
    @Transactional // без цьої анотації метод не буде працювати якщо в сервісі йде збереження через
                   // setter а не через метод save
    public ResponseEntity<String> associateSubjectToTeacher(@RequestBody Subject subjects[],
            @AuthenticationPrincipal UserDetails userDetails) {
        Person person = personService.findByUsername(userDetails.getUsername());
        Teacher teacher = (Teacher) person;
        if (teacherService.associateSubjectToTeacher(subjects, teacher)) {
            return ResponseEntity.status(HttpStatus.OK).body("good associateSubjectToTeacher");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error associateSubjectToTeacher");
        }
    }

    @PostMapping("/addSchoolTest")
    public ResponseEntity<String> addSchoolTest(@RequestBody SchoolTest schoolTest) {
        if (schoolTestService.addSchoolTest(schoolTest)) {
            return ResponseEntity.status(HttpStatus.OK).body("SchoolTest registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SchoolTest wasn't registered");
        }
    }

    @PostMapping("/associateTeacherToExtraCurricularCourses")
    public ResponseEntity<String> associateTeacherToExtraCurricularCourses(
            @RequestBody Extracurricularcourse extracurricularcourses[],
            @AuthenticationPrincipal UserDetails userDetails) {
        Person person = personService.findByUsername(userDetails.getUsername());
        Teacher teacher = (Teacher) person;
        if (teacherService.associateTeacherToExtraCurricularCourses(extracurricularcourses, teacher)) {
            return ResponseEntity.status(HttpStatus.OK).body("good associateTeacherToExtraCurricularCourses");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error associateTeacherToExtraCurricularCourses");
        }
    }
@PostMapping("/addWrapperTestWithStudent")
    public ResponseEntity<String> addWrapperTestWithStudent(
            @RequestBody WrapperTestWithStudentDTO wrapperTestWithStudentDTO)// teacher addes test with mark to student
    {
        if (wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("good addWrapperTestWithStudent");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error addWrapperTestWithStudent");
        }
    }
    @PostMapping("/updateStudentDebit")//Student could repass exam
    public ResponseEntity<String> updateStudentDebit(@RequestBody WrapperTestStudentWithRecoveredTypeDTO wrapperTestStudentWithRecoveredTypeDTO) {
        if (wrapperTestPersonService.removeStudentDebit(wrapperTestStudentWithRecoveredTypeDTO)) {
            return ResponseEntity.status(HttpStatus.OK).body("good updateStudentDebit");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error updateStudentDebit");
        }
    }
    @GetMapping("/getClassesTeacher")
    public ResponseEntity<String> getClassesTeacher(@AuthenticationPrincipal UserDetails userDetails)
    {
       List<String>schoolClassesName=schoolClassService.findNamesOfClassByTeacherName((Teacher)personService.findByUsername(userDetails.getUsername()));
       return ResponseEntity.status(HttpStatus.OK).body("Teacher "+userDetails.getUsername()+" teaches in these classes:"+schoolClassesName.stream().map(e->e+" ").toList());
    }
        
}
