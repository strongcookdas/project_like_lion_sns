package com.example.project_travel_sns.controller;

import com.example.project_travel_sns.domain.dto.Response;
import com.example.project_travel_sns.domain.dto.alarm.AlarmResponse;
import com.example.project_travel_sns.service.AlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/api/v1/alarms")
@RequiredArgsConstructor
@Api(tags = "Alarm API")
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping
    @ApiOperation(value = "알람 목록 조회")
    public ResponseEntity<Response> getAlarms(@ApiIgnore @PageableDefault(size = 20) @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable, @ApiIgnore Authentication authentication) {
        String userName = authentication.getName();
        Page<AlarmResponse> alarmResponses = alarmService.getAlarms(pageable, userName);
        return ResponseEntity.ok().body(Response.of("SUCCESS", alarmResponses));
    }
}
