package com.carrot.application.user.service;

import com.carrot.application.region.service.RegionService;
import com.carrot.application.user.domain.UserRegion;
import com.carrot.application.user.dto.UserRequest;
import com.carrot.application.user.repository.UserRegionRepository;
import com.carrot.application.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.carrot.presentation.response.UserResponse.UserRegionResponse;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReadService {

    private static final int DEFAULT_DISTANT = 3;
    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;
    private final RegionService regionService;


    @Cacheable(key = "#id", cacheNames = "MEMBER_REGION")
    public List<UserRegionResponse> getRegion(Long id) {
        return userRegionRepository.findByIdAndFetchRegion(id)
                .stream().map(UserRegionResponse::of)
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id", cacheNames = "MEMBER")
    public UserRequest getUser(Long id) {
        return UserRequest.of(userRepository.getById(id));
    }

    @Cacheable(key = "#id", cacheNames = "MEMBER_REGION_SURROUND")
    public List<Long> findSurroundAreaByUserRegion(final Long id) {
        List<UserRegion> userRegions = userRegionRepository.findByIdAndFetchRegion(id);

        Set<Long> ids = new HashSet<>();
        userRegions.forEach(u -> ids.addAll(regionService
                .findByGeometryRegions(
                        u.getRegion().getLocation().getX(),
                        u.getRegion().getLocation().getY(),
                        DEFAULT_DISTANT)));
        return new ArrayList<>(ids);
    }
}
