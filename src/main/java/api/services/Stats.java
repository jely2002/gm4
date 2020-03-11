package api.services;

import api.services.Metrics;
import com.belka.spigot.gm4.MainClass;
import com.belka.spigot.gm4.interfaces.Initializable;

import java.util.HashMap;
import java.util.Map;

public class Stats implements Initializable {

    private Metrics metrics;
    private MainClass mc;

    @Override
    public void init(MainClass mc) {
        this.mc = mc;
        metrics = new Metrics(mc);
        addModuleCharts();
    }

    private void addModuleCharts() {
        metrics.addCustomChart(new Metrics.DrilldownPie("disabled_modules", () -> {
            Map<String, Map<String, Integer>> map = new HashMap<>();
            for(String module : mc.getConfig().getConfigurationSection("").getKeys(false)) {
                Map<String, Integer> entry = new HashMap<>();
                entry.put(module, 1);
                if(!mc.getConfig().getBoolean(module + ".enabled")) {
                   map.put("module", entry);
                }
            }
            return map;
        }));
    }

}
