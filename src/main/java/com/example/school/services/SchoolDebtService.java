package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.SchoolDebt;
import com.example.school.repositories.SchoolDebtRepository;

@Service
public class SchoolDebtService {
    @Autowired
    private SchoolDebtRepository schoolDebtRepository;

    public boolean addSchoolDebt(SchoolDebt schoolDebt) {
        if (schoolDebtRepository.existsByWrapperlTestWithStudent(schoolDebt.getWrapperlTestWithStudent())) {
            return false;
        } else {
            schoolDebtRepository.save(schoolDebt);
            return true;
        }
    }
    public void updateDebt(SchoolDebt schoolDebt)
    {
    schoolDebtRepository.save(schoolDebt);
    }
}
