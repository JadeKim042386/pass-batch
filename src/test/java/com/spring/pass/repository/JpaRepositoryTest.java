package com.spring.pass.repository;

import com.spring.pass.domain.Booking;
import com.spring.pass.domain.Package;
import com.spring.pass.domain.PassTicket;
import com.spring.pass.domain.UserAccount;
import com.spring.pass.domain.constant.BookingStatus;
import com.spring.pass.domain.constant.PassStatus;
import com.spring.pass.domain.constant.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@DisplayName("JPA Repository 테스트")
class JpaRepositoryTest {

    @DisplayName("Booking Jpa 테스트")
    @Import(JpaRepositoryTest.TestJpaConfig.class)
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
                    1L,
                    "A1000000",
                    BookingStatus.IN_PROGRESS,
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
    @Import(JpaRepositoryTest.TestJpaConfig.class)
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
    @Import(JpaRepositoryTest.TestJpaConfig.class)
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
                    PassStatus.IN_PROGRESS,
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
    @Import(JpaRepositoryTest.TestJpaConfig.class)
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
        void getUserAccount() {
            // Given

            // When
            List<UserAccount> userAccounts = userAccountRepository.findAll();
            // Then
            assertThat(userAccounts).isNotNull().hasSize(7);
        }

        @DisplayName("사용자 정보 추가")
        @Test
        void addUserAccount() {
            // Given
            long previousCount = userAccountRepository.count();
            UserAccount userAccount = UserAccount.of(
                    "C1000000",
                    "김주영",
                    UserStatus.ACTIVE,
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

    @EnableJpaAuditing
    @TestConfiguration
    public static class TestJpaConfig {
    }
}
