package az.advisors.service.task;

import az.advisors.model.entity.Status;
import az.advisors.model.enums.StatusName;
import az.advisors.repository.status.StatusRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final StatusRepository statusRepository;

    @Override
    public Status getOne(StatusName statusName) {
        try {
            LOGGER.info("StatusName: " + statusName);
            return statusRepository.findByStatusName(statusName);
        } catch (Exception e) {
            LOGGER.warn("Xəta baş verdi: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
