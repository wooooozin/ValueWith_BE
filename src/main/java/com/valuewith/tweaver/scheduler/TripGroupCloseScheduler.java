package com.valuewith.tweaver.scheduler;

import com.valuewith.tweaver.constants.GroupStatus;
import com.valuewith.tweaver.group.entity.TripGroup;
import com.valuewith.tweaver.group.repository.TripGroupRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TripGroupCloseScheduler {

    private final TripGroupRepository tripGroupRepository;

    @Transactional
    @Scheduled(cron = "00 00 00 * * *", zone = "Asia/Seoul")
    public void updateTripGroupStatusToClose() {
        tripGroupRepository.updateTripGroupStatusToClose();
    }
}
