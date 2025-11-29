package knx.planner.project.repository;

import knx.planner.project.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NodeRepository extends JpaRepository<Node, Long> {

    @Query("""
        DELETE FROM Node n WHERE n.projectUid = :projectId
                """)
    void deleteByProjectId(Long projectId);
}
