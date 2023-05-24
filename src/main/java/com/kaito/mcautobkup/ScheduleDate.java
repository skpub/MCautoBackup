package com.kaito.mcautobkup;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
    It is almost identical to the specification of crontab.
    HOWEVER, EACH WEEK BEGINS ON MONDAY, BUT NOT SUNDAY.
    THEREFORE, a zero VALUE for day_of_week MEANS MONDAY, BUT NOT SUNDAY.
*/
public class ScheduleDate {
    private Optional<SortedSet<Byte>> min;
    private Optional<SortedSet<Byte>> hour;

    // a zero value for the day variable means the end of each month.
    private Optional<SortedSet<Byte>> day;
    private Optional<SortedSet<Byte>> month;
    private Optional<SortedSet<Byte>> day_of_week;

    ScheduleDate(String str) throws IllegalArgumentException {
        read(str);
    }

    public void read(String str) throws IllegalArgumentException {
        List<String> settings = new ArrayList<>(Arrays.asList(str.split(" ")));
        if (settings.size() != 5) {
            throw new IllegalArgumentException("Expected 5 elements when divided by space, but "
                + settings.size() + " elements were passed.");
        }

        if (!validate()) {
            throw new IllegalArgumentException(str + "were passed, but illegal format.");
        }
    }
    public void read_each_member(Optional<SortedSet<Byte>> container, String elements) {
        if (Objects.equals(elements, "*")) {
            container = Optional.empty();
        }
        String[] slots = elements.split(",");
    }

    public String show() {
        StringBuilder temp = new StringBuilder();
        min.ifPresentOrElse(
            slots -> {
                for (Byte slot: slots.headSet(slots.last())) temp.append(slot).append(",");
                temp.append(slots.last());
            },
            () -> {
                temp.append("*");
            }
        );
        temp.append(" ");

        hour.ifPresentOrElse(
            slots -> {
                for (Byte slot: slots.headSet(slots.last())) temp.append(slot).append(",");
                temp.append(slots.last());
            },
            () -> {
                temp.append("*");
            }
        );
        temp.append(" ");

        day.ifPresentOrElse(
            slots -> {
                for (Byte slot: slots.headSet(slots.last())) temp.append(slot).append(",");
                temp.append(slots.last());
            },
            () -> {
                temp.append("*");
            }
        );
        temp.append(" ");

        month.ifPresentOrElse(
            slots -> {
                for (Byte slot: slots.headSet(slots.last())) temp.append(slot).append(",");
                temp.append(slots.last());
            },
            () -> {
                temp.append("*");
            }
        );
        temp.append(" ");

        day_of_week.ifPresentOrElse(
            slots -> {
                for (Byte slot: slots.headSet(slots.last())) temp.append(slot).append(",");
                temp.append(slots.last());
            },
            () -> {
                temp.append("*");
            }
        );

        return temp.toString();
    }

    private boolean validate() {
        if (min.isPresent()) {
            for (Byte slot: min.get())
                if (! (Integer.valueOf(slot) >= 0
                    && Integer.valueOf(slot) <= 60))
                    return false;
        }
        if (hour.isPresent()) {
            for (Byte slot: hour.get())
                if (! (Integer.valueOf(slot) >= 0
                    && Integer.valueOf(slot) <= 23))
                    return false;
        }
        if (day.isPresent()) {
            for (Byte slot: day.get())
                if (! (Integer.valueOf(slot) >= 1
                    && Integer.valueOf(slot) <= 31
                    || Integer.valueOf(slot) == -1))
                    return false;
        }
        if (month.isPresent()) {
            for (Byte slot: month.get())
                if (! (Integer.valueOf(slot) >= 1
                    && Integer.valueOf(slot) <= 12))
                    return false;
        }
        if (day_of_week.isPresent()) {
            for (Byte slot: day_of_week.get())
                if (! (Integer.valueOf(slot) >= 0
                    && Integer.valueOf(slot) <= 6))
                    return false;
        }
        return true;
    }
}
