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
        StringBuilder sb = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomInt = random.nextInt(rightLimit - leftLimit + 1) + leftLimit;
            if ((randomInt <= 57 || randomInt >= 65) && (randomInt <= 90 || randomInt >= 97)) {
                sb.appendCodePoint(randomInt);
            } else {
                i--;
            }
        }

        return sb.toString();
    }

}
