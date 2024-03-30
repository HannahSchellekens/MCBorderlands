package maliwan.mcbl.util

import java.io.InputStreamReader
import kotlin.math.max
import kotlin.reflect.KClass

/**
 * Parse basic CSV tables separated by tabs and newlines.
 *
 * Format is:
 * - Top left cell is ignored and can be used for information.
 * - Top row and left column contain headings.
 * - The rest is content.
 * - Split by tabs and newlines.
 *
 * @param Col
 *          The type of the column header.
 * @param Row
 *          The type of the row header.
 * @param Cell
 *          The type of cell values.
 *
 * @author Hannah Schellekens
 */
open class TabTable<Col, Row, Cell>(

    /**
     * The full Csv Table contents.
     */
    tableString: String,

    /**
     * How to parse the column headers.
     */
    columnTransform: (String) -> Col,

    /**
     * How to parse the row headers.
     */
    rowTransform: (String) -> Row,

    /**
     * How to parse the cell values.
     */
    cellTransform: (String) -> Cell,
) {

    /**
     * All lines of the input text: rows of columns (row major).
     */
    private val inputCells: List<List<String>> = tableString.split("\n").map { it.split("\t") }

    /**
     * All cells in this table in row major.
     */
    val cells: List<List<Cell>> = inputCells.drop(1).map { line ->
        line.drop(1).map { cellTransform(it.trim()) }
    }

    /**
     * All column header values.
     */
    val columns: List<Col> = if (inputCells.isEmpty()) {
        emptyList()
    }
    else inputCells.first().drop(1).map { columnTransform(it.trim()) }

    /**
     * All row header values.
     */
    val rows: List<Row> = inputCells.drop(1).map { row ->
        rowTransform(row.firstOrNull()?.trim() ?: "")
    }

    /**
     * Get the element in the table from column with header `column` and row with header `row`.
     */
    operator fun get(column: Col, row: Row): Cell? {
        val colIndex = columns.indexOf(column)
        if (colIndex < 0) {
            return null
        }

        val rowIndex = rows.indexOf(row)
        if (rowIndex < 0) {
            return null
        }

        return cells[rowIndex][colIndex]
    }

    override fun toString(): String {
        val cellMax = cells.maxOf { it.maxOfOrNull { b -> b?.toString()?.length ?: 0 } ?: 0 }
        val colMax = columns.maxOf { it.toString().length }
        val rowMax = rows.maxOf { it.toString().length }
        val maxLength = max(cellMax, max(colMax, rowMax))

        return buildString {
            // Column header
            append(" ".repeat(maxLength + 1))
            append(" | ")
            append(columns.joinToString(" | ") { "%${maxLength}s".format(it) })
            append("\n")

            // Horizontal line
            val width = columns.size + 1
            repeat(width) {
                append("-".repeat(maxLength + 2))
                if (it < width - 1) {
                    append("+")
                }
            }
            append("\n")

            // Rows.
            for (i in rows.indices) {
                append("%${maxLength + 1}s".format(rows[i]))
                append(" | ")
                append(cells[i].joinToString(" | ") { "%${maxLength}s".format(it) })
                append( "\n")
            }
        }
    }

    companion object {

        /**
         * Create a new CsvTable from the contents of the given classpath resource.
         *
         * @param resourcePath
         *          The path to the resource on the classpath.
         * @param relativeTo
         *          Relative to which class to look for the resource.
         * @param Col
         *          The type of the column header.
         * @param Row
         *          The type of the row header.
         * @param Cell
         *          The type of cell values.
         * @return CsvTable read from this resource.
         * @throws IllegalStateException When the resource could not be found.
         */
        fun <Col, Row, Cell> fromResource(
            resourcePath: String,
            columnTransform: (String) -> Col,
            rowTransform: (String) -> Row,
            cellTransform: (String) -> Cell,
            relativeTo: KClass<*> = TabTable::class
        ): TabTable<Col, Row, Cell> {
            val inStream = relativeTo.java.getResourceAsStream(resourcePath)
                ?: error("No resource found <$resourcePath> relative to <${relativeTo.qualifiedName}>")
            val lines = InputStreamReader(inStream).use { it.readText() }
            return TabTable<Col, Row, Cell>(lines, columnTransform, rowTransform, cellTransform)
        }
    }
}