package knx.planner.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class EdgeRequestDto {
    private String id;
    private Map<String, Object> raw;
}
