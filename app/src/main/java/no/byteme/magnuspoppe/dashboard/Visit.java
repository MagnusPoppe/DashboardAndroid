package no.byteme.magnuspoppe.dashboard;

/**
 * Class to store data about a vistor's visits.
 * Created by MagnusPoppe on 23/03/2017.
 */

public class Visit
{
    private Visitor visitor;
    private String datetime;
    private int siteVisited;

    /**
     * Constructor
     * @param visitor owner of visit.
     * @param datetime date and time of visit.
     * @param site visited.
     */
    public Visit(Visitor visitor, String datetime, int site)
    {
        this.visitor = visitor;
        this.datetime = datetime;
        this.siteVisited = site;
    }
}
