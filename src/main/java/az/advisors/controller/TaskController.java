package az.advisors.controller;

import az.advisors.model.view.TaskRequest;
import az.advisors.model.view.TaskResponse;
import az.advisors.model.view.TaskResponseWithStatus;
import az.advisors.service.task.TaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("${az.root.task}")
@Api("Endpoint for task creation")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/add-task")
    @ApiOperation("add task for students if you user role is TEACHER")
    public ResponseEntity<?> createTask(@RequestBody TaskRequest taskRequest, HttpServletRequest request) {
        return ResponseEntity.ok(taskService.createTask(taskRequest, request));
    }

    @GetMapping("/get-all")
    @ApiOperation("get all task by token if you user role is STUDENT")
    public List<TaskResponse> getTasks(HttpServletRequest request) {
        return taskService.getTasks(request);
    }

    @PutMapping("/update-status/{taskId}")
    @ApiOperation("update task's status after finishing if you user role is STUDENT")
    public ResponseEntity<?> updateStatusOfTask(@PathVariable("taskId") Long taskId,
                                                HttpServletRequest request) {
        return ResponseEntity.ok(taskService.update(taskId, request));
    }

    @GetMapping("/get-all-task-with-statuses")
    @ApiOperation("get all task with statuses if you user role is TEACHER")
    public List<TaskResponseWithStatus> taskResponseWithStatusList(HttpServletRequest request) {
        return taskService.taskResponseWithStatusList(request);
    }
}
