package com.carrot.presentation.controller;

import com.carrot.application.region.service.RegionService;
import com.carrot.application.user.dto.LoginUser;
import com.carrot.global.common.Response;
import com.carrot.presentation.response.RegionSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/locations")
    public Response<Slice<RegionSearchResponse>> search(@RequestParam String state,
                                                        Pageable pageable,
                                                        @AuthenticationPrincipal LoginUser loginUser) {
        Slice<RegionSearchResponse> response = regionService.search(state, pageable);
        return Response.success(response);
    }
}
