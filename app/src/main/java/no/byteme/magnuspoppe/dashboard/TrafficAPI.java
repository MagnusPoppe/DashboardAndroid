package no.byteme.magnuspoppe.dashboard;

/**
 * Basic interface for use with my API.
 * Created by MagnusPoppe on 23/03/2017.
 */

interface TrafficAPI
{
    boolean LIVE_CONNECTION = false;

    // Path to API:
    String PATH = "api/traffic.php/";

    // Base addresses:
    String LOCAL_DOMAIN = "http://localhost:80/traffic/";
    String LIVE_DOMAIN  = "http://byteme.no/";

    // Addons to get more data.
    String VISITOR = "visitor/";
    String SITE    = "site/";

    boolean isOnline();
}