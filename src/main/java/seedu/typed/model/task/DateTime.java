package seedu.typed.model.task;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * DateTime represents our Date and Time in TaskManager using Java's LocalDateTime
 * It provides other static methods such as getting a new DateTime object one week,
 * one month or one year from now. The time zone and current time is based on User's
 * system default clock.
 *
 * @author YIM CHIA HUI
 *
 */
public class DateTime {

    private static final long LONG_ONE = (long) 1.0;

    private LocalDateTime localDateTime;
    private static Clock defaultClock = Clock.systemDefaultZone();

    public DateTime(LocalDateTime localDateTime) {
        super();
        this.localDateTime = localDateTime;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((localDateTime == null) ? 0 : localDateTime.hashCode());
        return result;
    }

    public boolean equals(DateTime other) {
        return this.localDateTime.equals(other.getLocalDateTime());

    }

    public boolean isBefore(DateTime other) {
        return this.localDateTime.isBefore(other.getLocalDateTime());
    }

    public boolean isAfter(DateTime other) {
        return this.localDateTime.isAfter(other.getLocalDateTime());
    }

    public boolean isToday() {
        LocalDate today = LocalDate.now();
        return this.localDateTime.toLocalDate().equals(today);
    }

    public boolean isNow() {
        LocalTime currentTime = LocalTime.now();
        return this.localDateTime.toLocalTime().equals(currentTime);
    }

    public static DateTime getTomorrow() {
        LocalDateTime nextDay = LocalDateTime.now(defaultClock).plusDays(LONG_ONE);
        return new DateTime(nextDay);
    }
    public static DateTime getNextWeek() {
        LocalDateTime nextWeek = LocalDateTime.now(defaultClock).plusWeeks(LONG_ONE);
        return new DateTime(nextWeek);
    }

    public static DateTime getNextMonth() {
        LocalDateTime nextMonth = LocalDateTime.now(defaultClock).plusMonths(LONG_ONE);
        return new DateTime(nextMonth);
    }

    public static DateTime getNextYear() {
        LocalDateTime nextYear = LocalDateTime.now(defaultClock).plusYears(LONG_ONE);
        return new DateTime(nextYear);
    }

    public DateTime tomorrow() {
        LocalDateTime nextDay = this.localDateTime.plusDays(LONG_ONE);
        return new DateTime(nextDay);
    }
    public DateTime nextWeek() {
        LocalDateTime nextWeek = this.localDateTime.plusWeeks(LONG_ONE);
        return new DateTime(nextWeek);
    }

    public DateTime nextMonth() {
        LocalDateTime nextMonth = this.localDateTime.plusMonths(LONG_ONE);
        return new DateTime(nextMonth);
    }

    public DateTime nextYear() {
        LocalDateTime nextYear = this.localDateTime.plusYears(LONG_ONE);
        return new DateTime(nextYear);
    }

    public static DateTime getDateTime(int year, Month month, int day, int hr, int min) {
        return new DateTime(LocalDateTime.of(year, month, day, hr, min));
    }
    @Override
    public String toString() {
        return localDateTime.toString();
    }

    public String toLocalDateString() {
        return localDateTime.toLocalDate().toString();
    }

    public String toLocalTimeString() {
        return localDateTime.toLocalTime().toString();
    }

}