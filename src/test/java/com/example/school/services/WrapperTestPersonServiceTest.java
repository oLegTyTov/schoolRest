package com.example.school.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.school.dtos.WrapperTestWithStudentDTO;
import com.example.school.entities.*;
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.SchoolTestRepository;
import com.example.school.repositories.WrapperTestPersonRepository;
import com.example.school.services.MarkService;
import com.example.school.services.SchoolDebtService;
import com.example.school.repositories.RecoveredTypeRepository;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WrapperTestPersonServiceTest {
    @InjectMocks
    private WrapperTestPersonService wrapperTestPersonService;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private SchoolTestRepository schoolTestRepository;

    @Mock
    private MarkService markService;

    @Mock
    private WrapperTestPersonRepository wrapperTestPersonRepository;

    @Mock
    private SchoolDebtService schoolDebtService;

    @Mock
    private RecoveredTypeRepository recoveredTypeRepository;

    private WrapperTestWithStudentDTO wrapperTestWithStudentDTO;
    private Studente student;
    private SchoolTest schoolTest;
    private Mark mark;

    @BeforeEach
    public void setUp() {
        wrapperTestWithStudentDTO = new WrapperTestWithStudentDTO();
        wrapperTestWithStudentDTO.setMark((short) 5); // Оцінка за замовчуванням нижче мінімального
        wrapperTestWithStudentDTO.setSchoolTestName("schoolname");
        wrapperTestWithStudentDTO.setStudentUsername("bob");

        student = new Studente();
        student.setUsername("bob");

        schoolTest = new SchoolTest();
        schoolTest.setName("schoolname");

        mark = new Mark();
        mark.setDescription("A");
    }

    // 1. Персона не існує
    @Test
    public void testAddWrapperTestWithStudent_PersonNonExist_ShouldReturnFalse() {
        when(personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername())).thenReturn(null);
        boolean result = wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO);
        assertFalse(result);
    }

    // 2. Тест школи не існує
    @Test
    public void testAddWrapperTestWithStudent_SchoolTestNonExist_ShouldReturnFalse() {
        when(personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername())).thenReturn(student);
        when(schoolTestRepository.findByName(wrapperTestWithStudentDTO.getSchoolTestName())).thenReturn(null);
        boolean result = wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO);
        assertFalse(result);
    }

    // 3. Оцінка не існує
    @Test
    public void testAddWrapperTestWithStudent_MarkNonExist_ShouldReturnFalse() {
        when(personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername())).thenReturn(student);
        when(schoolTestRepository.findByName(wrapperTestWithStudentDTO.getSchoolTestName())).thenReturn(schoolTest);
        when(markService.getMarkByName(wrapperTestWithStudentDTO.getMark())).thenReturn(null);
        boolean result = wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO);
        assertFalse(result);
    }

    // 4. Особа не є студентом (Person не інстанс Studente)
    @Test
    public void testAddWrapperTestWithStudent_PersonIsNotStudent_ShouldReturnFalse() {
        Teacher teacher = new Teacher();
        teacher.setUsername("bob");
        when(personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername())).thenReturn(teacher);
        when(schoolTestRepository.findByName(wrapperTestWithStudentDTO.getSchoolTestName())).thenReturn(schoolTest);
        when(markService.getMarkByName(wrapperTestWithStudentDTO.getMark())).thenReturn(mark);
        boolean result = wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO);
        assertFalse(result);
    }

    // 5. Студент існує, тест школи існує, оцінка існує, але оцінка менша за мінімальний бал
    @Test
    public void testAddWrapperTestWithStudent_MarkBelowMinPass_ShouldAddSchoolDebt() {
        // Arrange
        wrapperTestWithStudentDTO.setMark((short) 5); // Оцінка нижче мінімального балу
        when(personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername())).thenReturn(student);
        when(schoolTestRepository.findByName(wrapperTestWithStudentDTO.getSchoolTestName())).thenReturn(schoolTest);
        when(markService.getMarkByName(wrapperTestWithStudentDTO.getMark())).thenReturn(mark);
        when(wrapperTestPersonRepository.save(any(WrapperTestWithStudent.class))).thenReturn(new WrapperTestWithStudent());
        when(recoveredTypeRepository.findByName("notdoneyet")).thenReturn(new RecoveredType());

        // Act
        boolean result = wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO);

        // Assert
        assertTrue(result);
        verify(schoolDebtService, times(1)).addSchoolDebt(any(SchoolDebt.class)); // Переконайтеся, що метод викликаний
    }

    // 6. Студент існує, тест школи існує, оцінка існує, оцінка вище мінімального балу
    @Test
    public void testAddWrapperTestWithStudent_MarkAboveMinPass_ShouldReturnTrue() {
        // Arrange
        wrapperTestWithStudentDTO.setMark((short) 8); // Оцінка вище мінімального балу
        when(personRepository.findByUsername(wrapperTestWithStudentDTO.getStudentUsername())).thenReturn(student);
        when(schoolTestRepository.findByName(wrapperTestWithStudentDTO.getSchoolTestName())).thenReturn(schoolTest);
        when(markService.getMarkByName(wrapperTestWithStudentDTO.getMark())).thenReturn(mark);
        when(wrapperTestPersonRepository.save(any(WrapperTestWithStudent.class))).thenReturn(new WrapperTestWithStudent());

        // Act
        boolean result = wrapperTestPersonService.addWrapperTestWithStudent(wrapperTestWithStudentDTO);

        // Assert
        assertTrue(result);
        verify(schoolDebtService, never()).addSchoolDebt(any(SchoolDebt.class)); // Не повинна додавати борг
    }
}
