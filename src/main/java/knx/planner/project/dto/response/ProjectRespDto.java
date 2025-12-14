package knx.planner.project.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter @Setter
public class ProjectRespDto {

    private Long uid;
    private String projectId;
    private String name;
    private String description;
    private String slug;
    private String passcode;

    private List<Map<String, Object>> nodes;
    private List<Map<String, Object>> edges;
    private Map<String, Object> viewport;

}
