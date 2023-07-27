package com.spring.pass.repository;

import com.spring.pass.config.TestJpaConfig;
import com.spring.pass.domain.Package;
import com.spring.pass.domain.*;
import com.spring.pass.domain.constant.BookingStatus;
import com.spring.pass.domain.constant.PassTicketStatus;
import com.spring.pass.domain.constant.UserAccountStatus;
import io.hypersistence.utils.hibernate.type.json.internal.JacksonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DisplayName("JPA Repository 테스트")
class JpaRepositoryTest {

    @DisplayName("Booking Jpa 테스트")
    @Import(TestJpaConfig.class)
    @DataJpaTest
    @Nested
    class BookingJpaTest {
        private final BookingRepository bookingRepository;

        @Autowired
        BookingJpaTest(BookingRepository bookingRepository) {
            this.bookingRepository = bookingRepository;
        }

        @DisplayName("예약 정보 조회")
        @Test
        void getBooking() {
            // Given

            // When
            List<Booking> bookings = bookingRepository.findAll();
            // Then
            assertThat(bookings).isNotNull().hasSize(1);
        }

        @DisplayName("예약 정보 추가")
        @Test
        void addBooking() {
            // Given
            long previousCount = bookingRepository.count();
            Booking booking = Booking.of(
                    createPassTicket(),
                    createUserAccount(),
                    BookingStatus.PROGRESSED,
                    true,
                    true,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(5),
                    null
            );
            // When
            bookingRepository.save(booking);
            // Then
            assertThat(bookingRepository.count()).isEqualTo(previousCount + 1);
        }



        @DisplayName("예약 정보 삭제")
        @Test
        void updateBooking() {
            // Given
            long previousCount = bookingRepository.count();
            Long bookingId = 1L;
            // When
            bookingRepository.deleteById(bookingId);
            // Then
            assertThat(bookingRepository.count()).isEqualTo(previousCount - 1);
        }
    }

    @DisplayName("Package Jpa 테스트")
    @Import(TestJpaConfig.class)
    @DataJpaTest
    @Nested
    class PackageJpaTest {
        private final PackageRepository packageRepository;

        @Autowired
        PackageJpaTest(PackageRepository packageRepository) {
            this.packageRepository = packageRepository;
        }

        @DisplayName("패키지 정보 조회")
        @Test
        void getPackage() {
            // Given

            // When
            List<Package> packages = packageRepository.findAll();
            // Then
            assertThat(packages).isNotNull().hasSize(7);
        }

        @DisplayName("패키지 정보 추가")
        @Test
        void addPackage() {
            // Given
            long previousCount = packageRepository.count();
            Package aPackage = Package.of(
                    "Starter PT 40회",
                    40,
                    240
            );
            // When
            packageRepository.save(aPackage);
            // Then
            assertThat(packageRepository.count()).isEqualTo(previousCount + 1);
        }

        @DisplayName("패키지 정보 삭제")
        @Test
        void updatePackage() {
            // Given
            long previousCount = packageRepository.count();
            Long packageId = 1L;
            // When
            packageRepository.deleteById(packageId);
            // Then
            assertThat(packageRepository.count()).isEqualTo(previousCount - 1);
        }
    }

    @DisplayName("PassTicket Jpa 테스트")
    @Import(TestJpaConfig.class)
    @DataJpaTest
    @Nested
    class PassTicketJpaTest {
        private final PassTicketRepository passTicketRepository;

        @Autowired
        PassTicketJpaTest(PassTicketRepository passTicketRepository) {
            this.passTicketRepository = passTicketRepository;
        }

        @DisplayName("이용권 정보 조회")
        @Test
        void getPassTicket() {
            // Given

            // When
            List<PassTicket> passTickets = passTicketRepository.findAll();
            // Then
            assertThat(passTickets).isNotNull().hasSize(1);
        }

        @DisplayName("이용권 정보 추가")
        @Test
        void addPassTicket() {
            // Given
            long previousCount = passTicketRepository.count();
            PassTicket passTicket = PassTicket.of(
                    3L,
                    "A1000002",
                    PassTicketStatus.PROGRESSED,
                    20,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusDays(1),
                    null
            );
            // When
            passTicketRepository.save(passTicket);
            // Then
            assertThat(passTicketRepository.count()).isEqualTo(previousCount + 1);
        }

        @DisplayName("이용권 정보 삭제")
        @Test
        void updatePassTicket() {
            // Given
            long previousCount = passTicketRepository.count();
            Long passTicketId = 1L;
            // When
            passTicketRepository.deleteById(passTicketId);
            // Then
            assertThat(passTicketRepository.count()).isEqualTo(previousCount - 1);
        }
    }

    @DisplayName("UserAccount Jpa 테스트")
    @Import(TestJpaConfig.class)
    @DataJpaTest
    @Nested
    class UserAccountJpaTest {
        private final UserAccountRepository userAccountRepository;

        @Autowired
        UserAccountJpaTest(UserAccountRepository userAccountRepository) {
            this.userAccountRepository = userAccountRepository;
        }

        @DisplayName("사용자 정보 조회")
        @Test
        void getUserAccounts() {
            // Given

            // When
            List<UserAccount> userAccounts = userAccountRepository.findAll();
            // Then
            assertThat(userAccounts).isNotNull().hasSize(7);
        }

        @DisplayName("사용자 조회")
        @Test
        void getUserAccount() {
            // Given
            String UserId = "A1000000";
            // When
            Optional<UserAccount> userAccount = userAccountRepository.findById(UserId);
            // Then
            assertThat(userAccount).get()
                    .hasFieldOrPropertyWithValue("userGroup.userGroupId", "HANBADA");
        }

        @DisplayName("사용자 정보 추가")
        @Test
        void addUserAccount() {
            // Given
            long previousCount = userAccountRepository.count();
            UserAccount userAccount = UserAccount.of(
                    "C1000000",
                    "김주영",
                    createUserGroup("TAESAN", "태산"),
                    UserAccountStatus.ACTIVE,
                    "01012341234",
                    null
            );
            // When
            userAccountRepository.save(userAccount);
            // Then
            assertThat(userAccountRepository.count()).isEqualTo(previousCount + 1);
        }

        @DisplayName("사용자 정보 삭제")
        @Test
        void updateUserAccount() {
            // Given
            long previousCount = userAccountRepository.count();
            String userAccountId = "A1000000";
            UserAccount userAccount = userAccountRepository.getReferenceById(userAccountId);
            // When
            userAccountRepository.delete(userAccount);
            // Then
            assertThat(userAccountRepository.count()).isEqualTo(previousCount - 1);
        }
    }

    @DisplayName("UserGroup Jpa 테스트")
    @Import(TestJpaConfig.class)
    @DataJpaTest
    @Nested
    class UserGroupJpaTest {
        private final UserGroupRepository userGroupRepository;

        @Autowired
        UserGroupJpaTest(UserGroupRepository userGroupRepository) {
            this.userGroupRepository = userGroupRepository;
        }

        @DisplayName("사용자 그룹 조회")
        @Test
        void getUserGroup() {
            // Given
            String userGroupId = "HANBADA";
            // When
            Optional<UserGroup> userGroup = userGroupRepository.findById(userGroupId);
            // Then
            assertThat(userGroup).get()
                    .hasFieldOrPropertyWithValue("userGroupId", "HANBADA");
        }

        @DisplayName("사용자 그룹 추가")
        @Test
        void addUserGroup() {
            // Given
            long previousCount = userGroupRepository.count();
            System.out.println("previousCount = " + previousCount);
            UserGroup userGroup = createUserGroup("GROUP", "그룹");
            // When
            userGroupRepository.save(userGroup);
            // Then
            assertThat(userGroupRepository.count()).isEqualTo(previousCount + 1);
        }

        @DisplayName("사용자 그룹 삭제")
        @Test
        void updateUserGroup() {
            // Given
            long previousCount = userGroupRepository.count();
            String userGroupId = "HANBADA";
            UserGroup userGroup = userGroupRepository.getReferenceById(userGroupId);
            // When
            userGroupRepository.delete(userGroup);
            // Then
            assertThat(userGroupRepository.count()).isEqualTo(previousCount - 1);
        }
    }

    private PassTicket createPassTicket() {
        PassTicket passTicket = PassTicket.of(
                1L,
                "A1000000",
                PassTicketStatus.PROGRESSED,
                100,
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(10)
        );
        ReflectionTestUtils.setField(passTicket, "id", 1L);
        return passTicket;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "A1000000",
                "김주영",
                    createUserGroup("GROUP", "그룹"),
                    UserAccountStatus.ACTIVE,
                    "01012341234",
                JacksonUtil.toJsonNode("""
                        {
                            "uuid": "1234asdf"
                        }
                        """)
                );
    }

    private UserGroup createUserGroup(String userGroupId, String userGroupName) {
        return UserGroup.of(
                userGroupId,
                userGroupName,
                userGroupName
        );
    }
}
