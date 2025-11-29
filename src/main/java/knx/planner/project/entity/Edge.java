package knx.planner.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class Edge {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long uid;

    private String id;

    @Column(columnDefinition = "json")
    private String raw;

    private Long projectUid;

//    @ManyToOne
//    @JoinColumn(name= "project_uid", insertable = false, updatable = false)
//    private Project project;
}
