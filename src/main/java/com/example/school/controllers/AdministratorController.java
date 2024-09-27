package com.example.school.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.school.entities.Extracurricularcourse;
import com.example.school.entities.Mark;
import com.example.school.entities.SchoolClass;
import com.example.school.entities.Subject;
import com.example.school.services.ExtracurricularcourseService;
import com.example.school.services.MarkService;
import com.example.school.services.SchoolClassService;
import com.example.school.services.StudentService;
import com.example.school.services.SubjectService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/admin")
@Secured("ADMIN")
public class AdministratorController {
    @Autowired
    private SubjectService subjectService;
    @Autowired
    private SchoolClassService schoolClassService;
    @Autowired
    private ExtracurricularcourseService extracurricularcourseService;
    @Autowired 
    private MarkService markService;
    @Autowired
    private StudentService studentService;
    @PostMapping("/addSubject")
    public ResponseEntity<String> addSubject(@RequestBody Subject subject) {
        if (subjectService.addSubject(subject)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Subject registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Subject wasn't registered ");
        }
    }

    @PostMapping("/addSchoolClass")
    public ResponseEntity<String> addSchoolClass(@RequestBody SchoolClass schoolClass) {
        if (schoolClassService.addSchoolClass(schoolClass)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("SchoolClass registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("SchoolClass wasn't registered ");
        }
    }

    @PostMapping("/addExtraCurricularCourse")
    public ResponseEntity<String> addExtraCurricularCourse(@RequestBody Extracurricularcourse extracurricularcourse) {
        if (extracurricularcourseService.addExtracurricularcourse(extracurricularcourse)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("ExtraCurricularCourse registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ExtraCurricularCourse wasn't registered ");
        }
    }
    @PostMapping("/addMark")
    public ResponseEntity<String> addMark(@RequestBody Mark mark) {
        if (markService.addMark(mark)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Mark registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mark wasn't registered ");
        }
    }
    @PostMapping("/deleteStudent")
    public ResponseEntity<String> deleteStudent(@RequestParam String username) {
        if (studentService.deleteStudent(username)) {
            return ResponseEntity.status(HttpStatus.CREATED).body("good deleteStudent");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" error deleteStudent");
        }
    }
    //deleteTeacher TODO
}
