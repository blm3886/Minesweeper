/**
 * VisibleField class This is the data that's being displayed at any one point
 * in the game (i.e., visible field, because it's what the user can see about
 * the minefield). Client can call getStatus(row, col) for any square. It
 * actually has data about the whole current state of the game, including the
 * underlying mine field (getMineField()). Other accessors related to game
 * status: numMinesLeft(), isGameOver(). It also has mutators related to actions
 * the player could do (resetGameDisplay(), cycleGuess(), uncover()), and
 * changes the game state accordingly.
 * 
 * It, along with the MineField (accessible in mineField instance variable),
 * forms the Model for the game application, whereas GameBoardPanel is the View
 * and Controller, in the MVC design pattern. It contains the MineField that
 * it's partially displaying. That MineField can be accessed (or modified) from
 * outside this class via the getMineField accessor.
 */
public class VisibleField {
   /**
	 * Representation invariant: The mineFieldState array in the VisisbleField Class represents the exact state of the 
     *                           underlyingMineField after each operation.
	 */
   
	// ----------------------------------------------------------
	// The following public constants (plus numbers mentioned in comments below) are
	// the possible states of one
	// location (a "square") in the visible field (all are values that can be
	// returned by public method
	// getStatus(row, col)).

	// The following are the covered states (all negative values):
	public static final int COVERED = -1; // initial value of all squares
	public static final int MINE_GUESS = -2;
	public static final int QUESTION = -3;

	// The following are the uncovered states (all non-negative values):

	// values in the range [0,8] corresponds to number of mines adjacent to this
	// square
	public static final int NO_ADJACENT_MINE = 0;

	public static final int MINE = 9; // this loc is a mine that hasn't been guessed already (end of losing game)
	public static final int INCORRECT_GUESS = 10; // is displayed a specific way at the end of losing game
	public static final int EXPLODED_MINE = 11; // the one you uncovered by mistake (that caused you to lose)
	// ----------------------------------------------------------
   
   /**
     * @param numMinesLeft         Represents the numbeer of mines left in the mineField.
     * @param mineFieldState       Represenst the visible field of the underlying mineField.
     * @param underlyingMineField  The mineFiled object which has all the information for the mineField.
     * @param visited              A boolean 2D array in which num of rows and cols are same as the MineField,
                                   that keeps track of the visited squares during floodfill, 
                                   this array is used inside 2 methods.
     */
   
   
	// <instance variables here>
	// private int numRows;
	// private int numCols;
	private int numMinesLeft;
	private int[][] mineFieldState;
	private MineField underlyingMineField;
	private boolean[][] visited;

	/**
	 * Create a visible field that has the given underlying mineField. The initial
	 * state will have all the mines covered up, no mines guessed, and the game not
	 * over.
	 * 
	 * @param mineField the minefield to use for for this VisibleField
	 */
	public VisibleField(MineField mineField){
		underlyingMineField = mineField;
		this.numMinesLeft = underlyingMineField.numMines();
		mineFieldState = new int[underlyingMineField.numRows()][underlyingMineField.numCols()];
		// initial status (all mines COVERED)
		for (int i = 0; i < underlyingMineField.numRows(); i++){
			for (int j = 0; j < underlyingMineField.numCols(); j++){
				mineFieldState[i][j] = COVERED;
			}
		}

	}

	/**
	 * Reset the object to its initial state (see constructor comments), using the
	 * same underlying MineField.
	 */
	public void resetGameDisplay(){
		for (int i = 0; i < underlyingMineField.numRows(); i++){
			for (int j = 0; j < underlyingMineField.numCols(); j++){
				mineFieldState[i][j] = COVERED;
			}
		}
		this.numMinesLeft = underlyingMineField.numMines();
	}

	/**
	 * Returns a reference to the mineField that this VisibleField "covers"
	 * 
	 * @return the minefield
	 */
	public MineField getMineField(){
		return underlyingMineField; 
	}

	/**
	 * Returns the visible status of the square indicated.
	 * 
	 * @param row row of the square
	 * @param col col of the square
	 * @return the status of the square at location (row, col). See the public
	 *         constants at the beginning of the class for the possible values that
	 *         may be returned, and their meanings. PRE: getMineField().inRange(row,
	 *         col)
	 */
	public int getStatus(int row, int col){
		return mineFieldState[row][col];
	}

	/**
	 * Returns the the number of mines left to guess. This has nothing to do with
	 * whether the mines guessed are correct or not. Just gives the user an
	 * indication of how many more mines the user might want to guess. This value
	 * can be negative, if they have guessed more than the number of mines in the
	 * minefield.
	 * 
	 * @return the number of mines left to guess.
	 */
	public int numMinesLeft() {
		int numsMinesGuessed = 0;
		for (int i = 0; i < underlyingMineField.numRows(); i++){
			for (int j = 0; j < underlyingMineField.numCols(); j++){
				if (getStatus(i, j) == MINE_GUESS) {
					numsMinesGuessed++;
				}
			}
		}
		return numMinesLeft - numsMinesGuessed;
	}

	/**
	 * Cycles through covered states for a square, updating number of guesses as
	 * necessary. Call on a COVERED square changes its status to MINE_GUESS; call on
	 * a MINE_GUESS square changes it to QUESTION; call on a QUESTION square changes
	 * it to COVERED again; call on an uncovered square has no effect.
	 * 
	 * @param row row of the square
	 * @param col col of the square PRE: getMineField().inRange(row, col)
	 */
	public void cycleGuess(int row, int col){
		if (getStatus(row, col) == COVERED){
			mineFieldState[row][col] = MINE_GUESS;

		} else if (getStatus(row, col) == MINE_GUESS){
			mineFieldState[row][col] = QUESTION;
		

		} else if (getStatus(row, col) == QUESTION){
			mineFieldState[row][col] = COVERED;
		}
	}

	/**
	 * Uncovers this square and returns false iff you uncover a mine here. If the
	 * square wasn't a mine or adjacent to a mine it also uncovers all the squares
	 * in the neighboring area that are also not next to any mines, possibly
	 * uncovering a large region. Any mine-adjacent squares you reach will also be
	 * uncovered, and form (possibly along with parts of the edge of the whole
	 * field) the boundary of this region. Does not uncover, or keep searching
	 * through, squares that have the status MINE_GUESS. Note: this action may cause
	 * the game to end: either in a win (opened all the non-mine squares) or a loss
	 * (opened a mine).
	 * 
	 * @param row of the square
	 * @param col of the square
	 * @return false iff you uncover a mine at (row, col) PRE:
	 *         getMineField().inRange(row, col)
	 */
	public boolean uncover(int row, int col) {
	boolean gameStatus = true;	
		if (getMineField().hasMine(row, col)) {
			mineFieldState[row][col] = EXPLODED_MINE;
			gameStatus = false;
		} else if (getMineField().numAdjacentMines(row, col) > NO_ADJACENT_MINE) {
			mineFieldState[row][col] = getMineField().numAdjacentMines(row, col);
		} else if (getMineField().numAdjacentMines(row, col) == NO_ADJACENT_MINE) {
			visited = new boolean[underlyingMineField.numRows()][underlyingMineField.numCols()];
			uncoverEmptyRegions(row, col);
		}
		return gameStatus;

	}

	/**
	 * Returns whether the game is over. (Note: This is not a mutator.)
	 * @return whether game over
	 */
	public boolean isGameOver() {
		boolean gameEnd = false;
		int numSqrChecked = 0;
		int totNumSqr = underlyingMineField.numCols() * underlyingMineField.numRows();

		for (int i = 0; i < underlyingMineField.numRows(); i++) {

			for (int j = 0; j < underlyingMineField.numCols(); j++) {
				if (getStatus(i, j) == EXPLODED_MINE) {
					gameEnd = true;
					gameLostDisplay();
				}
				else if(isUncovered(i, j)){
				numSqrChecked++;
				}			
			}
		}
		if ((totNumSqr - numSqrChecked) == underlyingMineField.numMines()) {
			gameEnd = true;
			gameWinDisplay();
		}

		return gameEnd; 
	}

	/**
	 * Returns whether this square has been uncovered. (i.e., is in any one of the
	 * uncovered states, vs. any one of the covered states).
	 * 
	 * @param row of the square
	 * @param col of the square
	 * @return whether the square is uncovered PRE: getMineField().inRange(row, col)
	 */
	public boolean isUncovered(int row, int col) {
		if (getStatus(row, col) > COVERED) {
			return true;
		} else {
			return false;
		}
	}

	// <private methods here>
	/**
	 * Uncovers all the empty regions without any mine using the recursive flood fill algorithm.
	 */
	private void uncoverEmptyRegions(int row, int col) {
		if (isGameOver() || !underlyingMineField.inRange(row, col) || getStatus(row, col) == MINE_GUESS
				|| isUncovered(row, col) || visited[row][col] == true) {
			return;
		} else if (underlyingMineField.numAdjacentMines(row, col) > NO_ADJACENT_MINE) {
			mineFieldState[row][col] = getMineField().numAdjacentMines(row, col);
			return;
		}
		visited[row][col] = true;
		uncoverEmptyRegions(row - 1, col); // top
		uncoverEmptyRegions(row - 1, col + 1); // top-right
		uncoverEmptyRegions(row - 1, col + 1); // top-left
		uncoverEmptyRegions(row, col - 1); // left
		uncoverEmptyRegions(row + 1, col); // down
		uncoverEmptyRegions(row + 1, col + 1); // down-right
		uncoverEmptyRegions(row + 1, col - 1); // down-left
		uncoverEmptyRegions(row, col + 1); // right
		mineFieldState[row][col] = NO_ADJACENT_MINE;
		visited[row][col] = false;

	}

	/**
	 * Sets the winning game display.
	 * changes the state of all the uncovered mines to MINE_GUESS.
	 */
	private void gameWinDisplay() {
		for (int i = 0; i < getMineField().numRows(); i++) {
			for (int j = 0; j < underlyingMineField.numCols(); j++) {
			    if (underlyingMineField.hasMine(i, j)) {
					mineFieldState[i][j] = MINE_GUESS;
				}
			}
		}
	}

	/**
	 * Sets the Losing  game display.
	 * Changes the state of all the uncovered mines or mines with question mark to a  MINE.
	 * Sets the mines guessed wrong to INCORRECT_GUESS
	 */
	private void gameLostDisplay() {
		for (int i = 0; i < underlyingMineField.numRows(); i++) {
			for (int j = 0; j < underlyingMineField.numCols(); j++) {
				if ((mineFieldState[i][j] == COVERED || mineFieldState[i][j] == QUESTION)
						&& underlyingMineField.hasMine(i, j)) {
					mineFieldState[i][j] = MINE;
				} else if ((mineFieldState[i][j] == MINE_GUESS) && (!underlyingMineField.hasMine(i, j))) {
					mineFieldState[i][j] = INCORRECT_GUESS;
		     	}
			}
		}
	}
}
