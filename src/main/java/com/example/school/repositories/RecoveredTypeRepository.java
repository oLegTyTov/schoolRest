package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.RecoveredType;
import java.util.List;


@Repository
public interface RecoveredTypeRepository extends JpaRepository<RecoveredType,Long>{
RecoveredType findByName(String name);
boolean existsByName(String name);
}
