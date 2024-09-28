package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.SchoolClass;
import com.example.school.entities.Teacher;
import java.util.Set;


@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Long>{
boolean existsByName(String name);
Set<SchoolClass> findByTeachersContaining(Teacher teacher);
}
