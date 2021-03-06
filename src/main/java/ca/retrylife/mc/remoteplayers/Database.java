package ca.retrylife.mc.remoteplayers;

import java.util.prefs.Preferences;

import org.jetbrains.annotations.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Persistent storage for the application
 */
public class Database {
    private Logger logger = LogManager.getLogger(getClass());
    private static Database instance = null;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Preference prefixes
    private static final String DYNMAP_LINKAGE_PREFIX = "dynmap_";

    // Class preferences access
    private Preferences preferences;

    private Database() {

        // Access the class preferences
        preferences = Preferences.userRoot().node(this.getClass().getName());

        logger.debug("Opened lock on mod preferences");
    }

    private String getPrefKeyForServer(String server) {
        return String.format("%s%s", DYNMAP_LINKAGE_PREFIX, server);
    }

    /**
     * Sets if waypoint integration should be enabled
     * 
     * @param enabled Should be enabled?
     */
    public void setEnableIntegration(boolean enabled) {
        logger.debug(String.format("Waypoint integration set: %b", enabled));
        preferences.put("integrate_waypoints", (enabled) ? "T" : "F");
    }

    /**
     * Gets if waypoint integration should be enabled
     */
    public boolean getIntegrationEnabled() {
        return preferences.get("integrate_waypoints", "F").equals("T");
    }

    /**
     * Get the dynmap url for a minecraft server. May be null
     * 
     * @param server Minecraft server
     * @return Dynmap URL
     */
    public @Nullable String getConfiguredDynmapForServer(String server) {
        return preferences.get(getPrefKeyForServer(server), null);
    }

    /**
     * Check if there is a dynmap server linked to a minecraft server
     * 
     * @param server Minecraft server
     * @return Does link exist?
     */
    public boolean serverHasDynmapLinked(String server) {
        return getConfiguredDynmapForServer(server) != null;
    }

    /**
     * Set a dynmap url for a minecraft server
     * 
     * @param server    Minecraft server
     * @param remoteURL Dynmap URL
     */
    public void setDynmapRemoteForServer(String server, String remoteURL) {
        logger.debug(String.format("Added dynmap to server: %s -> %s", server, remoteURL));
        preferences.put(getPrefKeyForServer(server), remoteURL);
    }

    /**
     * Removes any dynmap URLs from a minecraft server
     * 
     * @param server Minecraft server
     */
    public void unlinkDynmapRemoteForServer(String server) {
        logger.debug(String.format("Removed dynmap from server: %s ", server));
        preferences.remove(getPrefKeyForServer(server));
    }
}