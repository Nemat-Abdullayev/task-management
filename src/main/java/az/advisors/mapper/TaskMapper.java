package az.advisors.mapper;

import az.advisors.model.entity.Status;
import az.advisors.model.entity.Task;
import az.advisors.model.enums.StatusName;
import az.advisors.model.view.TaskResponse;
import az.advisors.model.view.TaskResponseWithStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TaskMapper {

    public static final TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    @Mappings({
            @Mapping(target = "content", source = "task.content"),
            @Mapping(target = "deadLine", expression = "java(task.getDeadLine().toString())"),
            @Mapping(target = "assignedUserFullName", expression = "java(getFullNameOfAssignedByUser(task.getAssignedBy().getName(),task.getAssignedBy().getSurname()))"),
            @Mapping(target = "rank", source = "task.rank")
    })

    public abstract TaskResponse mapEntityToView(Task task);

    @Mappings({
            @Mapping(target = "content", source = "task.content"),
            @Mapping(target = "deadLine", expression = "java(task.getDeadLine().toString())"),
            @Mapping(target = "assignedUserFullName", expression = "java(getFullNameOfAssignedByUser(task.getAssignedBy().getName(),task.getAssignedBy().getSurname()))"),
            @Mapping(target = "rank", source = "task.rank"),
            @Mapping(target = "statusName", expression = "java(getRoleName(task.getStatus()))"),
    })


    public abstract TaskResponseWithStatus mapToView(Task task);

    public String getFullNameOfAssignedByUser(String name, String surname) {
        return surname + " " + name;
    }

    public StatusName getRoleName(Status status) {
        return status.getStatusName();
    }

}
