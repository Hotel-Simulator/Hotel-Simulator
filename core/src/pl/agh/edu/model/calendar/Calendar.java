package pl.agh.edu.model.calendar;

import org.json.simple.parser.ParseException;
import pl.agh.edu.generator.client_generator.JSONExtractor;
import pl.agh.edu.model.Time;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: 07.06.2023 ustalić jaki zakres ma kalendarz
public class Calendar {
    private static Calendar instance;
    private final Map<LocalDate, List<CalendarEvent>> weeks;

    private Calendar() throws IOException, ParseException {
        LocalDate gameStartDate = JSONExtractor.getDate("game_start_date");
        LocalDate gameEndDate = JSONExtractor.getDate("game_end_date");

        LocalDate mondayDate = getFirstDayOfWeekDate(gameStartDate);
        weeks = Stream.iterate(mondayDate, date -> date.plusDays(7))
                .limit(ChronoUnit.DAYS.between(gameStartDate,gameEndDate.plusYears(1)) / 7)
                .collect(Collectors.toMap(
                        d -> d,
                        d -> new ArrayList<>(),
                        (a,b) -> b,
                        HashMap::new
                ));
    }
    public static Calendar getInstance() throws IOException, ParseException {
        if(instance == null) instance = new Calendar();
        return instance;
    }
    private LocalDate getFirstDayOfWeekDate(LocalDate date){
        return date.minusDays(date.getDayOfWeek().ordinal());
    }

    public List<CalendarEvent> getEventsForWeek(LocalDate date){
        return weeks.getOrDefault(getFirstDayOfWeekDate(date),new ArrayList<>());
    }
    public void addEvent(CalendarEvent calendarEvent){
        weeks.get(getFirstDayOfWeekDate(calendarEvent.date())).add(calendarEvent);
        weeks.get(getFirstDayOfWeekDate(calendarEvent.date())).sort(CalendarEvent::compareTo);
    }

    public static void main(String[] args) throws IOException, ParseException {
        Calendar calendar = getInstance();
        calendar.addEvent(new CalendarEvent(Time.getInstance().getTime().toLocalDate(),"heji","essa"));
        System.out.println(calendar.getEventsForWeek(Time.getInstance().getTime().toLocalDate()));
    }



}
