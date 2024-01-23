package com.eventticketingsystem.eventticketingsystem.config;

import com.eventticketingsystem.eventticketingsystem.entities.Event;
import com.eventticketingsystem.eventticketingsystem.repositories.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
@AllArgsConstructor
@Component
public class ScheduledTasks {
    private final EventRepository eventRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateEventsStatusAutomatically() {
        List<Event> events = eventRepository.findAll();
        LocalDate endDate = LocalDate.now().plusDays(1);
        System.out.println(endDate);
        for (Event event : events) {
            if (event.getDate().isEqual(endDate) && !event.isDone()) {
                event.setDone(true);
                eventRepository.save(event);
            }
        }
        System.out.println("Executing scheduled task updateEventsStatusAutomatically at midnight");
    }
}

