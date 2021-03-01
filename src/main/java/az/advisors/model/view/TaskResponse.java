package az.advisors.model.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ApiModel("task response for get assigned tasks")
@ToString
public class TaskResponse {

    @ApiModelProperty("task's content")
    private String content;

    @ApiModelProperty("dead line for task,date pattern mus be 'dd.MM.yyyy' ")
    private String deadLine;

    @ApiModelProperty("username of assigned user")
    private String assignedUserFullName;

    @ApiModelProperty("rank of task,by default rank of each task is 10")
    private int rank;

}
