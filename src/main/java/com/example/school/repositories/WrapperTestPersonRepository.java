package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.school.entities.WrapperTestWithStudent;

public interface WrapperTestPersonRepository extends JpaRepository<WrapperTestWithStudent,Long>{

}
