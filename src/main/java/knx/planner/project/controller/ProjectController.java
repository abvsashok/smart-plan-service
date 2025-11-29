package knx.planner.project.controller;

import knx.planner.project.dto.ProjectRequestDto;
import knx.planner.project.dto.response.ProjectRespDto;
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



    @GetMapping("/data/{id}")
    public ResponseEntity<?> getProjectDetails(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.projectService.getProjectDetails(id));
    }

    @PostMapping("/save")
    public ResponseEntity<?> createProject(@RequestBody ProjectRequestDto input) {
        System.out.println("Project Input: ");
        this.projectService.saveProject(input);
        return ResponseEntity.ok("Project saved successfully");
    }



}
