package knx.planner.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "project")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String name;
    private String description;

    String projectId;

    @Column(columnDefinition = "json")
    private String viewport;

    private Long createdBy;

    private Long updatedBy;

    private Date createdAt;
    private Date updatedAt;

    private String slug;

    // No cascade, LAZY fetch to avoid automatic persistence/removal of children.
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "projectUid", insertable = false, updatable = false)
    private List<Node> nodes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "projectUid", insertable = false, updatable = false)
    private List<Edge> edges = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

}
