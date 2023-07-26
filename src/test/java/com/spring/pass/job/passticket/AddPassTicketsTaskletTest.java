package com.spring.pass.job.passticket;

import com.spring.pass.domain.BulkPassTicket;
import com.spring.pass.domain.PassTicket;
import com.spring.pass.domain.UserAccount;
import com.spring.pass.domain.UserGroup;
import com.spring.pass.domain.constant.BulkPassTicketStatus;
import com.spring.pass.domain.constant.PassTicketStatus;
import com.spring.pass.domain.constant.UserAccountStatus;
import com.spring.pass.repository.BulkPassTicketRepository;
import com.spring.pass.repository.PassTicketRepository;
import com.spring.pass.repository.UserAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AddPassTicketsTaskletTest {
    @InjectMocks private AddPassTicketsTasklet addPassTicketsTasklet;
    @Mock private StepContribution stepContribution;
    @Mock private ChunkContext chunkContext;
    @Mock private PassTicketRepository passTicketRepository;
    @Mock private BulkPassTicketRepository bulkPassTicketRepository;
    @Mock private UserAccountRepository userAccountRepository;

    @Test
    void execute() throws Exception {
        // Given
        BulkPassTicket bulkPassTicket = createBulkPassTicket();
        UserAccount userAccount = createUserAccount();

        given(bulkPassTicketRepository.findByStatusAndStartedAtGreaterThan(
                eq(BulkPassTicketStatus.READY),
                any()
        )).willReturn(List.of(bulkPassTicket));
        given(userAccountRepository.findByUserGroup_UserGroupId(eq("GROUP")))
                .willReturn(List.of(userAccount));
        // When
        RepeatStatus repeatStatus = addPassTicketsTasklet.execute(stepContribution, chunkContext);
        // Then
        assertThat(repeatStatus).isEqualTo(RepeatStatus.FINISHED);
        then(bulkPassTicketRepository).should().findByStatusAndStartedAtGreaterThan(eq(BulkPassTicketStatus.READY), any());
        then(userAccountRepository).should().findByUserGroup_UserGroupId(eq("GROUP"));

        ArgumentCaptor<List> passTicketsCaptor = ArgumentCaptor.forClass(List.class);
        verify(passTicketRepository, times(1)).saveAll(passTicketsCaptor.capture());
        List<PassTicket> passTickets = passTicketsCaptor.getValue();
        assertThat(passTickets.size()).isEqualTo(1);
        PassTicket passTicket = passTickets.get(0);
        assertThat(passTicket)
                .hasFieldOrPropertyWithValue("packageId", bulkPassTicket.getPackageId())
                .hasFieldOrPropertyWithValue("userId", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("status", PassTicketStatus.READY)
                .hasFieldOrPropertyWithValue("remainingCount", bulkPassTicket.getCount())
                .hasFieldOrPropertyWithValue("startedAt", bulkPassTicket.getStartedAt())
                .hasFieldOrPropertyWithValue("endedAt", bulkPassTicket.getEndedAt());
    }

    private PassTicket createPassTicket(BulkPassTicket bulkPassTicket) {
        PassTicket passTicket = PassTicket.renewal(
                bulkPassTicket,
                "A1000000"
        );
        ReflectionTestUtils.setField(passTicket, "id", 10L);

        return passTicket;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "A1000000",
                "김주영",
                createUserGroup(),
                UserAccountStatus.ACTIVE,
                "01012341234",
                null
        );
    }

    private UserGroup createUserGroup() {
        return UserGroup.of(
                "GROUP",
                "그룹"
        );
    }

    private BulkPassTicket createBulkPassTicket() {
        return BulkPassTicket.of(
                1L,
                "GROUP",
                BulkPassTicketStatus.READY,
                10,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(61)
        );
    }
}
