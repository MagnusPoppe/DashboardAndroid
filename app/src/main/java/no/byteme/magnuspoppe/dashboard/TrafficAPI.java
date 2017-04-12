package no.byteme.magnuspoppe.dashboard;

/**
 * Basic interface for use with my API.
 * Created by MagnusPoppe on 23/03/2017.
 */

interface TrafficAPI
{
    boolean LIVE_CONNECTION = true;

    // Path to API:
    String PATH = "api.php/";

    // Base addresses:  192.168.10.103
    String LOCAL_DOMAIN = "http://192.168.10.103:80/traffic/";
    //String LIVE_DOMAIN  = "http://byteme.no/";
    String LIVE_DOMAIN  = "http://itfag.usn.no/~210852/";

    // Addons to get more data.
    String TRAFFIC = "traffic/";
    String TRAFFIC_FOR_SITE = "traffic/site/";
    String VISITOR = "visitor/";
    String SITE    = "site/";
}