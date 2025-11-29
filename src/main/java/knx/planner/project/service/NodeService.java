package knx.planner.project.service;

import jakarta.transaction.Transactional;
import knx.planner.project.entity.Node;
import knx.planner.project.repository.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService {

    NodeRepository nodeRepo;
    public NodeService(
            NodeRepository nodeRepo
    ) {
        this.nodeRepo = nodeRepo;

    }

    @Transactional
    void saveNodes(List<Node> nodes) {
        this.nodeRepo.saveAll(nodes);
    }

    @Transactional
    void deleteNodesByProjectId(Long projectId) {
        this.nodeRepo.deleteByProjectId(projectId);
    }

}
