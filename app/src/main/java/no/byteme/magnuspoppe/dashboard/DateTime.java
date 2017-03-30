package no.byteme.magnuspoppe.dashboard;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.Calendar;

/**
 * Created by MagnusPoppe on 25/03/2017.
 */

public class DateTime implements Comparable
{
    final static private int DATE = 0;
    final static private int TIME = 1;

    final static private int YEAR = 0;
    final static private int MONTH = 1;
    final static private int DAY = 2;

    final static private int HOUR = 0;
    final static private int MINUTE = 1;
    final static private int SECOND = 2;

    int year;
    int month;
    int day;

    int hour;
    int minute;
    int second;

    public DateTime(String textDateTime)
    {
        String[] dateTime = textDateTime.split(" ");
        String[] date = dateTime[DATE].split("-");
        String[] time = dateTime[TIME].split(":");

        try
        {
            year = Integer.parseInt(date[YEAR]);
            month = Integer.parseInt(date[MONTH]);
            day = Integer.parseInt(date[DAY]);

            hour = Integer.parseInt(time[HOUR]);
            minute = Integer.parseInt(time[MINUTE]);
            second = Integer.parseInt(time[SECOND]);
        }
        catch (ArithmeticException e)
        {
            e.printStackTrace();
        }
    }

    public boolean sameDay(DateTime someDay)
    {
        return this.year == someDay.year && this.month == someDay.month && this.day == someDay.day;
    }

    public static DateTime now()
    {
        Calendar c = Calendar.getInstance();
        String now = c.get(Calendar.YEAR)+"-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DATE);
        now += " " + c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
        return new DateTime(now);
    }

    public boolean lastXDays(int x)
    {
        DateTime now = now();

        // Finding the correct date for x days ago.
        while (x<0)
        {
            if (x < now.day)
            {
                now.day -= x;
                x -= now.day;
            }
            else
            {
                x -= now.day;
                now.month -= 1;


                if (month == 0)
                {
                    year--;
                    month = 12;
                }

                if (now.month == 2)          now.day = 28;
                else if (now.month % 2 != 0) now.day = 31;
                else if (now.month % 2 == 0) now.day = 30;
            }
        }

        // Asserting answer:
        return compareTo(now) > 0;
    }

    @Override
    public String toString()
    {
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
    }

    public String prettyPrint(View view)
    {
        if (sameDay(DateTime.now()))
        {
            return printToday();
        }
        else return prettyPrint(view.getResources());
    }

    public String printToday()
    {
        return "today, at kl. "+ hour + ":" + minute;
    }

    public String prettyPrint(Resources res)
    {
        String[] months = res.getStringArray(R.array.months);
        if (month == 0) month = 1;
        return day + ". " + months[month-1] + " " + year + " at kl. "+ hour + ":" + minute;
    }

    /**
     * @param o other DateTime
     * @return -1 if this is latest, 1 otherwise. special case: 0 if same.
     */
    @Override
    public int compareTo(@NonNull Object o)
    {
        DateTime other = (DateTime) o;

        if (year < other.year) return -1;
        else if (year > other.year) return 1;
        else // year == other.year
        {
            if (month < other.month) return -1;
            else if (month > other.month) return 1;
            else // month == other.month
            {
                if (day < other.day) return -1;
                else if (day > other.day) return 1;
                else // day == other.day
                {
                    if (hour < other.hour) return -1;
                    else if (hour > other.hour) return 1;
                    else // hour == other.hour
                    {
                        if (minute < other.minute) return -1;
                        else if (minute > other.minute) return 1;
                        else // minute == other.minute
                        {
                            if (second < other.second) return -1;
                            else if (second > other.second) return 1;
                        }
                    }
                }
            }
        }
        return 0; // SAME DATES.
    }
}
