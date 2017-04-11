package no.byteme.magnuspoppe.dashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class to store data about visitors. Every visitor is identified by
 * their IP address. They also have a visit-list containing dates
 * for each visit.
 * Created by MagnusPoppe on 23/03/2017.
 */

public class Visitor
{
    protected String ip;
    protected String hostname;
    protected String organisation;
    protected String city;
    protected String country;
    protected double latitude;
    protected double longitude;

    ArrayList<Visit> visits;

    public Visitor(JSONObject json) throws JSONException
    {
        ip              = json.getString("ip") != "NULL"        ? json.getString("ip") : "";
        hostname        = json.getString("hostname")  != "NULL" ? json.getString("hostname")  : "";
        organisation    = json.getString("org")  != "NULL"      ? json.getString("org")  : "";
        city            = json.getString("city")  != "NULL"     ? json.getString("city")  : "";
        country         = json.getString("country")  != "NULL"  ? json.getString("country")  : "";
        latitude        = json.getDouble("latitude");
        longitude       = json.getDouble("longitude");
        visits          = new ArrayList<>();

        JSONArray visitJSON = json.getJSONArray("visits");
        for (int i = 0; i < visitJSON.length(); i++)
        {
            JSONObject obj = (JSONObject) visitJSON.get(i);
            visits.add(
                    new Visit(this, obj.optString("datetime"), obj.optInt("site"))
            );
        }
    }

    /**
     * Constructor:
     * @param ip
     * @param name
     * @param org
     * @param city
     * @param country
     * @param lat
     * @param lng
     */
    public Visitor(String ip, String name, String org, String city, String country, double lat, double lng)
    {
        this.ip = ip;
        this.hostname = name;
        this.organisation = org;
        this.city = city;
        this.country = country;
        this.latitude = lat;
        this.longitude = lng;
    }

    public Visit findFirstVisit()
    {
        Visit first = null;

        // Looping through to find the first:
        for( Visit visit : visits)
        {
            if (first == null) first = visit;
            else first = first.compareTo(visit) > 0 ? visit : first ;
        }
        return first;
    }


    public Visit findLastVisit()
    {
        Visit last = null;

        // Looping through to find the latest:
        for( Visit visit : visits)
        {
            if (last == null) last = visit;
            else last = last.compareTo(visit) < 0 ? visit : last ;
        }
        return last;
    }

    /**
     * Adds a visit to the visits list.
     * @param visit
     */
    public void addVisit(Visit visit)
    {
        visits.add(visit);
    }

    /**
     * Auto-generated Equals method.
     * @param o
     * @return true if same ip address.
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Visitor visitor = (Visitor) o;

        return ip != null ? ip.equals(visitor.ip) : visitor.ip == null;
    }

    /**
     * @return JSON object of this visitor:
     */
    public String toJSON()
    {
        return "{" +
                    "\"ip\": \""+ip+ "\", "+
                    "\"hostname\": \""+ hostname+"\", "+
                    "\"organisation\": \"" + organisation+"\", "+
                    "\"city\": \""+city+"\", "+
                    "\"country\": \""+ country+"\", "+
                    "\"latitude\": "+latitude+", "+
                    "\"longitude\": "+longitude+
                "}";
    }
}
