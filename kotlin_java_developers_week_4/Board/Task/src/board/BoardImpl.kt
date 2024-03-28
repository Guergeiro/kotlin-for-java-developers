package board

import board.Direction.*

open class SquareBoardImpl(override val width: Int) : SquareBoard {
    private val cells: List<Cell>

    init {
        this.cells =
            List(width * width) { index ->
                val row = index / width
                val col = index % width
                Cell(row + 1, col + 1)
            }
    }

    fun getCellOrNull(cell: Cell): Cell? {
        return this.cells.elementAtOrNull(cell.toIndex(this.width))
    }

    override fun getCellOrNull(
        i: Int,
        j: Int,
    ): Cell? {
        return this.getCellOrNull(Cell(i, j))
    }

    override fun getCell(
        i: Int,
        j: Int,
    ): Cell {
        val element = this.getCellOrNull(i, j)
        if (element == null) {
            throw IllegalArgumentException()
        }
        return element
    }

    fun getCell(cell: Cell): Cell {
        val element = this.getCellOrNull(cell)
        if (element == null) {
            throw IllegalArgumentException()
        }
        return element
    }

    override fun getAllCells(): Collection<Cell> {
        return this.cells
    }

    override fun getRow(
        i: Int,
        jRange: IntProgression,
    ): List<Cell> {
        val rows = this.cells.filter { it.i == i }
        return jRange.mapNotNull { it ->
            rows.elementAtOrNull(it - 1)
        }
    }

    override fun getColumn(
        iRange: IntProgression,
        j: Int,
    ): List<Cell> {
        val cols = this.cells.filter { it.j == j }
        return iRange.mapNotNull { it ->
            cols.elementAtOrNull(it - 1)
        }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        val tempCell =
            when (direction) {
                UP -> Cell(this.i - 1, this.j)
                DOWN -> Cell(this.i + 1, this.j)
                RIGHT -> Cell(this.i, this.j + 1)
                LEFT -> Cell(this.i, this.j - 1)
            }
        return getCellOrNull(tempCell)
    }

    fun Cell.toIndex(width: Int): Int {
        return (i - 1) * width + (j - 1)
    }
}

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

class GameBoardImpl<T>(size: Int) : SquareBoardImpl(size), GameBoard<T> {
    private val values = mutableMapOf<Cell, T?>()

    init {
        super.getAllCells().forEach {
            this.set(it, null)
        }
    }

    override fun get(cell: Cell): T? {
        return this.values.get(cell)
    }

    override fun set(
        cell: Cell,
        value: T?,
    ) {
        this.values.put(cell, value)
    }

    override fun filter(predicate: Function1<T?, Boolean>): Collection<Cell> {
        return this.values.filterValues(predicate).map { it.key }
    }

    override fun find(predicate: Function1<T?, Boolean>): Cell? {
        return this.filter(predicate).firstOrNull()
    }

    override fun any(predicate: Function1<T?, Boolean>): Boolean {
        return this.filter(predicate).size > 0
    }

    override fun all(predicate: Function1<T?, Boolean>): Boolean {
        return this.filter(predicate).size == super.getAllCells().size
    }
}

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl<T>(width)
