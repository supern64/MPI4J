package me.cirnoslab.mpiclient;

import me.cirnoslab.mpi4j.client.user.MPIUserClient;
import me.cirnoslab.mpi4j.model.paymentlink.PaymentLink;
import me.cirnoslab.mpi4j.model.response.*;
import me.cirnoslab.mpi4j.model.restriction.Restriction;
import me.cirnoslab.mpi4j.model.session.UserSession;
import me.cirnoslab.mpi4j.model.transaction.Transaction;
import me.cirnoslab.mpi4j.model.user.LeaderboardUser;
import me.cirnoslab.mpi4j.model.misc.TeamMember;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static MPIUserClient client;

    public static void main(String[] args) {
        System.out.println("=== MPI4J CLI ===");
        System.out.print("Use staging? (y/N): ");
        boolean staging = sc.nextLine().trim().equalsIgnoreCase("y");

        System.out.print("Account uses 2FA? (y/N): ");
        boolean otp = sc.nextLine().trim().equalsIgnoreCase("y");

        client = new MPIUserClient(otp, staging);

        System.out.println(staging ? "[Using staging environment]" : "[Using production environment]");

        while (true) {
            printMainMenu();
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> authMenu();
                case "2" -> userMenu();
                case "3" -> transactionMenu();
                case "4" -> paymentLinkMenu();
                case "5" -> infoMenu();
                case "0" -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Menus
    // -------------------------------------------------------------------------

    private static void printMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Auth");
        System.out.println("2. User");
        System.out.println("3. Transactions");
        System.out.println("4. Payment Links");
        System.out.println("5. Info (Public)");
        System.out.println("0. Exit");
        System.out.print("> ");
    }

    private static void authMenu() {
        while (true) {
            System.out.println("\n-- Auth --");
            System.out.println("1. Login");
            System.out.println("0. Back");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> doLogin();
                case "0" -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("\n-- User --");
            System.out.println("1. Get Info");
            System.out.println("2. Get Restrictions");
            System.out.println("3. List Sessions");
            System.out.println("4. Invalidate Session");
            System.out.println("5. Logout (invalidate current session)");
            System.out.println("6. Send Verification Email");
            System.out.println("0. Back");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> doGetInfo();
                case "2" -> doGetRestrictions();
                case "3" -> doListSessions();
                case "4" -> doInvalidateSession();
                case "5" -> doLogout();
                case "6" -> doVerifyEmail();
                case "0" -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private static void transactionMenu() {
        while (true) {
            System.out.println("\n-- Transactions --");
            System.out.println("1. Transfer");
            System.out.println("2. Get Transaction");
            System.out.println("3. List Transactions");
            System.out.println("0. Back");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> doTransfer();
                case "2" -> doGetTransaction();
                case "3" -> doListTransactions();
                case "0" -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private static void paymentLinkMenu() {
        while (true) {
            System.out.println("\n-- Payment Links --");
            System.out.println("1. Create Payment Link");
            System.out.println("2. List My Payment Links");
            System.out.println("3. Get Payment Link (public)");
            System.out.println("4. Claim Payment Link");
            System.out.println("5. Cancel Payment Link");
            System.out.println("0. Back");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> doCreatePaymentLink();
                case "2" -> doListPaymentLinks();
                case "3" -> doGetPaymentLink();
                case "4" -> doClaimPaymentLink();
                case "5" -> doCancelPaymentLink();
                case "0" -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    private static void infoMenu() {
        while (true) {
            System.out.println("\n-- Public Info --");
            System.out.println("1. Leaderboard");
            System.out.println("2. Team");
            System.out.println("0. Back");
            System.out.print("> ");
            switch (sc.nextLine().trim()) {
                case "1" -> doLeaderboard();
                case "2" -> doTeam();
                case "0" -> { return; }
                default -> System.out.println("Unknown option.");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Auth actions
    // -------------------------------------------------------------------------

    private static void doLogin() {
        System.out.print("Username/email: ");
        String user = sc.nextLine().trim();
        System.out.print("Password: ");
        String password = sc.nextLine().trim();

        String otp = null;
        if (client.context().useOTP()) {
            System.out.print("TOTP code: ");
            otp = sc.nextLine().trim();
        }

        try {
            Response<LoginResponse> res = client.auth().login(user, password, otp).join();
            if (res.success()) {
                System.out.println("Logged in successfully.");
                System.out.println("  Session ID : " + res.data().sessionID());
                System.out.println("  User ID    : " + res.data().user().id());
                System.out.println("  Username   : " + res.data().user().username());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    // -------------------------------------------------------------------------
    // User actions
    // -------------------------------------------------------------------------

    private static void doGetInfo() {
        try {
            Response<GetInfoResponse> res = client.user().getInfo().join();
            if (res.success()) {
                GetInfoResponse d = res.data();
                System.out.println("  ID          : " + d.id());
                System.out.println("  Username    : " + d.username());
                System.out.println("  Name        : " + d.firstName() + " " + d.lastName());
                System.out.println("  Email       : " + d.email());
                System.out.println("  DOB         : " + d.dateOfBirthOn());
                System.out.println("  Balance     : " + formatPaisa(d.balance()));
                System.out.println("  Role        : " + d.role());
                System.out.println("  2FA Enabled : " + d.isMFA());
                System.out.println("  Created     : " + d.createdAt());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doGetRestrictions() {
        try {
            Response<GetRestrictionsResponse> res = client.user().getRestrictions().join();
            if (res.success()) {
                var r = res.data().restrictions();
                if (r.frozen() == null && r.eightysixed() == null) {
                    System.out.println("  No active restrictions.");
                    return;
                }
                printRestriction("frozen", r.frozen());
                printRestriction("eightysixed", r.eightysixed());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doListSessions() {
        try {
            Response<GetSessionsResponse> res = client.user().getSessions().join();
            if (res.success()) {
                UserSession[] sessions = res.data().sessions();
                if (sessions.length == 0) { System.out.println("  No sessions."); return; }
                for (UserSession s : sessions) {
                    System.out.println("  ---");
                    System.out.println("  ID          : " + s.id());
                    System.out.println("  Device      : " + s.deviceInfo());
                    System.out.println("  IP          : " + s.ip());
                    System.out.println("  Created     : " + s.createdAt());
                    System.out.println("  Last Active : " + s.lastActive());
                    System.out.println("  Current     : " + s.isCurrent());
                    System.out.println("  Invalidated : " + s.isInvalidated()
                            + (s.invalidatedAt() != null ? " at " + s.invalidatedAt() : ""));
                }
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doInvalidateSession() {
        System.out.print("Session ID to invalidate: ");
        String id = sc.nextLine().trim();
        try {
            Response<Void> res = client.user().invalidateSession(id).join();
            if (res.success()) System.out.println("  Session invalidated.");
            else printError(res);
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doLogout() {
        try {
            Response<Void> res = client.user().logout().join();
            if (res.success()) System.out.println("  Logged out.");
            else printError(res);
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doVerifyEmail() {
        try {
            Response<Void> res = client.user().verifyEmail().join();
            if (res.success()) System.out.println("  Verification email sent.");
            else printError(res);
        } catch (Exception e) {
            printException(e);
        }
    }

    // -------------------------------------------------------------------------
    // Transaction actions
    // -------------------------------------------------------------------------

    private static void doTransfer() {
        System.out.print("Recipient (username or ID): ");
        String recipient = sc.nextLine().trim();
        System.out.print("Amount (in INR, e.g. 100.00): ");
        BigInteger paisa = inrToPaisa(sc.nextLine().trim());
        if (paisa == null) return;
        System.out.print("Note (optional, press Enter to skip): ");
        String note = sc.nextLine().trim();

        try {
            Response<TransferResponse> res = client.transaction()
                    .transfer(recipient, paisa, note.isEmpty() ? null : note).join();
            if (res.success()) {
                System.out.println("  Transfer confirmed.");
                System.out.println("  Transaction ID : " + res.data().transactionID());
                System.out.println("  Status         : " + res.data().status());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doGetTransaction() {
        System.out.print("Transaction ID (TXN-xxx or numeric ID): ");
        String id = sc.nextLine().trim();
        try {
            Response<Transaction> res = client.transaction().get(id).join();
            if (res.success()) {
                printTransaction(res.data());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doListTransactions() {
        try {
            Response<GetTransactionListResponse> res = client.transaction().getList().join();
            if (res.success()) {
                Transaction[] txns = res.data().transactions();
                if (txns.length == 0) { System.out.println("  No transactions."); return; }
                for (Transaction t : txns) {
                    System.out.println("  ---");
                    printTransaction(t);
                }
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    // -------------------------------------------------------------------------
    // Payment Link actions
    // -------------------------------------------------------------------------

    private static void doCreatePaymentLink() {
        System.out.print("Amount (in INR, e.g. 100.00): ");
        BigInteger paisa = inrToPaisa(sc.nextLine().trim());
        if (paisa == null) return;
        System.out.print("Note (optional, press Enter to skip): ");
        String note = sc.nextLine().trim();

        try {
            Response<CreatePaymentLinkResponse> res = client.paymentLink()
                    .create(paisa, note.isEmpty() ? null : note).join();
            if (res.success()) {
                CreatePaymentLinkResponse d = res.data();
                System.out.println("  Created successfully.");
                System.out.println("  ID     : " + d.id());
                System.out.println("  Token  : " + d.token());
                System.out.println("  Amount : " + formatPaisa(d.amount()));
                System.out.println("  Note   : " + d.note());
                System.out.println("  URL    : " + d.url());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doListPaymentLinks() {
        try {
            Response<GetPaymentLinkListResponse> res = client.paymentLink().getList().join();
            if (res.success()) {
                PaymentLink[] links = res.data().links();
                if (links.length == 0) { System.out.println("  No payment links."); return; }
                for (PaymentLink l : links) {
                    System.out.println("  ---");
                    System.out.println("  ID      : " + l.id());
                    System.out.println("  Token   : " + l.token());
                    System.out.println("  Amount  : " + formatPaisa(l.amount()));
                    System.out.println("  Note    : " + l.note());
                    System.out.println("  Status  : " + l.status());
                    System.out.println("  Created : " + l.createdAt());
                    System.out.println("  URL     : " + l.url());
                }
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doGetPaymentLink() {
        System.out.print("Token: ");
        String token = sc.nextLine().trim();
        try {
            Response<GetPaymentLinkResponse> res = client.paymentLink().get(token).join();
            if (res.success()) {
                GetPaymentLinkResponse d = res.data();
                System.out.println("  Amount  : " + formatPaisa(d.amount()));
                System.out.println("  Creator : " + d.creator().username());
                System.out.println("  Note    : " + d.note());
                System.out.println("  Created : " + d.createdAt());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doClaimPaymentLink() {
        System.out.print("Token: ");
        String token = sc.nextLine().trim();
        try {
            Response<ClaimPaymentLinkResponse> res = client.paymentLink().claim(token).join();
            if (res.success()) {
                System.out.println("  Claimed successfully.");
                System.out.println("  Transaction ID : " + res.data().transactionID());
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doCancelPaymentLink() {
        System.out.print("Token: ");
        String token = sc.nextLine().trim();
        try {
            Response<Void> res = client.paymentLink().cancel(token).join();
            if (res.success()) System.out.println("  Payment link cancelled and refunded.");
            else printError(res);
        } catch (Exception e) {
            printException(e);
        }
    }

    // -------------------------------------------------------------------------
    // Info actions
    // -------------------------------------------------------------------------

    private static void doLeaderboard() {
        try {
            Response<GetLeaderboardResponse> res = client.info().getLeaderboard().join();
            if (res.success()) {
                LeaderboardUser[] board = res.data().leaderboard();
                if (board.length == 0) { System.out.println("  No entries."); return; }
                for (int i = 0; i < board.length; i++) {
                    System.out.printf("  #%d  %-20s  %s%n",
                            i + 1, board[i].username(), formatPaisa(board[i].balance()));
                }
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    private static void doTeam() {
        try {
            Response<GetTeamResponse> res = client.info().getTeam().join();
            if (res.success()) {
                TeamMember[] team = res.data().team();
                if (team.length == 0) { System.out.println("  No team members."); return; }
                for (TeamMember m : team) {
                    System.out.println("  ---");
                    System.out.println("  Name   : " + m.name());
                    System.out.println("  Role   : " + m.role());
                    System.out.println("  Joined : " + m.joinedAt());
                    System.out.println("  Avatar : " + m.avatarURL());
                    if (m.socials() != null) {
                        if (m.socials().website()  != null) System.out.println("  Web    : " + m.socials().website());
                        if (m.socials().twitter()  != null) System.out.println("  Twitter: " + m.socials().twitter());
                        if (m.socials().github()   != null) System.out.println("  GitHub : " + m.socials().github());
                        if (m.socials().youtube()  != null) System.out.println("  YouTube: " + m.socials().youtube());
                    }
                }
            } else {
                printError(res);
            }
        } catch (Exception e) {
            printException(e);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static void printTransaction(Transaction t) {
        System.out.println("  ID     : " + t.transactionID());
        System.out.println("  From   : " + t.sender().username());
        System.out.println("  To     : " + t.recipient().username());
        System.out.println("  Amount : " + formatPaisa(t.amount()));
        System.out.println("  Note   : " + (t.note() != null ? t.note() : "(none)"));
        System.out.println("  Status : " + t.status());
        System.out.println("  Date   : " + t.createdAt());
    }

    private static void printRestriction(String name, Restriction r) {
        if (r == null || !r.active()) return;
        System.out.println("  [" + name + "]");
        System.out.println("    Active     : true");
        System.out.println("    Value      : " + (r.value() != null ? r.value() : "(none)"));
        System.out.println("    Expires At : " + (r.expiresAt() != null ? r.expiresAt() : "never"));
    }

    private static <T> void printError(Response<T> res) {
        if (res.error() != null) {
            System.out.println("  Error " + res.error().code() + ": " + res.error().description());
        } else {
            System.out.println("  Request failed (no error code returned).");
        }
    }

    private static void printException(Exception e) {
        System.out.println("  Exception: " + e.getMessage());
    }

    /** Formats a paisa BigInteger as "₹1,234.56" */
    private static String formatPaisa(BigInteger paisa) {
        BigDecimal inr = new BigDecimal(paisa).movePointLeft(2);
        return String.format("₹%,.2f", inr);
    }

    /** Parses an INR string like "100.50" into paisa, returns null on bad input. */
    private static BigInteger inrToPaisa(String input) {
        try {
            BigDecimal inr = new BigDecimal(input).movePointRight(2);
            return inr.toBigIntegerExact();
        } catch (Exception e) {
            System.out.println("  Invalid amount. Use decimal format, e.g. 100.00");
            return null;
        }
    }
}