package az.advisors.service.user;

import az.advisors.model.entity.UserRole;
import az.advisors.model.enums.RoleName;
import az.advisors.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public UserRole findOne(RoleName roleName) {
        try {
            return roleRepository.findByRoleName(roleName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
