package knx.planner.project.repository;

import knx.planner.project.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {

    @Query("""
        DELETE FROM Edge n WHERE n.projectUid = :projectId
                """)
    void deleteByProjectId(Long projectId);
}
