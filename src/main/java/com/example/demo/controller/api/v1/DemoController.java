package com.example.demo.controller.api.v1;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> get(){
        var runtime = Runtime.getRuntime();
        String info = String.format("maxMemory=%,d%stotalMemory=%,d%sfreeMemory=%,d",
                                    runtime.maxMemory(),
                                    System.lineSeparator(),
                                    runtime.totalMemory(),
                                    System.lineSeparator(),
                                    runtime.freeMemory());
        return ResponseEntity.ok(info);
    }

    @GetMapping("/add")
    public ResponseEntity<String> post(@RequestParam String email, HttpSession session) {

        session.setAttribute("emailAddress", email + "_" + session.getId());

        StringBuilder buf = new StringBuilder();
        for (int i = 1; i <= 10000; i++) {
            buf.append(i).append(":").append(email).append(System.lineSeparator());
        }
        session.setAttribute("dataString", buf.toString());

        return ResponseEntity.ok("ok:" + session.getId());
    }

    private static class DemoRequest {

        private String emailAddress;

        public String getEmailAddress() {
            return emailAddress;
        }

        public void setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
        }

    }

}
