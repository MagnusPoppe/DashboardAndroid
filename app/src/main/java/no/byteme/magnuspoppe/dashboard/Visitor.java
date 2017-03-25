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
    private String ip;
    private String hostname;
    private String organisation;
    private String city;
    private String country;
    private double latitude;
    private double longitude;

    ArrayList<Visit> visits;

    /**
     * Default constructor:
     */
    public Visitor()
    {
        ip = null;
        hostname = null;
        organisation = null;
        city = null;
        country = null;
        latitude = 0.0;
        longitude = 0.0;
    }

    public Visitor(JSONObject json) throws JSONException
    {
        ip              = json.getString("ip");
        hostname        = json.getString("hostname");
        organisation    = json.getString("org");
        city            = json.getString("city");
        country         = json.getString("country");
        latitude        = json.getDouble("latitude");
        longitude       = json.getDouble("longitude");

        JSONArray visitJSON = new JSONArray(json.opt("visits"));
        for (int i = 0; i < visitJSON.length(); i++)
        {
            JSONObject obj = (JSONObject) visitJSON.get(i);
            visits.add( new Visit(this, obj.getString("datetime"), obj.getInt("site")));
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
}
