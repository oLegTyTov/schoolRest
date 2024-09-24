package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.SchoolClass;

@Repository
public interface SchoolClassRepository extends JpaRepository<SchoolClass,Long>{
boolean existsByName(String name);
}
