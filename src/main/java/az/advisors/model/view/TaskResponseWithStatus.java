package az.advisors.model.view;

import az.advisors.model.enums.StatusName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TaskResponseWithStatus {
    @ApiModelProperty("task's content")
    private String content;

    @ApiModelProperty("dead line for task,date pattern mus be 'dd.MM.yyyy' ")
    private String deadLine;

    @ApiModelProperty("username of assigned user")
    private String assignedUserFullName;

    @ApiModelProperty("rank of task,by default rank of each task is 10")
    private int rank;

    @ApiModelProperty("status of task bu default task hasn't any status")
    private StatusName statusName;
}
