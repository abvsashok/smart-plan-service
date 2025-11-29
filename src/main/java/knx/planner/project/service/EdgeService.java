package knx.planner.project.service;

import jakarta.transaction.Transactional;
import knx.planner.project.entity.Edge;
import knx.planner.project.repository.EdgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EdgeService {

    EdgeRepository edgeRepo;
    public EdgeService(
            EdgeRepository edgeRepo
    ) {
        this.edgeRepo = edgeRepo;

    }

    @Transactional
    void saveEdges(List<Edge> edges) {
        this.edgeRepo.saveAll(edges);
    }

    @Transactional
    void deleteEdgesByProjectUid(Long projectId) {
        this.edgeRepo.deleteByProjectId(projectId);
    }
}
