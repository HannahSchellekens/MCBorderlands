package maliwan.mcbl.weapons.gun.behaviour

import maliwan.mcbl.weapons.gun.GunProperties
import org.bukkit.ChatColor
import org.bukkit.entity.Player

/**
 * Let the gun send the player a certain message, or `null` to not send a message.
 */
fun Player.sendGunMessage(gun: GunProperties, message: String? = null) {
    if (message == null) {
        return
    }
    sendMessage("${ChatColor.GRAY}${ChatColor.ITALIC}${gun.name}: $message")
}