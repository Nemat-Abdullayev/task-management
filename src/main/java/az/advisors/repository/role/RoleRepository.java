package az.advisors.repository.role;

import az.advisors.model.entity.UserRole;
import az.advisors.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findByRoleName(RoleName roleName);
}
