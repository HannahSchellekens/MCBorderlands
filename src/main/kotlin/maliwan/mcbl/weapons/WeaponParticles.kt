package maliwan.mcbl.weapons

import maliwan.mcbl.showElementalParticle
import org.bukkit.Color
import org.bukkit.Effect

/**
 * @author Hannah Schellekens
 */
open class WeaponParticles(val weaponHandler: WeaponEventHandler, val everyNticks: Int = 3) {

    /**
     * Counts up for each tick.
     */
    private var particleFrame = 0

    fun tick() {
        particleFrame++
        if (particleFrame++ % everyNticks != 0) {
            return
        }

        weaponHandler.bullets.forEach { (bullet, meta) ->
            if (Elements.EXPLOSIVE in meta.elements) {
                bullet.location.world?.playEffect(bullet.location.add(0.0, 0.5, 0.0), Effect.SMOKE, 0)
            }
            if (Elements.INCENDIARY in meta.elements) {
                bullet.location.showElementalParticle(Elements.INCENDIARY.color, 1, 0.6f)
            }
            if (Elements.CORROSIVE in meta.elements) {
                bullet.location.showElementalParticle(Elements.CORROSIVE.color, 1, 0.6f)
            }
            if (Elements.SHOCK in meta.elements) {
                bullet.location.showElementalParticle(Elements.SHOCK.color, 1, 0.6f)
            }
            if (Elements.SLAG in meta.elements) {
                bullet.location.showElementalParticle(Elements.SLAG.color, 1, 0.6f)
            }
        }
    }
}