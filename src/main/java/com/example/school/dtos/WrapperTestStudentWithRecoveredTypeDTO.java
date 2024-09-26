package com.example.school.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WrapperTestStudentWithRecoveredTypeDTO {
private String studentUsername;
private String schoolTestName;
private short mark;
private String recoveredType;
}