package com.carrot.presentation.controller;

import com.carrot.application.user.dto.LoginUser;
import com.carrot.application.user.service.UserReadService;
import com.carrot.application.user.service.UserWriteService;
import com.carrot.global.common.Response;
import com.carrot.global.error.CarrotRuntimeException;
import com.carrot.global.error.ErrorCode;
import com.carrot.infrastructure.util.ClassUtils;
import com.carrot.presentation.request.UserRegionRequest;
import com.carrot.presentation.response.UserRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserWriteService userWriteService;
    private final UserReadService userReadService;

    @GetMapping("/location")
    public Response<UserRegionResponse> findRegion(Authentication authentication){
        LoginUser loginUser = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), LoginUser.class)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR));

        UserRegionResponse response = userReadService.findRegion(loginUser.getId());
        return Response.success(response);
    }

    @PostMapping("/location")
    public Response<Void> saveRegion(@RequestBody UserRegionRequest request,
                                         Authentication authentication){
        LoginUser loginUser = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), LoginUser.class)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR));

        userWriteService.saveRegion(loginUser.getId(), request.getRegionId());
        return Response.success();
    }

    @DeleteMapping("/location")
    public Response<Void> deleteRegion(@RequestBody UserRegionRequest request,
                                         Authentication authentication){
        LoginUser loginUser = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), LoginUser.class)
                .orElseThrow(() -> new CarrotRuntimeException(ErrorCode.INTERNAL_SERVER_ERROR));

        userWriteService.deleteRegion(loginUser.getId(), request.getRegionId());
        return Response.success();
    }

}
