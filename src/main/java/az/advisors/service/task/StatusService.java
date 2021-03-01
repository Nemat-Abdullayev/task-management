package az.advisors.service.task;

import az.advisors.model.entity.Status;
import az.advisors.model.enums.StatusName;

public interface StatusService {
    Status getOne(StatusName statusName);
}
