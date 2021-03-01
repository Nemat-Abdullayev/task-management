package az.advisors.model.view;

import az.advisors.model.enums.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel("user model for add user request")
public class UserRequest {

    @ApiModelProperty("username of user")
    private String username;

    @ApiModelProperty("name of user")
    private String name;

    @ApiModelProperty("surname of user")
    private String surname;

    @ApiModelProperty("age of user")
    private int age;

    @ApiModelProperty("role name for user")
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @ApiModelProperty("password for user")
    private String password;

}
