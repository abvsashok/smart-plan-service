package knx.planner.project.auth;

import knx.planner.project.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

@Getter
@Setter
public class AuthUtil {

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        }
        return null;
    }

    public static User getAuthentication() {
        return (User) Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
    }

    public static Long getSubmitter(){
        return getAuthentication().getId();
    }
}
