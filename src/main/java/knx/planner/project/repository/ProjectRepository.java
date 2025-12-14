package knx.planner.project.repository;

import knx.planner.project.entity.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("""
            SELECT p FROM Project p
            WHERE p.passcode = :passcode
            """)
    List<Project> getProjectsBypPasscodeVal(@Param("passcode") String passcode);

    @Query("""
            SELECT p FROM Project p
            WHERE p.slug = :slug
            """)
    Project getProjectBySlug(@Param("slug") String slug);

    @Query("""
            SELECT DISTINCT p FROM Project p left join ProjectUser pu ON p.uid = pu.projectUid where pu.userId = :userId
                """)
    List<Project> findProjectsWithProjectUsers(@Param("userId") Long userId);

}
