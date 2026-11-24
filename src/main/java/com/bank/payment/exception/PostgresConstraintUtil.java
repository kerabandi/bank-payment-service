package com.bank.payment.exception;

import org.postgresql.util.PSQLException;
import org.postgresql.util.ServerErrorMessage;

public final class PostgresConstraintUtil {

    private static final String UNIQUE_VIOLATION = "23505";
    private static final String IDEMPOTENCY_CONSTRAINT = "uq_payments_idempotency_key";

    private PostgresConstraintUtil() {
    }

    public static boolean isIdempotencyConflict(Throwable exception) {

        Throwable current = exception;

        while (current != null) {
            if (current instanceof PSQLException pgException) {
                ServerErrorMessage error = pgException.getServerErrorMessage();

                if (UNIQUE_VIOLATION.equals(pgException.getSQLState())
                    && error != null
                    && IDEMPOTENCY_CONSTRAINT.equals(error.getConstraint())) {

                    return true;
                }
            }
            current = current.getCause();
        }
        return false;
    }
}