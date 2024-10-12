package maliwan.mcbl.util.music.melody

import maliwan.mcbl.util.music.Bpm
import maliwan.mcbl.util.music.Melody
import org.bukkit.Note.Tone.*

/**
 * We are number one, hey!
 */
val MELODY_WE_ARE_NUMBER_ONE = Melody.builder(Bpm.fast) {
    val base = 0
    
    quarterNatural(D, base)
    eightRest()
    eightNatural(A, base + 1)
    sixteenthSharp(G, base)
    sixteenthNatural(A, base + 1)
    sixteenthSharp(G, base)
    sixteenthNatural(A, base + 1)
    quarterSharp(G, base)
    quarterNatural(A, base + 1)

    quarterNatural(F, base)
    quarterNatural(D, base)
    eightRest()
    eightNatural(D, base)
    eightNatural(F, base)
    eightNatural(A, base)

    quarterFlat(B, base + 1)
    quarterNatural(F, base)
    quarterFlat(B, base + 1)
    quarterNatural(C, base + 1)

    eightNatural(A, base + 1)
    eightSharp(A, base + 1)
    eightNatural(A, base + 1)
    eightSharp(A, base + 1)
    eightNatural(A, base + 1)
    eightRest()
    quarterNatural(D, base)
}