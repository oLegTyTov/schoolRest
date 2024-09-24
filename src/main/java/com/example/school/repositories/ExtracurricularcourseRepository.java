package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.school.entities.Extracurricularcourse;
import java.util.List;


public interface ExtracurricularcourseRepository extends JpaRepository<Extracurricularcourse,Long>{
boolean existsByName(String name);
Extracurricularcourse findByName(String name);
}
