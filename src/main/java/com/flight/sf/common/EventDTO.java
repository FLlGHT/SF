package com.flight.sf.common;

/**
 * @author FLIGHT
 * @creationDate 13.08.2022
 */
public class EventDTO {

    private String summary;
    private String start;
    private String end;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
