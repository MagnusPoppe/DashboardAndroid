package no.byteme.magnuspoppe.dashboard;


import android.support.annotation.NonNull;

/**
 * Class to store data about a vistor's visits.
 * Created by MagnusPoppe on 23/03/2017.
 */

public class Visit implements Comparable
{
    protected Visitor visitor;
    protected DateTime datetime;
    protected int siteVisited;

    /**
     * Constructor
     * @param visitor owner of visit.
     * @param datetime date and time of visit.
     * @param site visited.
     */
    public Visit(Visitor visitor, String datetime, int site)
    {
        this.visitor = visitor;
        this.datetime = new DateTime(datetime);
        this.siteVisited = site;

        // Checking if visit was less than 30 days ago:
        if (ViewControllerActivity.last30Days != null)
            if (this.datetime.lastXDays(30))
                ViewControllerActivity.last30Days.add(this);
    }

    @Override
    public int compareTo(@NonNull Object o)
    {
        Visit visit = (Visit) o;
        return datetime.compareTo(visit.datetime);
    }
}
