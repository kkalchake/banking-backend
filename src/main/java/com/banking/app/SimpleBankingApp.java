package com.banking.app;

import com.americanexpress.unify.jdocs.Document;
import com.americanexpress.unify.jdocs.JDocument;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class SimpleBankingApp {
    // In-memory storage for demo
    private final Map<String, String> users = new HashMap<>(); // username -> password
    private final Map<String, Document> accounts = new HashMap<>(); // accNum -> JDocs Document
    // Map username to list of account numbers
    private final Map<String, List<String>> userAccounts = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(SimpleBankingApp.class, args);
    }

    // Register a user
    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (users.containsKey(username)) return "User exists";
        users.put(username, password);
        return "Registered";
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return password.equals(users.get(username)) ? "Login success" : "Invalid credentials";
    }

    // Create account
    @PostMapping("/account")
    public String createAccount(@RequestParam String username) {
    if (!users.containsKey(username)) return "No such user";
    String accNum = UUID.randomUUID().toString();
    Document acc = new JDocument();
    acc.setString("$.owner", username);
    acc.setBigDecimal("$.balance", BigDecimal.ZERO);
    accounts.put(accNum, acc);
    userAccounts.computeIfAbsent(username, k -> new ArrayList<>()).add(accNum);
    return accNum;
    }

    // Get balance
    @GetMapping("/balance")
    public String balance(@RequestParam String accNum) {
        Document acc = accounts.get(accNum);
        return acc == null ? "No such account" : acc.getBigDecimal("$.balance").toString();
    }

    // Deposit
    @PostMapping("/deposit")
    public String deposit(@RequestParam String accNum, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return "Amount must be positive";
        Document acc = accounts.get(accNum);
        if (acc == null) return "No such account";
        BigDecimal bal = acc.getBigDecimal("$.balance");
        acc.setBigDecimal("$.balance", bal.add(amount));
        return "Deposited";
    }

    // Withdraw
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam String accNum, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return "Amount must be positive";
        Document acc = accounts.get(accNum);
        if (acc == null) return "No such account";
        BigDecimal bal = acc.getBigDecimal("$.balance");
        if (bal.compareTo(amount) < 0) return "Insufficient funds";
        acc.setBigDecimal("$.balance", bal.subtract(amount));
        return "Withdrawn";
    }

    // Transfer
    @PostMapping("/transfer")
    public String transfer(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) return "Amount must be positive";
        Document src = accounts.get(from);
        Document dst = accounts.get(to);
        if (src == null || dst == null) return "Account not found";
        BigDecimal srcBal = src.getBigDecimal("$.balance");
        if (srcBal.compareTo(amount) < 0) return "Insufficient funds";
        src.setBigDecimal("$.balance", srcBal.subtract(amount));
        dst.setBigDecimal("$.balance", dst.getBigDecimal("$.balance").add(amount));
        return "Transferred";
    }

    // List all accounts for a user (returns list of account numbers)
    @GetMapping("/accounts")
    public List<String> listAccounts(@RequestParam String username) {
        if (!users.containsKey(username)) return Collections.emptyList();
        return userAccounts.getOrDefault(username, Collections.emptyList());
    }

    // Get all account details for a user (returns list of account JSON)
    @GetMapping("/accounts/details")
    public List<Map<String, Object>> listAccountDetails(@RequestParam String username) {
        if (!users.containsKey(username)) return Collections.emptyList();
        List<String> accNums = userAccounts.getOrDefault(username, Collections.emptyList());
        List<Map<String, Object>> details = new ArrayList<>();
        for (String accNum : accNums) {
            Document acc = accounts.get(accNum);
            if (acc != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("accNum", accNum);
                map.put("owner", acc.getString("$.owner"));
                map.put("balance", acc.getBigDecimal("$.balance"));
                details.add(map);
            }
        }
        return details;
    }
}
