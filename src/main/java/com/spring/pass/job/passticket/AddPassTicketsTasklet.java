package com.spring.pass.job.passticket;

import com.spring.pass.domain.BulkPassTicket;
import com.spring.pass.domain.PassTicket;
import com.spring.pass.domain.UserAccount;
import com.spring.pass.domain.constant.BulkPassTicketStatus;
import com.spring.pass.repository.BulkPassTicketRepository;
import com.spring.pass.repository.PassTicketRepository;
import com.spring.pass.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AddPassTicketsTasklet implements Tasklet {
    private final PassTicketRepository passTicketRepository;
    private final BulkPassTicketRepository bulkPassTicketRepository;
    private final UserAccountRepository userAccountRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<BulkPassTicket> bulkPassTickets = getBulkPassTickets1DayAgo();

        int count = 0;
        for (BulkPassTicket bulkPassTicket : bulkPassTickets) {
            //user_group에 속한 userId 조회
            List<String> userIds = userAccountRepository.findByUserGroup_UserGroupId(bulkPassTicket.getUserGroupId()).stream()
                    .map(UserAccount::getUserId)
                    .toList();
            //각 userId로 이용권 추가
            count += addPassTickets(bulkPassTicket, userIds);
            //status를 COMPLETED로 변경
            bulkPassTicket.changeStatus(BulkPassTicketStatus.COMPLETED);
        }
        log.info("AddPassesTasklet - execute: 이용권 {}건 추가 완료", count);
        return RepeatStatus.FINISHED;
    }

    /**
     * 이용권 시작 일시 1일 전 user_group 내 각 사용자에게 이용권을 추가
     */
    private List<BulkPassTicket> getBulkPassTickets1DayAgo() {
        LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        return bulkPassTicketRepository.findByStatusAndStartedAtGreaterThan(
                BulkPassTicketStatus.READY,
                startedAt
        );
    }

    private int addPassTickets(BulkPassTicket bulkPassTicket, List<String> userIds) {
        List<PassTicket> passTickets = new ArrayList<>();
        for (String userId : userIds) {
            PassTicket passTicket = PassTicket.renewal(bulkPassTicket, userId);
            passTickets.add(passTicket);
        }
        return passTicketRepository.saveAll(passTickets).size();
    }
}
