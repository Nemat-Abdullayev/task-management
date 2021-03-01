package az.advisors.service.user;

import az.advisors.model.entity.UserRole;
import az.advisors.model.enums.RoleName;

public interface RoleService {
    UserRole findOne(RoleName roleName);
}
