package com.solver.solver_be.domain.user.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Getter
@Service
@RequiredArgsConstructor
public class InfoProvider {

    private final int LEFT_LIMIT = 48;
    private final int RIGHT_LIMIT = 122;


    // 1. Generating a temporary password
    public String generateRandomPassword() {
        int targetStringLength = 10;
        return getRandomTarget(targetStringLength, LEFT_LIMIT, RIGHT_LIMIT);
    }

    // 2. Generating a temporary id
    public String generateRandomId() {
        int targetStringLength = 6;
        return getRandomTarget(targetStringLength, LEFT_LIMIT, RIGHT_LIMIT);
    }

    // 3. Get RandomTarget
    public static String getRandomTarget(int targetStringLength, int leftLimit, int rightLimit) {
        Random random = new Random();
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }



}
