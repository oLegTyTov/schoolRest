package com.example.school.services;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.school.entities.MainTokens;
import com.example.school.entities.Person;
import com.example.school.entities.Role;
import com.example.school.entities.Teacher;  // Додано для конкретного тестування
import com.example.school.repositories.PersonRepository;
import com.example.school.repositories.RoleRepository;
import com.example.school.services.PersonService;
import com.example.school.utils.JwtUtils;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    private Teacher teacher; // Змінено на Teacher для тестів

    @BeforeEach
    public void setUp() {
        teacher = new Teacher(); // Використання конкретного класу
        teacher.setUsername("john_doe");
        teacher.setPassword("password123");
        Role role = new Role();
        role.setName("ROLE_TEACHER");
        teacher.setRole(role);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        // Arrange
        when(personRepository.findByUsername(teacher.getUsername())).thenReturn(Optional.of(teacher));

        // Act
        UserDetails result = personService.loadUserByUsername(teacher.getUsername());

        // Assert
        assertNotNull(result);
        assertEquals(teacher.getUsername(), result.getUsername());
        assertEquals(teacher.getPassword(), result.getPassword());
        assertEquals(teacher.getRole().getName(), result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Arrange
        when(personRepository.findByUsername(teacher.getUsername())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            personService.loadUserByUsername(teacher.getUsername());
        });
    }

    @Test
    public void testAddPerson_UsernameExists() {
        // Arrange
        when(personRepository.existsByUsername(teacher.getUsername())).thenReturn(true);

        // Act
        boolean result = personService.addPerson(teacher);

        // Assert
        assertFalse(result);
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void testAddPerson_RoleNotExists() {
        // Arrange
        when(personRepository.existsByUsername(teacher.getUsername())).thenReturn(false);
        when(roleRepository.existsByName(teacher.getRole().getName())).thenReturn(false);

        // Act
        boolean result = personService.addPerson(teacher);

        // Assert
        assertFalse(result);
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    public void testAddPerson_Success() {
        // Arrange
        when(personRepository.existsByUsername(teacher.getUsername())).thenReturn(false);
        when(roleRepository.existsByName(teacher.getRole().getName())).thenReturn(true);
        when(roleRepository.findByName(teacher.getRole().getName())).thenReturn(teacher.getRole());
        when(passwordEncoder.encode(teacher.getPassword())).thenReturn("encodedPassword");

        // Act
        boolean result = personService.addPerson(teacher);

        // Assert
        assertTrue(result);
        verify(personRepository, times(1)).save(teacher);
        assertEquals("encodedPassword", teacher.getPassword());
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        String username = teacher.getUsername();
        String password = teacher.getPassword();
        when(personRepository.existsByUsername(username)).thenReturn(true);
        when(personRepository.findByUsername(username)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(password, teacher.getPassword())).thenReturn(true);
        when(jwtUtils.generateAccessToken(username)).thenReturn("accessToken");
        when(jwtUtils.generateRefreshToken(username)).thenReturn("refreshToken");

        // Act
        MainTokens result = personService.login(username, password);

        // Assert
        assertNotNull(result);
        assertEquals("accessToken", result.getAccessToken());
        assertEquals("refreshToken", result.getRefreshToken());
    }

    @Test
    public void testLogin_Failure() {
        // Arrange
        String username = teacher.getUsername();
        String password = teacher.getPassword();
        when(personRepository.existsByUsername(username)).thenReturn(true);
        when(personRepository.findByUsername(username)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(password, teacher.getPassword())).thenReturn(false);

        // Act
        MainTokens result = personService.login(username, password);

        // Assert
        assertNull(result); // Login should fail and return null
    }

    @Test
    public void testCheckPerson_UserExistsAndValidPassword() {
        // Arrange
        String username = teacher.getUsername();
        String password = teacher.getPassword();
        when(personRepository.existsByUsername(username)).thenReturn(true);
        when(personRepository.findByUsername(username)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(password, teacher.getPassword())).thenReturn(true);

        // Act
        boolean result = personService.checkPerson(username, password);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testCheckPerson_UserExistsButInvalidPassword() {
        // Arrange
        String username = teacher.getUsername();
        String password = "wrongPassword"; // Invalid password
        when(personRepository.existsByUsername(username)).thenReturn(true);
        when(personRepository.findByUsername(username)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches(password, teacher.getPassword())).thenReturn(false);

        // Act
        boolean result = personService.checkPerson(username, password);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCheckPerson_UserDoesNotExist() {
        // Arrange
        String username = teacher.getUsername();
        String password = teacher.getPassword();
        when(personRepository.existsByUsername(username)).thenReturn(false);

        // Act
        boolean result = personService.checkPerson(username, password);

        // Assert
        assertFalse(result);
    }
}
