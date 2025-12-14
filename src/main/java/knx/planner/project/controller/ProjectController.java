package knx.planner.project.controller;

import knx.planner.project.dto.ProjectRequestDto;
import knx.planner.project.dto.response.ProjectRespDto;
import knx.planner.project.entity.Project;
import knx.planner.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    ProjectService projectService;
    ObjectMapper objectMapper;

    public ProjectController(
             ProjectService projectService
    ) {
        this.projectService = projectService;
        this.objectMapper = new ObjectMapper();

    }

    @GetMapping("/test")
    public ResponseEntity<?> isAppRunning() {
        return ResponseEntity.ok("Application is running");
    }

    @GetMapping("/data")
    public ResponseEntity<List<ProjectRespDto>> getProjects() {
        return ResponseEntity.ok(this.projectService.getProjects());
    }

//    @GetMapping("/passcode/{passcode}")
//    public ResponseEntity<List<ProjectRespDto>> getProjectsByPassCode(@PathVariable("passcode") String passcode) {
//        return ResponseEntity.ok(this.projectService.getProjectsByPasscode(passcode));
//    }

    @GetMapping("/my-projects")
    public ResponseEntity<List<ProjectRespDto>> getMyProjects() {
        return ResponseEntity.ok(this.projectService.getProjects());
    }



    @GetMapping("/data/{slug}")
    public ResponseEntity<?> getProjectDetails(@PathVariable("slug") String slug) {
        return ResponseEntity.ok(this.projectService.getProjectDetails(slug));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createProject(@RequestBody ProjectRequestDto input) {
        Project project = this.projectService.saveProject(input);
        return ResponseEntity.ok(this.projectService.getProjectDetails(project.getSlug()));
    }



}
