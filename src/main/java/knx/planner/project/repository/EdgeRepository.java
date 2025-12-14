package knx.planner.project.repository;

import knx.planner.project.entity.Edge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EdgeRepository extends JpaRepository<Edge, Long> {

    @Modifying
    @Query("""
        DELETE FROM Edge n WHERE n.projectUid = :projectUid
                """)
    void deleteByProjectId(@Param("projectUid") Long projectUid);
}
