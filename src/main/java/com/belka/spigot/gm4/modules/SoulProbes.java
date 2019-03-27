package com.belka.spigot.gm4.modules;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_13_R1.IChatBaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R1.inventory.CraftMetaBook;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SoulProbes {

    private TextComponent n = new TextComponent("\n");

    public ItemStack SOUL_PROBES_BOOK() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();

        List<IChatBaseComponent> pages;
        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return book;
        }

        List<TextComponent> p1 = new ArrayList<>();
        TextComponent p1m2 = new TextComponent("      " + ChatColor.DARK_GREEN + "Select target\n\n");
        p1m2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asc select"));
        TextComponent p1m3 = new TextComponent("    " + ChatColor.RED + "Deselect target");
        p1m3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asc deselect"));

        p1.add(new TextComponent("         " + ChatColor.GOLD + "" + ChatColor.BOLD + "Statues\n\n\n"));
        p1.add(p1m2);
        p1.add(p1m3);
        p1.add(new TextComponent("\n\n\n\n\n" + ChatColor.GRAY + "" + ChatColor.ITALIC + "Original book from\n"));
        p1.add(new TextComponent(ChatColor.GRAY + "" + ChatColor.ITALIC + "Xisumavoid's Vanilla\n"));
        p1.add(new TextComponent(ChatColor.GRAY + "" + ChatColor.ITALIC + "Tweaks"));

        List<TextComponent> p2 = new ArrayList<>();
        p2.add(new TextComponent("    " + ChatColor.GOLD + "" + ChatColor.BOLD + "Style Settings\n\n"));
        p2.add(createSetting("Show Base Plate", "/asc setting baseplate"));
        p2.add(createSetting("Show Arms", "/asc setting arms"));
        p2.add(createSetting("Small Stand", "/asc setting small"));
        p2.add(createSetting("Apply Gravity", "/asc setting gravity"));
        p2.add(createSetting("Stand Visible", "/asc setting visible"));
        p2.add(createSetting("Display Name", "/asc setting name"));

        List<TextComponent> p3 = new ArrayList<>();
        p3.add(new TextComponent("    " + ChatColor.GOLD + "" + ChatColor.BOLD + "Nudge Position\n\n"));
        String nudge = "/asc nudge ";
//        p3.add(createNudge("X", nudge));
//        p3.add(createNudge("Y", nudge));
//        p3.add(createNudge("Z", nudge));
        p3.add(new TextComponent("\nTurn gravity off before nudging Y-position\n\n"));
        p3.add(new TextComponent("   " + ChatColor.GOLD + "" + ChatColor.BOLD + "Adjust Rotation\n\n"));
//        p3.add(createRotation("/asc rotate "));

        List<TextComponent> p4 = new ArrayList<>();
        p4.add(new TextComponent("     " + ChatColor.GOLD + "" + ChatColor.BOLD + "Pose: Adjust\n\n"));
        p4.add(new TextComponent("          X     Y     Z\n"));
        String pose = "/asc pose ";
        String amount = "0.1";
//        p4.add(createPose("Head", pose, amount));
//        p4.add(createPose("Body", pose, amount));
//        p4.add(createPose("RArm", pose, amount));
//        p4.add(createPose("LArm", pose, amount));
//        p4.add(createPose("RLeg", pose, amount));
//        p4.add(createPose("LLeg", pose, amount));

        List<TextComponent> p5 = new ArrayList<>();
        p5.add(new TextComponent("    " + ChatColor.GOLD + "" + ChatColor.BOLD + "Locking/Sealing\n\n"));
        p5.add(new TextComponent("Lock the armor stand to prevent accidental changes\n\n"));
        TextComponent p5m1 = new TextComponent("      " + ChatColor.DARK_GREEN + "Lock");
        p5m1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asc lock true"));
        TextComponent p5m2 = new TextComponent(ChatColor.RED + "Unlock");
        p5m2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asc lock false"));
        p5.add(p5m1);
        p5.add(new TextComponent(ChatColor.RESET + " / "));
        p5.add(p5m2);
        p5.add(new TextComponent("\n\nSealed armor stands are locked and invulnerable\n\n"));
        TextComponent p5m3 = new TextComponent("       " + ChatColor.DARK_GREEN + "Seal");
        p5m3.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asc seal true"));
        TextComponent p5m4 = new TextComponent(ChatColor.RED + "Unseal");
        p5m4.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/asc seal false"));
        p5.add(p5m3);
        p5.add(new TextComponent(ChatColor.RESET + " / "));
        p5.add(p5m4);

        addPages(pages,
                createPage(p1),
                createPage(p2),
                createPage(p3),
                createPage(p4),
                createPage(p5)
        );

        bookMeta.setTitle(ChatColor.GOLD + "Soul Probes");
        bookMeta.setAuthor(ChatColor.DARK_AQUA + "Gamemode 4");

        book.setItemMeta(bookMeta);
        return book;
    }

    // BOOK GENERATION
    public TextComponent createSetting(String name, String comm) {
        TextComponent tot = new TextComponent(name + "\n      ");

        TextComponent yes = new TextComponent(ChatColor.DARK_GREEN + "Yes");
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, comm + " true"));
        TextComponent space = new TextComponent(ChatColor.RESET + " / ");
        TextComponent no = new TextComponent(ChatColor.RED + "No");
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, comm + " false"));

        tot.addExtra(yes);
        tot.addExtra(space);
        tot.addExtra(no);
        tot.addExtra(n);
        return tot;
    }

    private IChatBaseComponent createPage(List<TextComponent> p) {
        BaseComponent tot = new TextComponent();
        for (TextComponent txt : p) {
            tot.addExtra(txt);
        }
        BaseComponent[] bc = new BaseComponent[]{tot};
        return IChatBaseComponent.ChatSerializer.a(ComponentSerializer.toString(bc));
    }

    private void addPages(List<IChatBaseComponent> book, IChatBaseComponent... pages) {
		Collections.addAll(book, pages);
    }
}
