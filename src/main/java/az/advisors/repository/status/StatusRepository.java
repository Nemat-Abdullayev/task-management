package az.advisors.repository.status;

import az.advisors.model.entity.Status;
import az.advisors.model.enums.StatusName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByStatusName(StatusName statusName);
}
