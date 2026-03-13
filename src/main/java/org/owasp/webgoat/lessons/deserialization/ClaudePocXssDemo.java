package org.owasp.webgoat.lessons.deserialization;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClaudePocXssDemo {

    @GetMapping("/VulnerableDemo/xss")
    public String reflectedXss(@RequestParam String name) {

        // Vulnerability:
        // User-controlled input is directly embedded into HTML
        // without output encoding or sanitization.
        return "<html><body>Welcome " + name + "</body></html>";
    }
}