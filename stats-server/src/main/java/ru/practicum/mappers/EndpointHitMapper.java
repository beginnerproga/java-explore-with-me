package ru.practicum.mappers;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.models.EndpointHit;

public class EndpointHitMapper {
    public static EndpointHit toEndpointHit(EndpointHitDto endpointHitDto) {
        return new EndpointHit(null, endpointHitDto.getApp(), endpointHitDto.getUri(), endpointHitDto.getIp(), endpointHitDto.getTimestamp());
    }

    public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
        return new EndpointHitDto(endpointHit.getApp(), endpointHit.getUri(), endpointHit.getIp(), endpointHit.getTimestamp());
    }
}
