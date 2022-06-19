package me.hao0.jbs.store.util;

import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class Dates {

    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";


    public static Boolean isValidDate(String date){
        return isValidDate(date, "\\d{4}-\\d{2}-\\d{2}");
    }


    public static Boolean isValidDate(String date, String pattern){
        return !Strings.isNullOrEmpty(date)
                && Pattern.compile(pattern).matcher(date).matches();
    }


    public static Date now(){
        return new Date();
    }


    public static String now(String format){
        return format(now(), format);
    }


    public static Date toDate(String dateStr){
        return toDate(dateStr, DEFAULT_DATE_FORMAT);
    }


    public static Date toDate(String dateStr, String pattern){
        return DateTimeFormat.forPattern(pattern).parseDateTime(dateStr).toDate();
    }


    public static Date toDate(long millis) {
        return new DateTime(millis).toDate();
    }


    public static String format(Date date, String format){
        return new DateTime(date).toString(format);
    }


    public static String format(Date date){
        return new DateTime(date).toString(DEFAULT_DATE_FORMAT);
    }


    public static String format(Long mills){
        return new DateTime(mills).toString(DEFAULT_DATE_FORMAT);
    }


    public static String format(Long mills, String pattern){
        return new DateTime(mills).toString(pattern);
    }


    public static long timeInterval(Date startTime, Date endTime) {
        long start = startTime.getTime();
        long end = endTime.getTime();
        return (end - start) / 1000;
    }


    public static String timeIntervalStr(Date startTime, Date endTime){

        Interval interval = new Interval(startTime.getTime(), endTime.getTime());
        Period period = interval.toPeriod();

        StringBuilder str = new StringBuilder();

        if (period.getYears() > 0){
            str.append(period.getMonths()).append("y, ");
        }

        if (period.getMonths() > 0){
            str.append(period.getMonths()).append("M, ");
        }

        if (period.getDays() > 0){
            str.append(period.getDays()).append("d, ");
        }

        if (period.getHours() > 0){
            str.append(period.getHours()).append("h, ");
        }

        if (period.getMinutes() > 0){
            str.append(period.getMinutes()).append("m, ");
        }

        if (period.getSeconds() > 0){
            str.append(period.getSeconds()).append("s");
        }

        return str.toString();
    }


    public static Date startOfDay(Date date) {
        return new DateTime(date).withTimeAtStartOfDay().toDate();
    }


    public static Date endOfDay(Date date) {
        return new DateTime(date).millisOfDay().withMaximumValue().toDate();
    }


    public static Date dayOfWeek(Integer day){
        return new DateTime(DateTime.now().toString("yyyy-MM-dd")).withDayOfWeek(day).toDate();
    }


    public static Date dayOfMonth(Integer day){
        return new DateTime(DateTime.now().toString("yyyy-MM-dd")).withDayOfMonth(day).toDate();
    }


    public static Date dayOfYear(Integer day){
        return new DateTime(DateTime.now().toString("yyyy-MM-dd")).withDayOfYear(day).toDate();
    }


    public static Date addMinutes(Date date, int numOfMinutes) {
        return new DateTime(date).plusMinutes(numOfMinutes).toDate();
    }


    public static Date addHours(Date date, int numOfHours) {
        return new DateTime(date).plusHours(numOfHours).toDate();
    }


    public static Date addDays(Date date, int numdays) {
        return new DateTime(date).plusDays(numdays).toDate();
    }


    public static Date addWeeks(Date date, int numWeeks) {
        return new DateTime(date).plusWeeks(numWeeks).toDate();
    }


    public static Date addMonths(Date date, int numMonths) {
        return new DateTime(date).plusMonths(numMonths).toDate();
    }


    public static Date addYears(Date date, int numYears) {
        return new DateTime(date).plusYears(numYears).toDate();
    }


    public static Boolean isAfter(Date a, Date b){
        return new DateTime(a).isAfter(b.getTime());
    }


    public static Boolean isAfterNow(Date a){
        return new DateTime(a).isAfterNow();
    }


    public static Boolean isBefore(Date a, Date b){
        return new DateTime(a).isBefore(b.getTime());
    }


    public static Boolean isBeforeNow(Date a){
        return new DateTime(a).isBeforeNow();
    }



    public static Date startDateOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfMonth().withMinimumValue().toDate();
    }


    public static Date endDateOfMonth(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfMonth().withMaximumValue().toDate();
    }



    public static Date startDateOfWeek(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfWeek().withMinimumValue().toDate();
    }


    public static Date endDateOfWeek(Date date){
        DateTime dateTime = new DateTime(date);
        return dateTime.dayOfWeek().withMaximumValue().toDate();
    }
}
