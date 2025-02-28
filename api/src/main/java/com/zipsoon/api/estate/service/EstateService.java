package com.zipsoon.api.estate.service;

import com.zipsoon.api.estate.dto.EstateDetailResponse;
import com.zipsoon.api.estate.dto.EstateResponse;
import com.zipsoon.api.estate.dto.ViewportRequest;
import com.zipsoon.api.estate.mapper.EstateMapper;
import com.zipsoon.api.exception.custom.ServiceException;
import com.zipsoon.api.exception.model.ErrorCode;
import com.zipsoon.common.domain.EstateSnapshot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.zipsoon.api.exception.model.ErrorCode.ESTATE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EstateService {
    private final EstateMapper estateMapper;
    private static final int MAX_RESULTS_PER_ZOOM = 1000;
    private static final int SRID = 4326;        // WGS84 좌표계 SRID 값

    @Transactional(readOnly = true)
    public List<EstateResponse> findEstatesInViewport(ViewportRequest request) {
        int limit = calculateLimit(request.zoom());
        List<EstateSnapshot> estates = estateMapper.findAllInViewport(request, limit, SRID);

        if (estates.isEmpty()) {
            throw new ServiceException(ErrorCode.ESTATE_NOT_FOUND, "해당 지역에 매물이 없습니다.");
        }

        return estates.stream()
            .map(EstateResponse::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public EstateDetailResponse findEstateDetail(Long id) {
        return estateMapper.findById(id)
                .map(EstateDetailResponse::from)
                .orElseThrow(() -> new ServiceException(ESTATE_NOT_FOUND));
    }

    private int calculateLimit(int zoom) {
        if (zoom <= 8) return 100;
        if (zoom <= 14) return 500;
        return MAX_RESULTS_PER_ZOOM;
    }
}
