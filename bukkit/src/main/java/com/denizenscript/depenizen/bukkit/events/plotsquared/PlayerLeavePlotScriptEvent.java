package com.denizenscript.depenizen.bukkit.events.plotsquared;

import com.denizenscript.depenizen.bukkit.objects.dPlotSquaredPlot;
import com.plotsquared.bukkit.events.PlayerLeavePlotEvent;
import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dEntity;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeavePlotScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // plotsquared player leaves plotsquaredplot
    // plotsquared player exits plotsquaredplot
    // plotsquared player leaves <dplotsquaredplot>
    // plotsquared player exits <dplotsquaredplot>
    //
    // @Regex ^on plotsquared player [^\s]+ level changes( in ((notable (cuboid|ellipsoid))|([^\s]+)))?$
    //
    // @Cancellable false
    //
    // @Triggers when a player leaves a plot.
    //
    // @Context
    // <context.plot> returns the plot the player left.
    //
    // @Plugin DepenizenBukkit, PlotSquared
    //
    // -->

    public PlayerLeavePlotScriptEvent() {
        instance = this;
    }

    public static PlayerLeavePlotScriptEvent instance;
    public PlayerLeavePlotEvent event;
    public dPlayer player;
    public dPlotSquaredPlot plot;

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("plotsquared player leaves") || lower.startsWith("plotsquared player exits");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String plotName = path.eventArgLowerAt(3);
        if (plotName.equals("plotsquaredplot")) {
            return true;
        }
        dPlotSquaredPlot dplot = dPlotSquaredPlot.valueOf(plotName);
        return dplot != null && dplot.equals(plot);
    }

    @Override
    public String getName() {
        return "PlayerLeavePlotEvent";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("plot")) {
            return plot;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlotLeave(PlayerLeavePlotEvent event) {
        if (dEntity.isNPC(event.getPlayer())) {
            return;
        }
        player = dPlayer.mirrorBukkitPlayer(event.getPlayer());
        plot = new dPlotSquaredPlot(event.getPlot());
        this.event = event;
        fire(event);
    }
}
