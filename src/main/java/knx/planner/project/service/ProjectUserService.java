package knx.planner.project.service;

import knx.planner.project.entity.ProjectUser;
import knx.planner.project.repository.ProjectUserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectUserService {
    ProjectUserRepository projectUserRepo;

    public ProjectUserService(ProjectUserRepository projectUserRepo) {
        this.projectUserRepo = projectUserRepo;
    }

    void saveProjectUser(Long projectId, Long userId) {
        this.projectUserRepo.findByProjectUidAndUserId(projectId, userId).orElseGet(() -> {;
            ProjectUser pu = new ProjectUser();
            pu.setProjectUid(projectId);
            pu.setUserId(userId);
            return this.projectUserRepo.save(pu);
        });
    }

}
