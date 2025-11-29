package knx.planner.project.controller;

import knx.planner.project.auth.AuthUtil;
import knx.planner.project.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProfileController {

    private final ObjectMapper om;

    public ProfileController(ObjectMapper om) {
        this.om = om;
    }

    @GetMapping("/my-account")
    public ResponseEntity<?> meFromContext() {
        System.out.println("Fetching current user profile");
        return ResponseEntity.ok(AuthUtil.getAuthentication());
    }
}
