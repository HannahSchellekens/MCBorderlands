package maliwan.mcbl.gui

import maliwan.mcbl.MCBorderlandsPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import kotlin.math.max
import kotlin.math.min

/**
 * @author Hannah Schellekens
 */
open class Hud(val plugin: MCBorderlandsPlugin) : Listener, Runnable {

    private val bossBars = HashMap<Player, BossBar>()

    /**
     * Shows the text message that displays the ammo status of the gun that the player is currently holding.
     * `null` when there is no applicable ammo context (e.g. no gun held).
     *
     * @return (text above, left in clip, clip size).
     */
    fun gunData(player: Player): Triple<String, Int, Int>? {
        val gunExecution = plugin.weaponEventHandler.obtainGunExecutionFromInventory(player) ?: return null
        val inventory = plugin.inventoryManager[player]

        val gunType = gunExecution.weaponClass.displayName
        val totalAmmo = inventory[gunExecution.weaponClass]
        val leftInClip = min(gunExecution.clip, totalAmmo)
        val ammoLeft = max(0, totalAmmo - leftInClip)

        val element = gunExecution.elements.sorted().joinToString("") {
            it.chatColor + it.symbol
        }
        val space = if (gunExecution.elements.isEmpty()) "" else " "
        val colour = gunExecution.rarity.colourPrefix
        val branding = "${ChatColor.GRAY}$element$space$colour${gunExecution.manufacturer.displayName}"

        return Triple(
            "${ChatColor.GRAY}$gunType: ${ChatColor.WHITE}$leftInClip ${ChatColor.GRAY}/ $ammoLeft • $branding".trimEnd(),
            leftInClip,
            gunExecution.magazineSize
        )
    }

    fun updateTopBar(player: Player) {
        val (message, clipSize, totalClip) = gunData(player) ?: run {
            showTopBar(player, false)
            return
        }

        val reloadProgress = plugin.weaponEventHandler.reloadProgress(player)
        if (reloadProgress != null) {
            updateReload(player, message, reloadProgress)
        }
        else {
            updateAmmoClipText(player, message, clipSize, totalClip)
        }
    }

    fun updateAmmoClipText(player: Player, message: String, clipSize: Int, totalClip: Int) {
        val progress = clipSize / totalClip.toDouble()
        bossBars[player]?.let {
            it.setTitle(message)
            it.progress = if (progress > 0.999) 1.0 else progress
            it.color = BarColor.WHITE
        }
    }

    fun updateReload(player: Player, message: String, progress: Double) {
        bossBars[player]?.let {
            it.setTitle(message)
            it.progress = progress
            it.color = BarColor.RED
        }
    }

    fun showTopBar(player: Player, show: Boolean = true) {
        val bar = bossBars[player] ?: createTopBar(player)
        bar.isVisible = show
    }

    fun createTopBar(player: Player): BossBar {
        val bossBar = Bukkit.createBossBar(null, BarColor.WHITE, BarStyle.SOLID)
        bossBar.addPlayer(player)
        bossBars[player] = bossBar
        return bossBar
    }

    fun clear(player: Player) {
        bossBars.remove(player)?.isVisible = false
    }

    fun clearAll() {
        bossBars.forEach { (_, bar) -> bar.isVisible = false }
        bossBars.clear()
    }

    override fun run() {
        Bukkit.getOnlinePlayers().forEach {
            showTopBar(it)
            updateTopBar(it)
        }
    }

    @EventHandler
    fun playerLeave(event: PlayerQuitEvent) {
        clear(event.player)
    }
}