package az.advisors.controller;

import az.advisors.model.view.UserRequest;
import az.advisors.service.user.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("${az.root.user}")
@Api("Endpoint for user creation")
public class UserController {

    private final UserService userService;

    @PostMapping("/add")
    @ApiOperation("add user")
    public ResponseEntity<?> create(@RequestBody UserRequest userRequest, HttpServletRequest request) {
        return ResponseEntity.ok(userService.create(userRequest, request));
    }
}
