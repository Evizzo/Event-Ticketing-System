package com.eventticketingsystem.eventticketingsystem.config;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.entities.PasswordResetToken;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import com.eventticketingsystem.eventticketingsystem.repositories.PasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScheduledTasksTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ScheduledTasks scheduledTasks;
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateEventsStatusAutomatically() {
        LocalDate endDate = LocalDate.now().plusDays(1);

        Event event = new Event();
        event.setDate(endDate);
        event.setDone(false);

        when(eventRepository.findAll()).thenReturn(List.of(event));

        scheduledTasks.updateEventsStatusAutomatically();

        verify(eventRepository).save(any(Event.class));

        assertTrue(event.isDone());
    }

    @Test
    void testDeleteExpiredTokens() {
        LocalDate today = LocalDate.now();
        PasswordResetToken expiredToken1 = new PasswordResetToken();
        expiredToken1.setExpiryDate(today.minusDays(1));

        PasswordResetToken expiredToken2 = new PasswordResetToken();
        expiredToken2.setExpiryDate(today.minusDays(2));

        when(passwordResetTokenRepository.findAll()).thenReturn(List.of(expiredToken1, expiredToken2));

        scheduledTasks.deleteExpiredTokens();

        verify(passwordResetTokenRepository, times(2)).delete(any(PasswordResetToken.class));
    }
}