package az.advisors.model.view;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("task request for add task")
@ToString
public class TaskRequest {

    @ApiModelProperty("task's content")
    private String content;

    @ApiModelProperty("dead line for task,date pattern mus be 'dd.MM.yyyy' ")
    private String deadLine;

    @ApiModelProperty("username of assigned user")
    private String assignedUsernameOfUser;


}
