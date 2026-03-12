package org.owasp.webgoat.lessons.deserialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClaudePocVulnerableDemo {

    // Hardcoded credentials / secret
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String JWT_SECRET = "super-secret-signing-key";

    // Weak authentication / auth bypass
    @PostMapping("/VulnerableDemo/login")
    public String login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String isAdmin) {
        if ("true".equalsIgnoreCase(isAdmin)) {
            return "Login successful as admin";
        }

        if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
            return "Login successful";
        }

        return "Invalid credentials";
    }

    // SQL Injection
    @GetMapping("/VulnerableDemo/user")
    public String getUser(@RequestParam String username) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
        Statement stmt = conn.createStatement();

        String query = "SELECT * FROM users WHERE username = '" + username + "'";
        ResultSet rs = stmt.executeQuery(query);

        StringBuilder output = new StringBuilder();
        while (rs.next()) {
            output.append(rs.getString(1)).append(" ");
        }

        rs.close();
        stmt.close();
        conn.close();

        return output.toString();
    }

    // Path Traversal
    @GetMapping("/VulnerableDemo/readFile")
    public String readFile(@RequestParam String fileName) throws IOException {
        Path path = Paths.get("C:/temp/uploads/" + fileName);
        return Files.readString(path);
    }

    // Insecure Deserialization
    @PostMapping("/VulnerableDemo/deserialize")
    public String deserialize(@RequestParam String data) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(data);
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(decoded));
        Object obj = ois.readObject();
        ois.close();
        return "Deserialized object: " + obj;
    }

    // Command Injection
    @GetMapping("/VulnerableDemo/ping")
    public String ping(@RequestParam String host) throws IOException {
        Process process = Runtime.getRuntime().exec("cmd /c ping " + host);
        return "Ping started for " + host;
    }

    // Sensitive information exposure
    @GetMapping("/VulnerableDemo/debug")
    public String debug() {
        return "admin=" + ADMIN_USERNAME + ", password=" + ADMIN_PASSWORD + ", jwtSecret=" + JWT_SECRET;
    }
}