package knx.planner.project.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter @Setter @Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDto {

    private Long uid;
    private String name;
    private String description;
    private String projectId;
    List<Map<String, Object>> nodes;
    List<Map<String, Object>> edges;
    ViewportDto viewport;

}
