package com.example.school.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MainTokens {
private String refreshToken;
private String accessToken;
}
