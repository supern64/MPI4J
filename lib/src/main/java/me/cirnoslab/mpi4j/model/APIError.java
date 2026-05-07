package me.cirnoslab.mpi4j.model;

public enum APIError {
    NOT_AUTHENTICATED(1001, "Not authenticated"),
    TOTP_NOT_PROVIDED(1002, "2FA code required but not provided"),
    ACCOUNT_SUSPENDED(1003, "Account is suspended"),
    ACCOUNT_FROZEN(1004, "Relevant account is frozen"),
    INVALID_CREDENTIALS(1005, "Invalid username/email or password"),

    INVALID_2FA_CODE(1010, "Invalid 2FA code"),
    EMAIL_ALREADY_VERIFIED(1013, "Email already verified"),
    NOT_PART_OF_TRANSACTION(1014, "Not a party to this transaction"),
    OPERATION_EXCEEDS_FUND_LIMITS(1015, "Operation would exceed account fund limits"),

    INSUFFICIENT_BALANCE(2001, "Insufficient balance"),
    RECIPIENT_NOT_FOUND(2002, "Recipient not found"),
    SELF_TRANSFER(2003, "Cannot transfer to yourself"),
    ACCOUNT_RESTRICTED(2005, "Sender or recipient is restricted from receiving or making transactions"),

    PAYMENT_LINK_NOT_FOUND(3001, "Payment link not found"),
    PAYMENT_LINK_ALREADY_CLAIMED(3002, "Payment link already claimed"),
    PAYMENT_LINK_CANCELLED(3003, "Payment link cancelled"),

    INVALID_RETURN_URL(4001, "return_url domain not in allowed domains"),
    KEY_LIMIT_EXCEEDED(4002, "\tAmount exceeds key's limit (unreviewed/unverified)"),

    UNDER_MAINTENANCE(9003, "Server is under maintenance"),
    INVALID_INPUT(9004, "Invalid input"),
    INVALID_SESSION(9005, "Session not found or already invalidated"),
    RATE_LIMITED(9006, "Rate limited (60 second cooldown)");

    private final int code;
    private final String description;

    APIError(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return code;
    }

    public String description() {
        return description;
    }
}
