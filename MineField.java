import java.util.Random;
/** 
   MineField
      class with locations of mines for a game.
      This class is mutable, because we sometimes need to change it once it's created.
      mutators: populateMineField, resetEmpty
      includes convenience method to tell the number of mines adjacent to a location.
 */
public class MineField {
    /**
	 * Representation invariant: The number of mines in the 3-arg constructor must be less than 1/3 of total number of possible mines.
	 */
   
   //<instance variables here>
   private boolean [][] mineFieldData;
   private Random randNum;
   private int numMines;
      
/**
    Create a minefield with same dimensions as the given array, and populate it with the mines in the array
    such that if mineData[row][col] is true, then hasMine(row,col) will be true and vice versa.  numMines() for
    this minefield will corresponds to the number of 'true' values in mineData.
    @param mineData  the data for the mines; must have at least one row and one col,
                       and must be rectangular (i.e., every row is the same length)
*/
  public MineField(boolean[][] mineData) {
  mineFieldData = new boolean[mineData.length][mineData[0].length];
  int countMines = 0;   
      for(int i = 0; i < mineData.length; i++) {
    	  for(int j = 0; j< mineData[0].length;j++){
    	     mineFieldData[i][j] = mineData[i][j];
             if(hasMine(i,j)){
             countMines++;
             }    	  
          }
      }
   this.numMines = countMines;  
   }
     
   /**
      Create an empty minefield (i.e. no mines anywhere), that may later have numMines mines (once 
      populateMineField is called on this object).  Until populateMineField is called on such a MineField, 
      numMines() will not correspond to the number of mines currently in the MineField.
      @param numRows  number of rows this minefield will have, must be positive
      @param numCols  number of columns this minefield will have, must be positive
      @param numMines   number of mines this minefield will have,  once we populate it.
      PRE: numRows > 0 and numCols > 0 and 0 <= numMines < (1/3 of total number of field locations). 
    */
   public MineField(int numRows, int numCols, int numMines) {
      mineFieldData = new boolean[numRows][numCols];
      this.numMines = numMines;
      for(int i = 0; i < numRows(); i++) {
    	  for(int j = 0; j< numCols(); j++) {
    		    mineFieldData[i][j] = false;
    	  }
      }
      
   }
 
   /**
      Removes any current mines on the minefield, and puts numMines() mines in random locations on the minefield,
      ensuring that no mine is placed at (row, col).
      @param row the row of the location to avoid placing a mine
      @param col the column of the location to avoid placing a mine
      PRE: inRange(row, col) and numMines() < (1/3 * numRows() * numCols())
    */
   public void populateMineField(int row, int col) {
     // removes current mines in the mineField.
     resetEmpty();
     int count = 0;
     randNum = new Random();
     while(count < numMines()){
          int mineRowPos = randNum.nextInt(numRows());
    	  int mineColPos = randNum.nextInt(numCols());
    	  
    	  if(mineRowPos == row && mineColPos == col || mineFieldData[mineRowPos][mineColPos] == true) {
    		  continue;
    	  } 
    	  else {
    		  mineFieldData[mineRowPos][mineColPos] = true;
    		  count++;
    	  }
     
     }
       
   }
      
   /**
      Reset the minefield to all empty squares.  This does not affect numMines(), numRows() or numCols()
      Thus, after this call, the actual number of mines in the minefield does not match numMines().  
      Note: This is the state a minefield created with the three-arg constructor is in 
         at the beginning of a game.
    */
   public void resetEmpty() {
      for(int i = 0; i < numRows(); i++) {
    	  for(int j = 0; j< numCols(); j++) {
    		    mineFieldData[i][j] = false;
    	  }
      }
   }
   
  /**
     Returns the number of mines adjacent to the specified mine location (not counting a possible 
     mine at (row, col) itself).
     Diagonals are also considered adjacent, so the return value will be in the range [0,8]
     @param row  row of the location to check
     @param col  column of the location to check
     @return  the number of mines adjacent to the square at (row, col)
     PRE: inRange(row, col)
   */
    public int numAdjacentMines(int row, int col) {
    int numAdjMines = 0;
	//top  
    if(inRange(row -1, col) && hasMine(row-1, col)) {
    numAdjMines++;
     }
    //right diagonal 
    if(inRange(row -1, col+1) && hasMine(row-1, col+1)) {
   	 numAdjMines++;
    }  
    // right 
    if(inRange(row, col+1) && hasMine(row, col+1)) {
    numAdjMines++;
    }
    //down right diagonal
    if(inRange(row +1, col+1) && hasMine(row+1, col+1)) {
      	 numAdjMines++;
       }   
    //down 
    if(inRange(row +1, col) && hasMine(row+1, col)) {
         numAdjMines++;
       }  
    //left down diag
    if(inRange(row +1, col-1) && hasMine(row+1, col-1)) {
      	 numAdjMines++;
     } 
    //left
    if(inRange(row, col-1) && hasMine(row, col-1)) {
      	 numAdjMines++;
    } 	 
    //left top diag
    if(inRange(row-1, col-1) && hasMine(row-1, col-1)) {
      	 numAdjMines++;
     }  
    return numAdjMines;       
   }
  
   /**
      Returns true iff (row,col) is a valid field location.  Row numbers and column numbers
      start from 0.
      @param row  row of the location to consider
      @param col  column of the location to consider
      @return whether (row, col) is a valid field location
   */
   public boolean inRange(int row, int col) {
	   if(row >= 0 && row < numRows() && col >= 0 && col < numCols()) {
		   return true;
	   }
      return false;       
   }
   
   /**
      Returns the number of rows in the field.
      @return number of rows in the field
   */  
   public int numRows() {
	  int numRow = mineFieldData.length;
      return numRow;       
   }
   
   /**
      Returns the number of columns in the field.
      @return number of columns in the field
   */    
   public int numCols() {
	  int numCols = mineFieldData[0].length;
      return numCols;       
   }
   
   /**
      Returns whether there is a mine in this square
      @param row  row of the location to check
      @param col  column of the location to check
      @return whether there is a mine in this square
      PRE: inRange(row, col)   
   */    
   public boolean hasMine(int row, int col) {
      if(mineFieldData[row][col]) {
    	  return true;
      } 
	   return false;       
   }
   
   /**
      Returns the number of mines you can have in this minefield.  For mines created with the 3-arg constructor,
      some of the time this value does not match the actual number of mines currently on the field.  See doc for that
      constructor, resetEmpty, and populateMineField for more details.
    * @return
    */
   public int numMines() {
	  return numMines;       
   }
    
}

