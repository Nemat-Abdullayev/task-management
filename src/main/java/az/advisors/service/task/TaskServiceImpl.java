package az.advisors.service.task;

import az.advisors.mapper.TaskMapper;
import az.advisors.model.entity.Status;
import az.advisors.model.entity.Task;
import az.advisors.model.entity.User;
import az.advisors.model.enums.RoleName;
import az.advisors.model.enums.StatusName;
import az.advisors.model.view.TaskRequest;
import az.advisors.model.view.TaskResponse;
import az.advisors.model.view.TaskResponseWithStatus;
import az.advisors.repository.status.StatusRepository;
import az.advisors.repository.task.TaskRepository;
import az.advisors.security.JwtTokenUtil;
import az.advisors.service.user.UserService;
import az.advisors.util.exception.UserNotFoundException;
import lombok.extern.java.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final TaskRepository taskRepository;
    private final String jwtHeader;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final StatusRepository statusRepository;

    public TaskServiceImpl(TaskRepository taskRepository,
                           @Value("${jwt.header}") String jwtHeader,
                           JwtTokenUtil jwtTokenUtil,
                           UserService userService,
                           StatusRepository statusRepository) {
        this.taskRepository = taskRepository;
        this.jwtHeader = jwtHeader;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.statusRepository = statusRepository;
    }

    @Override
    @Transactional
    public ResponseEntity<?> createTask(TaskRequest taskRequest, HttpServletRequest request) {
        try {
            LOGGER.info("TaskRequestObject:" + taskRequest.toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(taskRequest.getDeadLine(), formatter);
            String[] data = getUserDataFromHttpServletRequest(request).split("#");
            LOGGER.info("UserRole while creating task: " + data[0]);
            User assignedByUser = userService.findByUsername(data[0]);
            User assignedToUser = userService.findByUsername(taskRequest.getAssignedUsernameOfUser());
            if (Objects.nonNull(assignedByUser) && Objects.nonNull(assignedToUser)) {
                if (assignedByUser.getUserRole().getRoleName().equals(RoleName.TEACHER)) {
                    if (assignedToUser.getUserRole().getRoleName().equals(RoleName.STUDENT)) {
                        List<Task> optionalTask = taskRepository.getAllByAssignedTo(assignedToUser);
                        Task result = optionalTask.stream().filter(task -> task.getRank() == 0).findFirst().orElse(null);
                        if (Objects.isNull(result)) {
                            Task task = Task.builder()
                                    .assignedBy(assignedByUser)
                                    .assignedTo(assignedToUser)
                                    .content(taskRequest.getContent())
                                    .deadLine(localDate)
                                    .rank(10)
                                    .build();
                            return ResponseEntity.ok(taskRepository.save(task));
                        } else {
                            LOGGER.debug("you cannot assigned task to user which task rank=0!==>" + "Rank:" + result.getRank());
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you cannot assigned task to user which task rank=0!");
                        }
                    } else {
                        LOGGER.debug("assigned to user role is not STUDENT role==>UserRole: " + assignedToUser.getUserRole().getRoleName());
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("assigned to user role is not STUDENT role");
                    }
                } else {
                    LOGGER.debug("Only TEACHER role user can create task!==> UserRole :" + assignedByUser.getUserRole().getRoleName());
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only TEACHER role user can create task!");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("assigned by user or assigned to user not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("An error while create task==>" + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error while create task");
        }
    }

    @Override
    public List<TaskResponse> getTasks(HttpServletRequest request) {
        try {
            String[] data = getUserDataFromHttpServletRequest(request).split("#");
            LOGGER.info("Enter input UserRole :" + data[1]);
            User user = userService.findByUsername(data[0]);
            List<Task> tasks = taskRepository.getAllByAssignedTo(user);
            List<TaskResponse> taskResponses = new ArrayList<>();
            if (!tasks.isEmpty()) {
                for (Task task : tasks) {
                    TaskResponse taskResponse = TaskMapper.INSTANCE.mapEntityToView(task);
                    taskResponses.add(taskResponse);
                    LOGGER.debug("Task response object after mapping entity to dto==>" + taskResponse.toString());
                }
                return taskResponses;
            } else {
                LOGGER.debug("The return lis is empty in get task method");
                return Collections.EMPTY_LIST;
            }
        } catch (Exception e) {
            LOGGER.warn("An error while get tasks==>" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> update(Long taskId, HttpServletRequest request) throws UserNotFoundException {
        LOGGER.info("Enter input taskId=" + taskId);
        String[] data = getUserDataFromHttpServletRequest(request).split("#");
        User user = userService.findByUsername(data[0]);
        if (Objects.nonNull(user)) {
            if (user.getUserRole().getRoleName().equals(RoleName.STUDENT)) {
                List<Task> tasks = taskRepository.getAllByAssignedTo(user);
                Optional<Task> matchingObject = tasks.stream().
                        filter(t -> t.getId().equals(taskId)).
                        findFirst();
                Task task = matchingObject.get();
                if (LocalDate.now().isBefore(task.getDeadLine())) {
                    LOGGER.debug("Dead line of task is after current date");
                    Status status = statusRepository.findByStatusName(StatusName.SUCCESS);
                    task.setStatus(status);
                } else {
                    LOGGER.debug("Dead line of task is before current date");
                    Status status = statusRepository.findByStatusName(StatusName.MISS);
                    task.setRank(-1);
                    task.setStatus(status);
                }
                taskRepository.save(task);
                return ResponseEntity.ok("OK");
            } else {
                LOGGER.debug("you user role is not STUDENT ==>[" + user.getUserRole().getRoleName() + "]");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("you user role is not STUDENT!");
            }
        } else {
            LOGGER.debug("user not found by username==> user is null");
            throw new UserNotFoundException("user not found by username");
        }

    }

    @Override
    public List<TaskResponseWithStatus> taskResponseWithStatusList(HttpServletRequest request) {
        try {
            String[] data = getUserDataFromHttpServletRequest(request).split("#");
            LOGGER.debug("User role==>[" + data[1] + "]");
            User user = userService.findByUsername(data[0]);
            if (user.getUserRole().getRoleName().equals(RoleName.TEACHER)) {
                List<Task> taskList = taskRepository.findAll();
                List<TaskResponseWithStatus> taskResponseWithStatuses = new ArrayList<>();
                if (!taskList.isEmpty()) {
                    for (Task task : taskList) {
                        taskResponseWithStatuses.add(TaskMapper.INSTANCE.mapToView(task));
                    }
                    return taskResponseWithStatuses;
                } else {
                    return Collections.EMPTY_LIST;
                }
            } else {
                LOGGER.debug("User role isn't a TEACHER role==> [" + user.getUserRole().getRoleName() + "]");
                return Collections.EMPTY_LIST;
            }
        } catch (Exception e) {
            LOGGER.warn("An error while get all task with statuses==>[" + e.getMessage() + "]");
            e.printStackTrace();
            return null;
        }
    }

    private String getUserDataFromHttpServletRequest(HttpServletRequest request) {
        final String requestHeader = request.getHeader(this.jwtHeader);
        LOGGER.debug("Request header from yaml file==> [" + requestHeader + "]");
        if (Objects.nonNull(requestHeader)
                && requestHeader.startsWith("Bearer ")
                && !requestHeader.contains("null")) {
            String authorizationToken = requestHeader.substring(7);
            LOGGER.debug("Return token after substring Bearer==>Token :[" + authorizationToken + "]");
            return jwtTokenUtil.getUserNameFromToken(authorizationToken);
        } else {
            LOGGER.debug("Request header is null or not start with Bearer");
            return null;
        }
    }
}
