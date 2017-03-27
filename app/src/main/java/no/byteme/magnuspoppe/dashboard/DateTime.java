package no.byteme.magnuspoppe.dashboard;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by MagnusPoppe on 25/03/2017.
 */

public class DateTime implements Comparable
{
    int year;
    int month;
    int day;

    int hour;
    int minute;
    int second;

    final static private int DATE = 0;
    final static private int TIME = 1;

    final static private int YEAR = 0;
    final static private int MONTH = 1;
    final static private int DAY = 2;

    final static private int HOUR = 0;
    final static private int MINUTE = 1;
    final static private int SECOND = 2;

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

    public boolean sameDay(int year, int month, int day)
    {
        return this.year == year && this.month == month && this.day == day;
    }

    @Override
    public String toString()
    {
        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
    }

    public String prettyPrint(View view, int year, int month, int day)
    {
        if (sameDay(year, month, day))
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
