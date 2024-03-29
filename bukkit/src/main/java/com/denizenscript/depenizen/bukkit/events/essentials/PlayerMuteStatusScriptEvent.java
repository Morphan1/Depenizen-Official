package com.denizenscript.depenizen.bukkit.events.essentials;

import net.aufdemrand.denizen.BukkitScriptEntryData;
import net.aufdemrand.denizen.events.BukkitScriptEvent;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.objects.dObject;
import net.aufdemrand.denizencore.scripts.ScriptEntryData;
import net.aufdemrand.denizencore.scripts.containers.ScriptContainer;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.ess3.api.events.MuteStatusChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMuteStatusScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // player muted
    // player unmuted
    // player un-muted
    // player mute status changes
    //
    // @Regex ^on player (muted|unmuted|un-muted|mute status changes)$
    //
    // @Cancellable true
    //
    // @Triggers when a player is muted or un-muted.
    //
    // @Context
    // <context.status> Returns whether the player is muted.
    //
    // @Plugin DepenizenBukkit, Essentials
    //
    // -->

    public static PlayerMuteStatusScriptEvent instance;
    public MuteStatusChangeEvent event;
    public Element muted;

    public PlayerMuteStatusScriptEvent() {
        instance = this;
    }

    @Override
    public boolean couldMatch(ScriptContainer scriptContainer, String s) {
        String lower = CoreUtilities.toLowerCase(s);
        return lower.startsWith("player mute")
                || lower.startsWith("player unmuted")
                || lower.startsWith("player un-muted");
    }

    @Override
    public boolean matches(ScriptPath path) {
        String status = path.eventArgLowerAt(1);
        if (status.equals("muted") && muted.asBoolean()) {
            return true;
        }
        else if ((status.equals("unmuted") || status.equals("un-muted")) && !muted.asBoolean()) {
            return true;
        }
        else {
            return status.equals("status");
        }
    }

    @Override
    public String getName() {
        return "PlayerMuteStatus";
    }

    @Override
    public boolean applyDetermination(ScriptContainer container, String determination) {
        return super.applyDetermination(container, determination);
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(dPlayer.mirrorBukkitPlayer(event.getAffected().getBase()), null);
    }

    @Override
    public dObject getContext(String name) {
        if (name.equals("status")) {
            return muted;
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onPlayerAFKStatus(MuteStatusChangeEvent event) {
        muted = new Element(event.getValue());
        this.event = event;
        fire(event);
    }
}
