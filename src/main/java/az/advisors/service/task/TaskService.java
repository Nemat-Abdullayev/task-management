package az.advisors.service.task;

import az.advisors.model.view.TaskRequest;
import az.advisors.model.view.TaskResponse;
import az.advisors.model.view.TaskResponseWithStatus;
import az.advisors.util.exception.UserNotFoundException;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface TaskService {
    ResponseEntity<?> createTask(TaskRequest taskRequest, HttpServletRequest request);

    List<TaskResponse> getTasks(HttpServletRequest request);

    ResponseEntity<?> update(Long taskId, HttpServletRequest request) throws UserNotFoundException;

    List<TaskResponseWithStatus> taskResponseWithStatusList(HttpServletRequest request);
}
