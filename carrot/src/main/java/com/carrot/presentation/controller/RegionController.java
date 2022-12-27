package com.carrot.presentation.controller;

import com.carrot.application.region.service.RegionService;
import com.carrot.global.common.Response;
import com.carrot.presentation.response.RegionSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class RegionController {
    private final RegionService regionService;

    @GetMapping("/locations")
    public Response<List<RegionSearchResponse>> search(@RequestParam String state, Authentication authentication){
        List<RegionSearchResponse> response = regionService.search(state)
                .stream().map(RegionSearchResponse::fromRegionDto)
                .collect(Collectors.toList());
        return Response.success(response);
    }
}
