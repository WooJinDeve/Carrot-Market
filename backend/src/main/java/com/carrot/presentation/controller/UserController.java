package com.carrot.presentation.controller;

import com.carrot.application.user.dto.LoginUser;
import com.carrot.application.user.service.UserReadService;
import com.carrot.application.user.service.UserWriteService;
import com.carrot.global.common.Response;
import com.carrot.presentation.request.UserRegionRequest;
import com.carrot.presentation.response.UserRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserWriteService userWriteService;
    private final UserReadService userReadService;

    @GetMapping("/location")
    public Response<List<UserRegionResponse>> findRegion(@AuthenticationPrincipal LoginUser loginUser) {
        List<UserRegionResponse> response = userReadService.findRegion(loginUser.getId());
        return Response.success(response);
    }

    @PostMapping("/location")
    public Response<Void> saveRegion(@RequestBody UserRegionRequest request,
                                     @AuthenticationPrincipal LoginUser loginUser) {
        userWriteService.saveRegion(loginUser.getId(), request.getRegionId());
        return Response.success();
    }

    @DeleteMapping("/location")
    public Response<Void> deleteRegion(@RequestBody UserRegionRequest request,
                                       @AuthenticationPrincipal LoginUser loginUser) {
        userWriteService.deleteRegion(loginUser.getId(), request.getRegionId());
        return Response.success();
    }
}
