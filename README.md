<div align="center">

![GitHub](https://img.shields.io/github/license/NaufalFitri/Lipo-TreeCapitator?style=flat-square)
[![Join us on Discord](https://img.shields.io/discord/1040175857462943788.svg?label=&logo=discord&logoColor=ffffff&color=7389D8&labelColor=6A7EC2&style=flat-square)](https://discord.com/invite/4DtDBGtST4)

</div>

<h2 align="center">
  <img src="lipomc.png" alt="LipoMC Logo" width="400">
  <br>
    <br>
  A Plugin Developed by LipoTeam
    <br>
    <h4 align="center">
    This plugin is licensed under the GNU General Public License v3.0. See the LICENSE file for details.
    </h4>
</h2>

**Compatible Minecraft Server Versions**

- Our plugin has been tested on Minecraft version 1.21.1, but it should be compatible with server versions up to 1.21.3.

---

## Offers

Our TreeCapitator plugin offers a various configuration options for server owners 
to customize the configuration file to their preferences.

**Configuration Options Include**

- Enable or Disable TreeCapitator in certain worlds.
- Enable or disable particle effects when a tree is chopped down.
- Customize the particle characteristics.
- Enable or disable delays for breaking each log.
- Customize the characteristics of the delay.
- Enable or disable 'Break After Particle' (BAP), which causes the tree to be chopped down instantly after the particle effect finishes.
- Enable or disable custom damage for tools.
- Customize custom damage for tools.
- Enable or disable the requirement for players to sneak in order to chop down a tree.
---
## Configuration
<details>
<summary><b>Default</b></summary>

```
# The prefix for the plugin
prefix : "&8[&9&l!&8] &9&lTreeCapitator &8»&f"

TreeCapitator:
  # Use to enable/disable the usage of tree capitator
  enabled: true
  # [BETA] use asynchronous mode for collecting log
  async: false
  # The break delay for each log
  delay:
    # Enable/Disable delay for breaking the log (not for async breaking the log)
    enabled: true
    # The delay time foreach blocks collection/breaking
    time: 1
    # BAP stands for Break After Particle
    BAP: true

  particle:
    # Enable/Disable particle when the Capitator is running
    enabled: true
    # Percentage of particle to be spawn (Depends on liking)
    percentage: 65
    # Type of particle to be used, you can check here https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html
    type: CAMPFIRE_COSY_SMOKE
    # Amount of particle to be spawns, better set to low value so that low-end player will not experience lag
    amount: 5
    # The velocity of the particle spread, value is in points
    speed: 0.1
    # The Sound for particle
    sound:
      # Enable/Disable sound for particle
      enabled: true
      # The name of sound for the particle, you can check here https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Sound.html
      name: BLOCK_AZALEA_LEAVES_BREAK

  damage:
    # Enable/Disable damage to be applied to the tools
    enabled: true
    # Enable/Disable if you want unbreaking to influence the damage taken by the tools
    unbreakingInfluence: true
    # Added extradamage for every damage taken by the tools
    extraDamage: 1

  # You can set which world can player use treecapitator, set worlds: [] to apply on all worlds
  worlds:
    - world

    # Enable/Disable for the player to sneak to break the logs
  needsneak: true
```

</details>


