package com.killarney.todolist.util;

import com.killarney.todolist.models.Day;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility class to turn calendar or related objects to string and/or back
 * Note that returned calendars are all created via Calendar.getInstance()
 *
 * Created by Anthony on 7/26/2016.
 */
public final class CalendarParser {

    //prevent instantiation
    private CalendarParser(){}

    /**
     * @return parsed calendar as a string in format yyyy/mm/dd/hh/mi without /'s
     */
    public static String parseCalendar(Calendar calendar){
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = appendZeroToShortTime(Integer.toString(calendar.get(Calendar.MONTH)));
        String day = appendZeroToShortTime(Integer.toString(calendar.get(Calendar.DATE)));
        String hour = appendZeroToShortTime(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
        String minute = appendZeroToShortTime(Integer.toString(calendar.get(Calendar.MINUTE)));

        return(year+month+day+hour+minute);
    }

    /**
     * @return parsed calendar as a string in format hh/mi without /'s
     */
    public static String parseTime(Calendar calendar){
        String hour = appendZeroToShortTime(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)));
        String minute = appendZeroToShortTime(Integer.toString(calendar.get(Calendar.MINUTE)));

        return(hour+minute);
    }

    /**
     * @return parsed set of days as a string in format s/m/t/w/th/f/sa without /'s
     */
    public static String parseDays(Set<Day> days){
        char[] c = "0000000".toCharArray();
        for (Day d : days) {
            c[d.toInt()] = '1';
        }
        return(new String(c));
    }

    /**
     * @return parsed day as a string
     */
    public static String parseDay(Day day){
        return Integer.toString(day.toInt());
    }

    /**
     *
     * @param s string to unparse in format yyyy/mm/dd/hh/mi without /'s
     * @return calendar
     */
    public static Calendar unparseCalendar(String s){
        if(s.length()!=12)
            throw new IllegalArgumentException();
        int year = Integer.parseInt(s.substring(0, 4));
        int month = Integer.parseInt(s.substring(4, 6));
        int day = Integer.parseInt(s.substring(6, 8));
        int hour = Integer.parseInt(s.substring(8, 10));
        int minute = Integer.parseInt(s.substring(10, 12));
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        return calendar;
    }

    /**
     *
     * @param s String to unparse in format hh/mi without /'s
     * @return calendar
     */
    public static Calendar unparseTime(String s){
        if(s.length()!=4)
            throw new IllegalArgumentException();
        int hour = Integer.parseInt(s.substring(0, 2));
        int minute = Integer.parseInt(s.substring(2, 4));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return calendar;
    }

    /**
     *
     * @param s String to unparse in format s/m/t/w/th/f/sa without /'s
     * @return set of days
     */
    public static Set<Day> unparseDays(String s){
        if(s.length()!=7)
            throw new IllegalArgumentException();
        Set<Day> set = new HashSet<>();
        char[] c = s.toCharArray();
        for (int i=0;i<7;i++){
            if(c[i]=='1'){
                switch (i){
                    case 0:
                        set.add(Day.SUNDAY);
                        break;
                    case 1:
                        set.add(Day.MONDAY);
                        break;
                    case 2:
                        set.add(Day.TUESDAY);
                        break;
                    case 3:
                        set.add(Day.WEDNESDAY);
                        break;
                    case 4:
                        set.add(Day.THURSDAY);
                        break;
                    case 5:
                        set.add(Day.FRIDAY);
                        break;
                    case 6:
                        set.add(Day.SATURDAY);
                        break;
                }
            }
        }
        return set;
    }

    /**
     *
     * @param s String to unparse; value of s as int: 0 <= s <= 6
     * @return set of days
     */
    public static Day unparseDay(String s){
        switch (Integer.parseInt(s)){
            case 0:
                return Day.SUNDAY;
            case 1:
                return Day.MONDAY;
            case 2:
                return Day.TUESDAY;
            case 3:
                return Day.WEDNESDAY;
            case 4:
                return Day.THURSDAY;
            case 5:
                return Day.FRIDAY;
            case 6:
                return Day.SATURDAY;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static Day calendarDaytoDay(int calendarDay){
        switch(calendarDay){
            case Calendar.SUNDAY:
                return Day.SUNDAY;
            case Calendar.MONDAY:
                return Day.MONDAY;
            case Calendar.TUESDAY:
                return Day.TUESDAY;
            case Calendar.WEDNESDAY:
                return Day.WEDNESDAY;
            case Calendar.THURSDAY:
                return Day.TUESDAY;
            case Calendar.FRIDAY:
                return Day.FRIDAY;
            case Calendar.SATURDAY:
                return Day.SATURDAY;
            default:
                throw new IllegalArgumentException();
        }
    }

    public static int dayToCalendaryDay(Day day){
        switch(day){
            case SUNDAY:
                return Calendar.SUNDAY;
            case MONDAY:
                return Calendar.MONDAY;
            case TUESDAY:
                return Calendar.TUESDAY;
            case WEDNESDAY:
                return Calendar.WEDNESDAY;
            case THURSDAY:
                return Calendar.THURSDAY;
            case FRIDAY:
                return Calendar.FRIDAY;
            case SATURDAY:
                return Calendar.SATURDAY;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static String appendZeroToShortTime(String s){
        if(s.length()==1)
            return ("0" + s);
        else
            return s;
    }
}
