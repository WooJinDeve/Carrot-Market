package com.carrot.application.region.service;

import com.carrot.application.region.repository.RegionRepository;
import com.carrot.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("[Business] RegionService")
public class RegionServiceTest extends ServiceTest {

    @InjectMocks
    private RegionService regionService;
    @Mock
    private RegionRepository regionRepository;

    @DisplayName("[Success] 행정동 기반 지역 검색 요청")
    @Test
    void 행정동_기반_지역_검색_요청() {
        //given
        String state = "노은";
        Pageable pageable = mock(Pageable.class);

        when(regionRepository.findByNameContaining(state, pageable)).thenReturn(Page.empty());

        assertDoesNotThrow(() -> regionService.search(state, pageable));
    }
}