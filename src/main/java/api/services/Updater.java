package api.services;

import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
        JSONParser parser = new JSONParser();
        try {
            URL github = new URL(latestReleaseURL);
            URLConnection con = github.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                JSONArray a = (JSONArray) parser.parse(inputLine);
                for (Object o : a) {
                    JSONObject release = (JSONObject) o;
                    String version = (String) release.get("name");
                    return version.substring(1);
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}