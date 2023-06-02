package pl.agh.edu.model.event.temporary;

import java.time.LocalDate;

public abstract class TemporaryEvent {
    protected final LocalDate startDate;
    protected final LocalDate endDate;

    protected final String message;

    protected TemporaryEvent(LocalDate startDate, LocalDate endDate, String message) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.message = message;
    }


    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "TemporaryEvent{" +
                "startDate=" + startDate +
                ", endDate=" + endDate + '\'' +
                '}';
    }
}
