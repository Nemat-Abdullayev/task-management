package az.advisors.model.entity;

import az.advisors.model.base.BaseEntity;
import az.advisors.util.format.JsonLocalDateDeserializer;
import az.advisors.util.format.JsonLocalDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TASK")
public class Task extends BaseEntity {

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "RANK", columnDefinition = "int default 10")
    private int rank;

    @Column(name = "DEAD_LINE")
    @JsonSerialize(using = JsonLocalDateSerializer.class)
    @JsonDeserialize(using = JsonLocalDateDeserializer.class)
    private LocalDate deadLine;

    @OneToOne
    private Status status;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

}
