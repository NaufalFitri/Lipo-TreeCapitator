package dev.lipoteam.treeCapitator;

import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

// Author Naufal & Zaff
// Tree Capitator was inspired in yada yada..

public class Event implements Listener {

    private Boolean enable = false;

    private final Plugin plugin;

    private boolean AsyncMode;
    private boolean isDelay;
    private boolean BreakAfterParticle;
    private boolean haveParticle;
    private boolean canDamage;
    private boolean needSneak;
    private boolean unbreakingInfluence;
    private boolean particlesoundenable;

    private Particle particle;
    private Sound particlesound;
    private List<String> worlds;
    private int particlepercentage;
    private int particleamount;
    private long delayTime;
    private double particlespeed;
    private int extraDamage;

    public Event(Configurations config, Plugin plugin) {
        this.plugin = plugin;
        setConfig(config);
    }

    public void setConfig(Configurations config) {

        this.enable = config.isEnabled();
        this.AsyncMode = config.isAsyncMode();
        this.isDelay = config.isDelay();
        this.BreakAfterParticle = config.BAP();
        this.haveParticle = config.haveParticle();
        this.canDamage = config.canDamage();
        this.needSneak = config.needSneak();
        this.unbreakingInfluence = config.isUnbreakingInfluence();

        this.particle = config.particle();
        this.particlesoundenable = config.isParticleSoundable();
        this.particlesound = config.particleSound();
        this.particlepercentage = config.particlePercentage();
        this.particleamount = config.particleAmount();
        this.particlespeed = config.particleSpeed();
        this.delayTime = config.delayTime();
        this.extraDamage = config.extraDamage();
        this.worlds = config.worlds();
    }

    Method method = new Method(TreeCapitator.getInstance());
    DataManager manager = new DataManager(TreeCapitator.getInstance());

    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerBreak(BlockBreakEvent e) {

        Player p = e.getPlayer();

        ItemStack tool = p.getInventory().getItemInMainHand();
        ItemMeta toolMeta = tool.getItemMeta();
        Damageable toolDamage = (Damageable) toolMeta;

        Block block = e.getBlock();
        String type = block.getType().name();

        String world = p.getWorld().getName();

        if (!worlds.contains(world) && !worlds.isEmpty()) {
            return;
        }

        if (!enable || p.getGameMode().equals(GameMode.CREATIVE) || !tool.getType().name().endsWith("_AXE") || (!type.endsWith("_STEM") && !type.endsWith("_LOG")) || manager.hasData(block, "placed"))
            return;

        if (needSneak && !p.isSneaking()) {
            return;
        }

        if (block.hasMetadata("inprocess")) {
            e.setCancelled(true);
            return;
        }

        if (type.startsWith("STRIPPED_")) {
            type = type.substring(9);
        }

        if (AsyncMode) {
            // Asynchronous mode for detecting block (Not necessary as it does not have a lot of computation)
            method.getLogsAsync(block.getLocation(), type, logs -> {
                assert toolDamage != null;
                if (toolDamage.getDamage() + logs.size() <= tool.getType().getMaxDurability()) {
                    MainLogic(logs, tool);
                }
            });

        } else {
            List<Block> logs = method.getLogs(block.getLocation(), type);

            // Where the fun stuff happens, the particle, the delay but just for boolean detection
            // The method, or heavy duty still in the TreeMethod.class
            assert toolDamage != null;
            if (toolDamage.getDamage() + logs.size() <= tool.getType().getMaxDurability()) {
                MainLogic(logs, tool);
            }

            // method.getLogs(block.getLocation(), type).forEach(Block::breakNaturally);
            // the code at the top is one of the way to make the code simple but unreadable
        }

    }

    private void MainLogic(List<Block> logs, ItemStack tool) {

        if (isDelay) {
            if (haveParticle) {
                method.SpawnParticle(logs, delayTime, BreakAfterParticle, particle, particleamount, particlespeed, particlepercentage, particlesound, particlesoundenable);
            } else {
                long delay = delayTime;
                for (Block log : logs) {
                    method.BreakLogic(log, delay);
                    log.setMetadata("inprocess", new FixedMetadataValue(plugin, true));
                    delay += delayTime;
                }
            }
        } else {
            if (haveParticle) {
                method.SpawnParticle(logs, 0, BreakAfterParticle, particle, particleamount, particlespeed, particlepercentage, particlesound, particlesoundenable);
            } else {
                for (Block log : logs) {
                    log.setMetadata("inprocess", new FixedMetadataValue(plugin, true));
                    method.BreakLogic(log, 0);
                }
            }
        }

        if (canDamage) {

            int totalDamage = 0;
            if (unbreakingInfluence) {

                for (int i = 0; i < logs.size(); i++) {
                    totalDamage = method.CalcDamageTool(tool, totalDamage, extraDamage);
                }
                method.DamageTool(tool, totalDamage);

            } else {

                method.DamageTool(tool, logs.size() + (logs.size() * extraDamage));

            }

        }
    }

    // Put PersistentData in the block to prevent TreeCapitator detection
    // Careful with the Memory Overhead / Memory Leakage when using this
    @EventHandler(priority = EventPriority.NORMAL)
    public void OnPlayerPlace(BlockPlaceEvent e) {

        Block block = e.getBlock();
        // Player p = e.getPlayer();

        if (block.getType().name().endsWith("_LOG")) {
            manager.setdata(block, "placed", true);
        }
    }


}
