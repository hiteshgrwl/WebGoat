package org.owasp.webgoat.lessons.deserialization;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClaudePocSsrfDemo {

    @GetMapping("/SsrfDemo/fetch")
    public String fetch(@RequestParam String targetUrl) throws Exception {
        URL url = new URL(targetUrl);
        URLConnection connection = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }

        reader.close();
        return response.toString();
    }
}