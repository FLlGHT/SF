package com.flight.sf.common;

import com.google.api.services.calendar.model.Event;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FLIGHT
 * @creationDate 13.08.2022
 */

@Component
public class Mapper {

    public EventDTO toEventDTO(Event event) {
        EventDTO dto = new EventDTO();
        dto.setSummary(event.getSummary());
        dto.setStart(event.getStart().toString());
        dto.setEnd(event.getEnd().toString());

        return dto;
    }

    public List<EventDTO> toEventsDTO(List<Event> events) {
        List<EventDTO> list = new ArrayList<>();
        events.forEach(event -> list.add(toEventDTO(event)));
        return list;
    }
}
