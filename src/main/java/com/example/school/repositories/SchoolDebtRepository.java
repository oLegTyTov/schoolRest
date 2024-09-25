package com.example.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.school.entities.SchoolDebt;
import com.example.school.entities.WrapperTestWithStudent;


@Repository
public interface SchoolDebtRepository extends JpaRepository<SchoolDebt,Long>{
boolean existsByWrapperlTestWithStudent(WrapperTestWithStudent wrapperlTestWithStudent);
}
