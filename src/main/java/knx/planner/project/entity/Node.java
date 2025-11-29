package knx.planner.project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Node {

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
