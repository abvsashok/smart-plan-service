package knx.planner.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import knx.planner.project.auth.AuthUtil;
import knx.planner.project.auth.UniqueIdGenerator;
import knx.planner.project.dto.ProjectRequestDto;
import knx.planner.project.dto.ProjectRespDTO;
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
    UniqueIdGenerator uniqueIdGenerator;
    ProjectUserService projectUserService;

    public ProjectService (
            ProjectRepository projectRepo,
            NodeService nodeService,
            EdgeService edgeService,
            UniqueIdGenerator uniqueIdGenerator,
            ProjectUserService projectUserService
    ) {
        this.projectRepo = projectRepo;
        this.nodeService = nodeService;
        this.edgeService = edgeService;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.om = new ObjectMapper();
        this.projectUserService = projectUserService;

    }

    public List<ProjectRespDto> getProjects() {
        Long submitter = AuthUtil.getSubmitter();
        List<Project> projects = this.projectRepo.findProjectsWithProjectUsers(submitter);
        List<ProjectRespDto> respDtos = new ArrayList<>();
        for(Project project : projects) {
            ProjectRespDto dto = new ProjectRespDto();
            dto.setUid(project.getUid());
            dto.setName(project.getName());
            dto.setDescription(project.getDescription());
            dto.setProjectId(project.getProjectId());
            dto.setSlug(project.getSlug());
            dto.setPasscode(project.getPasscode());

            respDtos.add(dto);
        }
        return respDtos;
    }
//    public List<ProjectRespDto> getProjectsByPasscode(String passcode) {
//        List<Project> projects = this.projectRepo.getProjectsBypPasscodeVal(passcode);
//        List<ProjectRespDto> respDtos = new ArrayList<>();
//        for(Project project : projects) {
//            ProjectRespDto dto = new ProjectRespDto();
//            dto.setUid(project.getUid());
//            dto.setName(project.getName());
//            dto.setDescription(project.getDescription());
//            dto.setSlug(project.getSlug());
//            dto.setPasscode(project.getPasscode());
//            dto.setProjectId(project.getProjectId());
//
//            respDtos.add(dto);
//        }
//        return respDtos;
//
//    }

    public ProjectRespDTO getProjectDetails(String id) {

        Project projectObj = this.projectRepo.getProjectBySlug(id);
        if(projectObj != null) {
            ViewportDto viewport = null;
            String jsonViewport = projectObj.getViewport();
            if (jsonViewport != null) {
                try {
                    viewport = this.om.readValue(jsonViewport, ViewportDto.class);
                } catch (Exception ignored) {
                }
            }
            ProjectRespDTO dto = ProjectRespDTO.builder()
                    .uid(projectObj.getUid())
                    .name(projectObj.getName())
                    .description(projectObj.getDescription())
                    .projectId(projectObj.getProjectId())
                    .slug(projectObj.getSlug())
                    .passcode(projectObj.getPasscode())
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
    public Project saveProject(ProjectRequestDto inputDto) {
        try {
            Long submitter = AuthUtil.getSubmitter();
            Project project;
            if(inputDto.getUid() != null) {
                project = this.projectRepo.findById(inputDto.getUid()).orElseThrow(() ->  new RuntimeException("Project not found with id: " + inputDto.getUid()));
                this.nodeService.deleteNodesByProjectId(project.getUid());
                this.edgeService.deleteEdgesByProjectUid(project.getUid());
            } else {
                 project = new Project();
                 project.setSlug(this.uniqueIdGenerator.generateShortUuidSlug(8));
                 project.setPasscode(inputDto.getPasscode());
                 project.setCreatedBy(submitter);
            }

            if(project.getPasscode() == null || project.getPasscode().isEmpty()) {
                project.setPasscode(inputDto.getPasscode());
            }

            project.setName(inputDto.getName());
            project.setDescription(inputDto.getDescription());
            project.setProjectId(inputDto.getProjectId());
            project.setViewport(this.om.writeValueAsString(inputDto.getViewport()));
//            System.out.println(submitter);
            project.setUpdatedBy(submitter);
            this.projectRepo.save(project);
            this.projectUserService.saveProjectUser(project.getUid(), submitter);

            List<Node> nodes = new ArrayList<>();
            for (Map<String, Object> nodeDto : inputDto.getNodes()) {
                Node node = new Node();
                node.setProjectUid(project.getUid());

                node.setNodeId((String) nodeDto.get("id"));
                node.setRaw(new ObjectMapper().writeValueAsString(nodeDto));
                nodes.add(node);
            }
            this.nodeService.saveNodes(nodes);
            List<Edge> edges = new ArrayList<>();
            for (Map<String, Object> edgeDto : inputDto.getEdges()) {
                Edge edge = new Edge();
                edge.setEdgeId((String) edgeDto.get("id"));
                edge.setProjectUid(project.getUid());
                edge.setRaw(this.om.writeValueAsString(edgeDto));
                edges.add(edge);
            }
            this.edgeService.saveEdges(edges);
            return project;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON serialization error", e);
        }

    }



}
