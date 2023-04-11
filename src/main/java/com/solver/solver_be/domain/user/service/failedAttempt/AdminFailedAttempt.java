package com.solver.solver_be.domain.user.service.failedAttempt;

import com.solver.solver_be.global.exception.exceptionType.UserException;
import com.solver.solver_be.global.type.ErrorType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Service
@RequiredArgsConstructor
public class AdminFailedAttempt {

    private int failedAttemptCount = 0;
    private LocalDateTime lastFailedAttemptTime = LocalDateTime.MIN;

    public void adminFailedAttempt() {
        // Failed Count Check
        if (failedAttemptCount >= 3) {
            // Last Failed Attempt Time Compare Now Time
            if (lastFailedAttemptTime.plusHours(2).isBefore(LocalDateTime.now())) {
                adminResetFailedAttempt();
            } else {
                throw new UserException(ErrorType.NOT_IMPOSSIBLE_TRY);
            }
        }
    }

    public void adminFailedCountUp() {
        // Failed Count Up
        failedAttemptCount++;
        lastFailedAttemptTime = LocalDateTime.now();
    }

    public void adminResetFailedAttempt() {
        // Reset Failed Attempt
        failedAttemptCount = 0;
        lastFailedAttemptTime = LocalDateTime.MIN;
    }
}
