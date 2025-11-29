package knx.planner.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import knx.planner.project.auth.AuthUtil;
import knx.planner.project.dto.ProjectRequestDto;
import knx.planner.project.dto.ViewportDto;
import knx.planner.project.dto.response.ProjectRespDto;
import knx.planner.project.entity.*;
import knx.planner.project.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService {

    ProjectRepository projectRepo;
    NodeService nodeService;
    EdgeService edgeService;
    ObjectMapper om;

    public ProjectService (
            ProjectRepository projectRepo,
            NodeService nodeService,
            EdgeService edgeService
    ) {
        this.projectRepo = projectRepo;
        this.nodeService = nodeService;
        this.edgeService = edgeService;
        this.om = new ObjectMapper();

    }

    public List<ProjectRespDto> getProjects() {
        List<Project> projects = this.projectRepo.findAll();
        List<ProjectRespDto> respDtos = new ArrayList<>();
        for(Project project : projects) {
            ProjectRespDto dto = new ProjectRespDto();
            dto.setUid(project.getUid());
            dto.setName(project.getName());
            dto.setDescription(project.getDescription());
            dto.setProjectId(project.getProjectId());

            respDtos.add(dto);
        }
        return respDtos;
    }

    public ProjectRequestDto getProjectDetails(Long id) {

        Optional<Project> project = this.projectRepo.findById(id);
        if(project.isPresent()) {
            Project projectObj = project.get();
            ViewportDto viewport = null;
            String jsonViewport = projectObj.getViewport();
            if (jsonViewport != null) {
                try {
                    viewport = this.om.readValue(jsonViewport, ViewportDto.class);
                } catch (Exception ignored) {
                }
            }
            ProjectRequestDto dto = ProjectRequestDto.builder()
                    .uid(projectObj.getUid())
                    .name(projectObj.getName())
                    .description(projectObj.getDescription())
                    .projectId(projectObj.getProjectId())
                    .viewport(viewport)
                    .nodes(projectObj.getNodes().stream().map(node -> {
                        try {
                            return this.om.readValue(node.getRaw(), new TypeReference<Map<String, Object>>() {});
                        } catch (Exception e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).toList())
                    .edges(projectObj.getEdges().stream().map(edge -> {
                        try {
                            return this.om.readValue(edge.getRaw(), new TypeReference<Map<String, Object>>() {});
                        } catch (Exception e) {
                            return null;
                        }
                    }).filter(Objects::nonNull).toList())
                    .build();
            return dto;

        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
    }



    @Transactional
    public void saveProject(ProjectRequestDto inputDto) {
        try {
            Long submitter = AuthUtil.getSubmitter();
            Project project;
            if(inputDto.getUid() != null) {
                project = this.projectRepo.findById(inputDto.getUid()).orElseThrow(() ->  new RuntimeException("Project not found with id: " + inputDto.getUid()));
                this.nodeService.deleteNodesByProjectId(project.getUid());
                this.edgeService.deleteEdgesByProjectUid(project.getUid());
            } else {
                 project = new Project();
                 project.setCreatedBy(submitter);
            }


            project.setName(inputDto.getName());
            project.setDescription(inputDto.getDescription());
            project.setProjectId(inputDto.getProjectId());
            project.setViewport(this.om.writeValueAsString(inputDto.getViewport()));
            System.out.println(submitter);
            project.setUpdatedBy(submitter);
            this.projectRepo.save(project);

            List<Node> nodes = new ArrayList<>();
            for (Map<String, Object> nodeDto : inputDto.getNodes()) {
                Node node = new Node();
                node.setProjectUid(project.getUid());

                node.setId((String) nodeDto.get("id"));
                node.setRaw(new ObjectMapper().writeValueAsString(nodeDto));
                nodes.add(node);
            }
            this.nodeService.saveNodes(nodes);
            List<Edge> edges = new ArrayList<>();
            for (Map<String, Object> edgeDto : inputDto.getEdges()) {
                Edge edge = new Edge();
                edge.setId((String) edgeDto.get("id"));
                edge.setProjectUid(project.getUid());
                edge.setRaw(this.om.writeValueAsString(edgeDto));
                edges.add(edge);
            }
            this.edgeService.saveEdges(edges);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }
    }



}
