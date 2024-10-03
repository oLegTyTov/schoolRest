package com.example.school.services;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.school.entities.Extracurricularcourse;
import com.example.school.repositories.ExtracurricularcourseRepository;

@ExtendWith(MockitoExtension.class)
public class ExtracurricularcourseServiceTest {

    @Mock
    private ExtracurricularcourseRepository extracurricularcourseRepository;

    @InjectMocks
    private ExtracurricularcourseService extracurricularcourseService;

    private Extracurricularcourse extracurricularcourse;

    @BeforeEach
    public void setUp() {
        extracurricularcourse = new Extracurricularcourse();
        extracurricularcourse.setName("Art Club");
    }

    @Test
    public void testAddExtracurricularcourse_WhenCourseExists_ShouldReturnFalse() {
        // Arrange
        when(extracurricularcourseRepository.existsByName(extracurricularcourse.getName())).thenReturn(true);

        // Act
        boolean result = extracurricularcourseService.addExtracurricularcourse(extracurricularcourse);

        // Assert
        assertFalse(result);
        verify(extracurricularcourseRepository, times(1)).existsByName(extracurricularcourse.getName());
        verify(extracurricularcourseRepository, never()).save(any(Extracurricularcourse.class));
    }

    @Test
    public void testAddExtracurricularcourse_WhenCourseDoesNotExist_ShouldReturnTrue() {
        // Arrange
        when(extracurricularcourseRepository.existsByName(extracurricularcourse.getName())).thenReturn(false);

        // Act
        boolean result = extracurricularcourseService.addExtracurricularcourse(extracurricularcourse);

        // Assert
        assertTrue(result);
        verify(extracurricularcourseRepository, times(1)).existsByName(extracurricularcourse.getName());
        verify(extracurricularcourseRepository, times(1)).save(extracurricularcourse);
    }

    @Test
    public void testFindByName_ShouldReturnCourse() {
        // Arrange
        when(extracurricularcourseRepository.findByName(extracurricularcourse.getName())).thenReturn(extracurricularcourse);

        // Act
        Extracurricularcourse result = extracurricularcourseService.findByName(extracurricularcourse.getName());

        // Assert
        assertNotNull(result);
        assertEquals(extracurricularcourse.getName(), result.getName());
    }

    @Test
    public void testExistsByName_ShouldReturnTrue() {
        // Arrange
        when(extracurricularcourseRepository.existsByName(extracurricularcourse.getName())).thenReturn(true);

        // Act
        boolean result = extracurricularcourseService.existsByName(extracurricularcourse.getName());

        // Assert
        assertTrue(result);
        verify(extracurricularcourseRepository, times(1)).existsByName(extracurricularcourse.getName());
    }

    @Test
    public void testExistsByName_ShouldReturnFalse() {
        // Arrange
        when(extracurricularcourseRepository.existsByName(extracurricularcourse.getName())).thenReturn(false);

        // Act
        boolean result = extracurricularcourseService.existsByName(extracurricularcourse.getName());

        // Assert
        assertFalse(result);
        verify(extracurricularcourseRepository, times(1)).existsByName(extracurricularcourse.getName());
    }
}
