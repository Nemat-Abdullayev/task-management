package az.advisors.model.entity;

import az.advisors.model.base.BaseEntity;
import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "A_USER")
public class User extends BaseEntity {

    @Column(name = "USERNAME", length = 20, unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "NAME", length = 20)
    private String name;

    @Column(name = "SURNAME", length = 20)
    private String surname;

    @Column(name = "AGE")
    private int age;

    @OneToOne
    private UserRole userRole;

    @OneToMany(mappedBy = "assignedTo")
    private List<Task> tasks;
}
