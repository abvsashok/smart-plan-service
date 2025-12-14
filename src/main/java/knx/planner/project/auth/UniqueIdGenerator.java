package knx.planner.project.auth;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UniqueIdGenerator {

    public String generateUuidSlug() {
        return UUID.randomUUID().toString();
    }

    public String generateShortUuidSlug(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
