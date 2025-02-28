package com.zipsoon.api.estate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zipsoon.common.domain.EstateSnapshot;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;

import java.math.BigDecimal;

@Slf4j
public record EstateResponse(
    Long id,
    String name,
    EstateSnapshot.EstateType type,
    EstateSnapshot.TradeType tradeType,
    BigDecimal price,
    BigDecimal rentPrice,
    BigDecimal area,
    @JsonProperty("lat") double latitude,
    @JsonProperty("lng") double longitude
) {
    public static EstateResponse from(EstateSnapshot snapshot) {
        log.info("Geometry: {}", snapshot.getLocation());
        Point location = (Point) snapshot.getLocation();
        double latitude = location != null ? location.getY() : 0.0;
        double longitude = location != null ? location.getX() : 0.0;
        log.info("location: {}, latitude: {}, longitude: {}", location, latitude, longitude);
        return new EstateResponse(
            snapshot.getId(),
            snapshot.getEstateName(),
            snapshot.getEstateType(),
            snapshot.getTradeType(),
            snapshot.getPrice(),
            snapshot.getRentPrice(),
            snapshot.getAreaMeter(),
            ((Point) snapshot.getLocation()).getY(),
            ((Point) snapshot.getLocation()).getX()
        );
    }
}
