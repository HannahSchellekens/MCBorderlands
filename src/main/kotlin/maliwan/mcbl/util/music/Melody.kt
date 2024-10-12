package maliwan.mcbl.util.music

import org.bukkit.Note

/**
 * @author Hannah Schellekens
 */
class Melody private constructor(

    /**
     * Ordered queue with pairs (starting tick, note).
     */
    private val notes: HashMap<Int, Note>,

    /**
     * How long the melody lasts in ticks.
     */
    private var length: Int
) {

    /**
     * The note to
     */
    fun noteAt(tick: Int): Note {
        if (length == 0) error("Empty melody.")

        val index = tick % length
        return notes[index] ?: error("No note at index $index")
    }

    companion object {

        inline fun builder(bpm: Bpm, builder: Builder.() -> Unit): Melody {
            val melodyBuilder = Builder(bpm)
            melodyBuilder.builder()
            return melodyBuilder.build()
        }
    }

    /**
     * Create a new melody.
     *
     * @param bpm Use [Bpm] for a standard tempo. These result in nice whole tick numbers.
     */
    class Builder(bpm: Bpm = Bpm.sluggish) {

        private var currentLength = 0
        private val notes = HashMap<Int, Note>()
        private val ticksPerBar = 1200 / bpm.bpm

        fun build() = Melody(notes, currentLength)

        fun wholeNatural(tone: Note.Tone, octave: Int) = natural(tone, octave, ticksPerBar)
        fun halfNatural(tone: Note.Tone, octave: Int) = natural(tone, octave, ticksPerBar / 2)
        fun quarterNatural(tone: Note.Tone, octave: Int) = natural(tone, octave, ticksPerBar / 4)
        fun eightNatural(tone: Note.Tone, octave: Int) = natural(tone, octave, ticksPerBar / 8)
        fun sixteenthNatural(tone: Note.Tone, octave: Int) = natural(tone, octave, ticksPerBar / 16)
        fun wholeFlat(tone: Note.Tone, octave: Int) = flat(tone, octave, ticksPerBar)
        fun halfFlat(tone: Note.Tone, octave: Int) = flat(tone, octave, ticksPerBar / 2)
        fun quarterFlat(tone: Note.Tone, octave: Int) = flat(tone, octave, ticksPerBar / 4)
        fun eightFlat(tone: Note.Tone, octave: Int) = flat(tone, octave, ticksPerBar / 8)
        fun sixteenthFlat(tone: Note.Tone, octave: Int) = flat(tone, octave, ticksPerBar / 16)
        fun wholeSharp(tone: Note.Tone, octave: Int) = sharp(tone, octave, ticksPerBar)
        fun halfSharp(tone: Note.Tone, octave: Int) = sharp(tone, octave, ticksPerBar / 2)
        fun quarterSharp(tone: Note.Tone, octave: Int) = sharp(tone, octave, ticksPerBar / 4)
        fun eightSharp(tone: Note.Tone, octave: Int) = sharp(tone, octave, ticksPerBar / 8)
        fun sixteenthSharp(tone: Note.Tone, octave: Int) = sharp(tone, octave, ticksPerBar / 16)
        fun wholeRest() = rest(ticksPerBar)
        fun halfRest() = rest(ticksPerBar / 2)
        fun quarterRest() = rest(ticksPerBar / 4)
        fun eightRest() = rest(ticksPerBar / 8)
        fun sixteenthRest() = rest(ticksPerBar / 16)

        fun rest(ticks: Int) {
            currentLength += ticks
        }

        /**
         * Octave is 0, 1 or 2.
         */
        fun natural(tone: Note.Tone, octave: Int, ticks: Int) {
            for (i in currentLength..(currentLength + ticks)) {
                notes[i] = Note.natural(octave, tone)
            }
            currentLength += ticks
        }

        /**
         * Octave is 0, 1 or 2.
         */
        fun flat(tone: Note.Tone, octave: Int, ticks: Int) {
            for (i in currentLength..(currentLength + ticks)) {
                notes[i] = Note.flat(octave, tone)
            }
            currentLength += ticks
        }

        /**
         * Octave is 0, 1 or 2.
         */
        fun sharp(tone: Note.Tone, octave: Int, ticks: Int) {
            for (i in currentLength..(currentLength + ticks)) {
                notes[i] = Note.sharp(octave, tone)
            }
            currentLength += ticks
        }
    }
}