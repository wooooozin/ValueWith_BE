package com.valuewith.tweaver.group.controller;

import com.valuewith.tweaver.commons.security.service.TokenService;
import com.valuewith.tweaver.group.dto.TripGroupDetailResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupListResponseDto;
import com.valuewith.tweaver.group.dto.TripGroupStatusListDto;
import com.valuewith.tweaver.group.dto.TripGroupStatusResponseDto;
import com.valuewith.tweaver.group.service.TripGroupListService;
import com.valuewith.tweaver.member.entity.Member;
import com.valuewith.tweaver.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    private final MemberService memberService;

    @ApiOperation(
        value = "메인 여행 그룹을 조회하는 API",
        notes = "입력 정보\n"
            + "area : 지역 필터(지역명 영문, 소문자)\n"
            + "page : 기본값 1\n"
            + "sort : latest(그룹 등록일 최신순) / deadline(모집 마감일 빠른순)\n"
            + "status : all(전체) / open(모집중) / close(마감)\n"
            + "title : 제목 → 검색어 입력"
    )
    @GetMapping("/list")
    public ResponseEntity<TripGroupListResponseDto> getTripGroupsList(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "all") String status,
        @RequestParam(defaultValue = "all") String area,
        @RequestParam(defaultValue = "latest") String sort,
        @RequestParam(defaultValue = "") String title
    ) {
        Pageable pageable = PageRequest.of(page - 1, 12, sortDirection(sort));
        TripGroupListResponseDto tripGroupListResponseDto = tripGroupListService.getFilteredTripGroups(
            status, area, title, pageable
        );
        return ResponseEntity.ok(tripGroupListResponseDto);
    }

    @ApiOperation(value = "여행 상세보기", notes = "여행그룹 ID 전달받아 path에 넣어줍니다.")
    @GetMapping("/list/{tripGroupId}")
    public ResponseEntity<TripGroupDetailResponseDto> getTripGroupDetail(
        @PathVariable Long tripGroupId
    ) {
        TripGroupDetailResponseDto tripGroupDetailDto = tripGroupListService.getTripGroupDetail(tripGroupId);
        return ResponseEntity.ok(tripGroupDetailDto);
    }

    @ApiOperation(value = "나의 여행 그룹 리스트 조회 API",
        notes = "leader : 내가 그룹장인 여행그룹\n"
                + "approved : 승인된 여행그룹\n"
                + "pending : 승인 대기중인 여행그룹")
    @GetMapping("/list/my-list")
    public ResponseEntity<TripGroupStatusListDto> getMyTripGroupList(
        @RequestHeader("Authorization") String token,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") int page
    ) {
        Pageable pageable = PageRequest.of(page - 1, 8);
        String memberEmail = tokenService.getMemberEmail(token);
        log.info("😀" + memberEmail);
        Page<TripGroupStatusResponseDto> myTripGroupList = tripGroupListService.getMyTripGroupList(memberEmail, status, pageable);
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
