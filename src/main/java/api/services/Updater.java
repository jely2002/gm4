package api.services;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import com.sun.nio.sctp.IllegalReceiveException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.logging.Level;

public class Updater implements Initializable {

    private String latestReleaseURL = "https://api.github.com/repos/jely2002/gm4/releases/latest";
    private MainClass mc;

    @Override
    public void init(MainClass mc) {
        this.mc = mc;
        checkUpdate();
    }

    public void checkUpdate() {
        String latestVersion = getLatestVersion();
        if(getPluginVersion().equals(latestVersion)) {
            mc.getLogger().log(Level.INFO, "Gamemode 4 is up to date!");
        } else {
            mc.getLogger().log(Level.WARNING, "There is an update available for Gamemode 4!");
            mc.getLogger().log(Level.WARNING, "Current version: " + getPluginVersion() + " | New version: " + latestVersion);
            mc.getLogger().log(Level.WARNING, "Download it now at: https://github.com/jely2002/gm4/releases/latest");
        }
    }

    public String getLatestReleaseURL() {
        return latestReleaseURL;
    }

    public String getPluginVersion() {
        return mc.getDescription().getVersion();
    }

    public String getLatestVersion() {
        try {
            URLConnection conn = new URL(latestReleaseURL).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            String version = (String) jsonObject.get("name");
            return version.substring(1);

        } catch(Exception e) {
            e.printStackTrace();
        }
        throw new IllegalReceiveException("GitHub did not respond to API request, please report this.");
    }
}
