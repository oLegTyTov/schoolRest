package com.example.school.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.school.entities.RecoveredType;
import com.example.school.repositories.RecoveredTypeRepository;

@Service
public class RecoveredTypeService {
    @Autowired
    private RecoveredTypeRepository recoveredTypeRepository;

    public RecoveredType findByName(String type) {
        if (recoveredTypeRepository.existsByName(type)) {
            return recoveredTypeRepository.findByName(type);
        } else {
            return null;
        }

    }
}
