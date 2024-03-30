package maliwan.mcbl.weapons

import maliwan.mcbl.util.showElementalParticle
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
            if (Elemental.EXPLOSIVE in meta.elements) {
                bullet.location.world?.playEffect(bullet.location.add(0.0, 0.5, 0.0), Effect.SMOKE, 0)
            }
            if (Elemental.INCENDIARY in meta.elements) {
                bullet.location.showElementalParticle(Elemental.INCENDIARY.color, 1, 0.6f)
            }
            if (Elemental.CORROSIVE in meta.elements) {
                bullet.location.showElementalParticle(Elemental.CORROSIVE.color, 1, 0.6f)
            }
            if (Elemental.SHOCK in meta.elements) {
                bullet.location.showElementalParticle(Elemental.SHOCK.color, 1, 0.6f)
            }
            if (Elemental.SLAG in meta.elements) {
                bullet.location.showElementalParticle(Elemental.SLAG.color, 1, 0.6f)
            }
        }
    }
}