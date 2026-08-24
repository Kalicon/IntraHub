package com.example.demo.security;

import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

@Component
public class SecuritySanitizer {

    public String sanitizeText(String input) {
        if (input == null) return null;
        String sanitized = HtmlUtils.htmlEscape(input.trim());
        return sanitized;
    }

    public String sanitizeInput(String input) {
        if (input == null) return "";
        return input.replaceAll("(?i)<script.*?>.*?</script>", "")
                    .replaceAll("(?i)<iframe.*?>.*?</iframe>", "")
                    .replaceAll("(?i)javascript:", "")
                    .replaceAll("(?i)onload=.*?", "");
    }
}