package com.valuewith.tweaver.group.controller;

import com.valuewith.tweaver.commons.security.TokenService;
import com.valuewith.tweaver.group.dto.TripGroupDetailResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupListResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupStatusListDto;
import com.valuewith.tweaver.group.dto.TripGroupStatusResponseDto;
import com.valuewith.tweaver.group.service.TripGroupListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/groups/*")
public class TripGroupListController {

    private final TripGroupListService tripGroupListService;
    private final TokenService tokenService;

    @GetMapping("/list")
    public ResponseEntity<TripGroupListResponseDto> getTripGroupsList(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "all") String status,
        @RequestParam(defaultValue = "all") String area,
        @RequestParam(defaultValue = "latest") String sort,
        @RequestParam(defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(page, 12, sortDirection(sort));
        TripGroupListResponseDto tripGroupListResponseDto = tripGroupListService.getFilteredTripGroups(
            status, area, title, pageable
        );
        return ResponseEntity.ok(tripGroupListResponseDto);
    }

    @GetMapping("/list/{tripGroupId}")
    public ResponseEntity<TripGroupDetailResponseDto> getTripGroupDetail(
        @PathVariable Long tripGroupId
    ) {
        TripGroupDetailResponseDto tripGroupDetailDto = tripGroupListService.getTripGroupDetail(tripGroupId);
        return ResponseEntity.ok(tripGroupDetailDto);
    }

    @GetMapping("/list/my-list")
    public ResponseEntity<TripGroupStatusListDto> getMyTripGroupList(
        @RequestHeader("Authorization") String token,
        @RequestParam(required = false) String status
    ) {
        String memberEmail = tokenService.getMemberEmail(token);
        log.info("😀" + memberEmail);
        List<TripGroupStatusResponseDto> myTripGroupList = tripGroupListService.getMyTripGroupList(memberEmail, status);
        TripGroupStatusListDto tripGroupStatusListDto = TripGroupStatusListDto.from(myTripGroupList);
        return ResponseEntity.ok(tripGroupStatusListDto);
    }

    private Sort sortDirection(String sort) {
        if ("deadline".equals(sort)) {
            return Sort.by("dueDate").ascending();
        } else {
            return Sort.by("createdDateTime").descending();
        }
    }
}
