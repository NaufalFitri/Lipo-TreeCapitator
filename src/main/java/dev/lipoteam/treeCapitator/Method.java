package dev.lipoteam.treeCapitator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class Method {

    DataManager manager = new DataManager(TreeCapitator.getInstance());
    private final Plugin plugin;

    public Method(Plugin plugin) {
        this.plugin = plugin;
    }

    public List<Block> getEnv(Location origin, String type) {

        List<Block> blocks = new ArrayList<>();
        for (int x = -1; x < 2; x++)
            for (int y = 0; y < 2; y++)
                for (int z = -1; z < 2; z++) {
                    Location loc = origin.clone().add(x, y, z);
                    Block block = loc.getBlock();
                    if (!origin.equals(loc) && block.getType().name().endsWith(type) && !manager.hasData(block, "placed")) {
                        blocks.add(block);
                    }
                }
        return blocks;
    }

    public List<Block> getLogs(Location origin, String type) {
        List<Block> logs = new ArrayList<>();
        List<Block> next = getEnv(origin, type);

        while (!next.isEmpty()) {
            List<Block> nextNext = new ArrayList<>();
            for (Block log : next) {

                if (!logs.contains(log)) {
                    logs.add(log);
                    nextNext.addAll(getEnv(log.getLocation(), type));
                }
            }
            next = nextNext;
        }
        return logs;
    }

    public void getLogsAsync(Location origin, String type, Consumer<List<Block>> callback) {

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            List<Block> logs = new ArrayList<>();
            List<Block> next = null;
            next = getEnv(origin, type);

            while (!next.isEmpty()) {
                List<Block> nextNext = new ArrayList<>();
                for (Block log : next) {
                    // You can’t check contains in an async task, so we will just keep track of checked logs
                    if (!logs.contains(log)) {
                        logs.add(log);
                        nextNext.addAll(getEnv(log.getLocation(), type));
                    }
                }
                next = nextNext;
            }

            // Now switch back to the main thread to execute the callback with the result
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(logs));
        });
    }

    public void SpawnParticle(List<Block> blocks, long delayTime, boolean BAP, Particle particle, int particleamount, double particlespeed, int particlepercentage, Sound particlesound, boolean particlesoundenable) {
        long delay = delayTime;
        if (BAP) {
            for (Block log : blocks) {
                int ran = new Random().nextInt(100);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (ran < particlepercentage) {
                        log.getWorld().spawnParticle(particle, log.getLocation(), particleamount, 0, 0, 0, particlespeed);
                        if (particlesoundenable) {
                            log.getWorld().playSound(log.getLocation(), particlesound, 1, 1);
                        }
                    }
                }, delay);
                delay += delayTime;
                log.setMetadata("inprocess", new FixedMetadataValue(plugin, true));
            }
            for (Block log : blocks) {
                BreakLogic(log, delayTime * blocks.size());
            }
        } else {
            for (Block log : blocks) {
                int ran = new Random().nextInt(100);
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (ran < particlepercentage) {
                        log.getWorld().spawnParticle(particle, log.getLocation(), particleamount, 0, 0, 0, particlespeed);
                        if (particlesoundenable) {
                            log.getWorld().playSound(log.getLocation(), particlesound, 1, 1);
                        }
                    }
                }, delay);
                BreakLogic(log, delay);
                delay += delayTime;
                log.setMetadata("inprocess", new FixedMetadataValue(plugin, true));
            }
        }
    }

    public void BreakLogic(Block log, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            log.breakNaturally();
            log.getWorld().playSound(log.getLocation(), Sound.BLOCK_WOOD_BREAK, 1, 1);
        }, delay);
    }

    public int CalcDamageTool(ItemStack tool, int calcDamage, int extraDamage) {

        int ran = new Random().nextInt(100);

        if (tool.containsEnchantment(Enchantment.UNBREAKING)) {
            if (ran < (100 / (tool.getEnchantmentLevel(Enchantment.UNBREAKING) + 1))) {
                calcDamage += 1 + extraDamage;
            }
        } else {
            calcDamage += 1 + extraDamage;
        }

        return calcDamage;


    }

    public void DamageTool(ItemStack tool, int totaldamage) {

        ItemMeta toolMeta = tool.getItemMeta();
        Damageable toolDamage = (Damageable) toolMeta;
        assert toolDamage != null;
        int currDamage = toolDamage.getDamage();
        int applyDamage = currDamage + totaldamage;
        toolDamage.setDamage(applyDamage);
        tool.setItemMeta(toolMeta);
    }

}
