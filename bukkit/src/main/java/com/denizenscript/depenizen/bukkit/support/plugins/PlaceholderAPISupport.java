package com.denizenscript.depenizen.bukkit.support.plugins;

import com.denizenscript.depenizen.bukkit.DepenizenPlugin;
import com.denizenscript.depenizen.bukkit.support.Support;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.aufdemrand.denizen.objects.dPlayer;
import net.aufdemrand.denizen.tags.BukkitTagContext;
import net.aufdemrand.denizencore.objects.Element;
import net.aufdemrand.denizencore.tags.Attribute;
import net.aufdemrand.denizencore.tags.TagContext;
import net.aufdemrand.denizencore.tags.TagManager;
import net.aufdemrand.denizencore.utilities.CoreUtilities;
import net.aufdemrand.denizencore.utilities.debugging.dB;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPISupport extends Support {

    public PlaceholderAPISupport() {
        if (!new PlaceholderHook().register()) {
            DepenizenPlugin.depenizenLog("Failed to register placeholder for identifier 'denizen'!" +
                    " Denizen PlaceholderAPI placeholders will not function.");
        }
        registerAdditionalTags("placeholder");
    }

    @Override
    public String additionalTags(Attribute attribute, TagContext tagContext) {
        // <--[tag]
        // @attribute <placeholder[<name>]>
        // @returns Element
        // @description
        // Returns the value of the placeholder.
        // @Plugin DepenizenBukkit, PlaceholderAPI
        // -->
        if (attribute.startsWith("placeholder")) {
            String placeholder = CoreUtilities.toLowerCase(attribute.getContext(1));
            if (placeholder.startsWith("denizen_")) {
                dB.echoError("Cannot use <placeholder[]> tags with 'denizen' prefix!");
                return null;
            }
            Player player = null;
            if (tagContext instanceof BukkitTagContext) {
                player = ((BukkitTagContext) tagContext).player != null ? ((BukkitTagContext) tagContext).player.getPlayerEntity() : null;
            }
            return new Element(PlaceholderAPI.setPlaceholders(player, "%" + placeholder + "%"))
                    .getAttribute(attribute.fulfill(1));
        }
        return null;
    }

    private static class PlaceholderHook extends PlaceholderExpansion {

        @Override
        public boolean canRegister() {
            return true;
        }

        @Override
        public String getAuthor() {
            return "The DenizenScript Team";
        }

        @Override
        public String getIdentifier() {
            return "denizen";
        }

        @Override
        public String getVersion() {
            return "1.0.0";
        }

        @Override
        public String onRequest(OfflinePlayer player, String identifier) {
            return TagManager.tag(identifier, new BukkitTagContext(dPlayer.mirrorBukkitPlayer(player), null, false, null, false, null));
        }
    }
}
