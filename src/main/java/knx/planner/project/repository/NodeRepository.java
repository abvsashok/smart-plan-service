package knx.planner.project.repository;

import knx.planner.project.entity.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NodeRepository extends JpaRepository<Node, Long> {

    @Modifying
    @Query("""
        DELETE FROM Node n WHERE n.projectUid = :projectUid
                """)
    void deleteByProjectId(@Param("projectUid") Long projectUid);
}
