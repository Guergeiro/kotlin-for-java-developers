package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val winningBoard = (1..15).map { it }

    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.addNewValue(initializer)
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() =
        board
            .filter { it != null }
            .mapNotNull { it -> board.get(it) }
            .zip(winningBoard)
            .map { it }
            .all { (first, second) -> first == second }

    override fun processMove(direction: Direction) {
        board.moveValues(direction)
    }

    override fun get(
        i: Int,
        j: Int,
    ): Int? = board.run { get(getCell(i, j)) }
}

fun GameBoard<Int?>.addNewValue(initializer: GameOfFifteenInitializer) {
    initializer.initialPermutation.chunked(width).forEachIndexed { i, row ->
        (1..width).forEach { j ->
            this.set(this.getCell(i + 1, j), row.getOrNull(j - 1))
        }
    }
}

fun GameBoard<Int?>.moveValues(direction: Direction) {
    this.filter { it == null }.first().let { emptyCell ->
        emptyCell.getNeighbour(direction.reversed())?.let { neighbour ->
            // swap
            this[emptyCell] = this[neighbour]
            this[neighbour] = null
        }
    }
}
