package com.carrot.presentation.controller;

import com.carrot.application.user.dto.LoginUser;
import com.carrot.application.user.service.UserReadService;
import com.carrot.application.user.service.UserWriteService;
import com.carrot.global.common.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.carrot.presentation.response.UserResponse.UserRegionResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserWriteService userWriteService;
    private final UserReadService userReadService;

    @GetMapping("/location")
    public Response<List<UserRegionResponse>> getRegion(@AuthenticationPrincipal LoginUser loginUser) {
        List<UserRegionResponse> response = userReadService.getRegion(loginUser.getId());
        return Response.success(response);
    }

    @PostMapping("/location/{id}")
    public Response<Void> saveRegion(@PathVariable(name = "id") Long regionId,
                                     @AuthenticationPrincipal LoginUser loginUser) {
        userWriteService.saveRegion(loginUser.getId(), regionId);
        return Response.success();
    }

    @DeleteMapping("/location/{id}")
    public Response<Void> deleteRegion(@PathVariable(name = "id") Long userRegionId,
                                       @AuthenticationPrincipal LoginUser loginUser) {
        userWriteService.deleteRegion(loginUser.getId(), userRegionId);
        return Response.success();
    }
}
