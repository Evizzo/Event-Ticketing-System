package com.eventticketingsystem.eventticketingsystem.config;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ScheduledTasksTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private ScheduledTasks scheduledTasks;

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

        scheduledTasks.updateEventsStatusAutomaticlly();

        verify(eventRepository).save(any(Event.class));

        assertTrue(event.isDone());
    }
}