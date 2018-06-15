package de.didi090302.deathchest;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class Main extends JavaPlugin implements Listener {

    public static HashMap<Block, Inventory> DeathChest;
    public static String prefix="§7[§3DeathChest§7]§r ";

    @Override
    public void onEnable() {
        DeathChest = new HashMap();
        onConfig();
        getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getConsoleSender().sendMessage(prefix+"§aPlugin loaded successfully!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().getConsoleSender().sendMessage(prefix+"§cPlugin unloaded successfully!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Block deathchest = p.getWorld().getBlockAt(p.getLocation().add(0.0D, 0.5D, 0.0D));
        deathchest.setType(Material.CHEST);

        Inventory content = Bukkit.getServer().createInventory(p.getInventory().getHolder(),54, "§6"+p.getName()+"'s items");
        ItemStack[] itemStacks = (ItemStack[])ArrayUtils.addAll(p.getInventory().getContents(), p.getEquipment().getArmorContents());
        content.setContents(itemStacks);

        DeathChest.put(deathchest, content);
        e.getDrops().clear();
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if((e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getClickedBlock().getType() == Material.CHEST)) {
            Block chest = e.getClickedBlock();
            for(Block chests : DeathChest.keySet()) {
                if(chests.getLocation().equals(chest.getLocation())) {
                    e.setCancelled(true);
                    e.getPlayer().openInventory(DeathChest.get(chests));
                }
            }
        }
    }

    public void onConfig() {
        reloadConfig();
        getConfig().addDefault("Deathchest.locationmessage", "§3Your Location Message");
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

}
