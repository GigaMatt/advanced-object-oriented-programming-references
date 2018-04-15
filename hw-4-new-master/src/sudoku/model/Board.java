/*
 * CS 3331
 * Homework 2
 * @author Enrique Salcido
 * @author Matthew S Montoya
 * @author Yoonsik Cheon
 * Purpose: To practice implementing Java Applets & Graphics
 * Last Modified: 4 March 2018
 */

package sudoku.model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** An abstraction of Sudoku puzzle. 
 * @param <E>*/
public class Board{
	
	/** Size of the Sudoku game. */
	public int size;
	
	/** x coordinate of square chosen. */
	public int x;
	
	/** y coordinate of square chosen. */
	public int y;
	
	/** The squares the compose the board. */
    public List<Square> squares;
    
    
    /**
     * Initiate Board
     */
    public Board() {
    	this.size = 9;
    	squares = new ArrayList<Square>(size*size);
    	for(int i = 0; i < size; i++) {
    		for(int j = 0; j < size; j++) {
    			squares.add(new Square(i, j));
    		}
    	}
    }

    
    /**
     * Create ArrayList of n*n squares
     * @param size
     */
    public Board(int size) {
        if(size == 4) {
	    	this.size = size;
	    	squares = new ArrayList<Square>(size*size);
	    	for(int i = 0; i < size; i++) {
	    		for(int j = 0; j < size; j++) {
	    			squares.add(new Square(i, j));
	    		}
	    	}
        }
        else {
        	this.size = 9;
        	squares = new ArrayList<Square>(size*size);
	    	for(int i = 0; i < size; i++) {
	    		for(int j = 0; j < size; j++) {
	    			squares.add(new Square(i, j));
	    		}
	    	}
	    	/*
	    	Set<Integer> startNums = new HashSet(Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8,9}));
	    	int count = 0;
	    	for(Iterator<Integer> i = startNums.iterator(); i.hasNext(); ) {
	    		Integer value = i.next();
	    		this.setEntry(count, count, (int) value);
	    		startNums.remove(value);
	    		count++;
	    	}
	    	
	    	solveBoard(this);
	    	
	    	remove a bunch of cells aka make them equal to zero
	    	
	    	for(int i = 0; i < squares.size(); i++){
	    		if(squares.get(i).value != 0)
	    			squares.get(i).canBeChanged = false;
	    	}
	    	
	    	and now we have a board if some blocks already set in stone
	    	just have to make sure the finalized squares cannot be changed
	    	
	    	*/
        }
    }
    
    private Square getSquare(int i, int j) {
    	return squares.get(i*size + j);
    }
    
    /**
     * Return value of a square
     * @param i
     * @param j
     * @return
     */
    public int getEntry(int i, int j) {
    	return getSquare(i,j).value;
    }
      
    /**
     * Ensure user's desired coordinates are valid
     * @param row
     * @param col
     * @param entry
     * @return
     */
    public boolean validEntry(int row, int col, int entry) {
        if(row >= size || col >= size)
            return false;
        if(entry > size) 
        	return false;
        /*
        if(!this.getSquare(row, col).canBeChanged)//prevents the user from changing a square that is part of the puzzle
        	return false;
        */
        return (rowColCheck(row, col, entry) && boxCheck(row, col, entry));
    }
    
    /**
     * 
     * @param row
     * @param col
     * @param entry
     * @return
     */
    private boolean rowColCheck(int row, int col, int entry) {
        for(int i = 0; i < size; i++) {
        	if (entry == squares.get(size*i + col).value || entry == squares.get(size*row + i).value) {
        		return false;
        	}
        }
        return true;
    }
    
    

    /**
     * Ensure user's coordinate value is valid per sudoku rules
     * @param row
     * @param col
     * @param entry
     * @return
     */
    private boolean boxCheck(int row, int col, int entry) {
    	int rowBound = (int) (row/Math.sqrt(size));
        rowBound = (int) (rowBound * Math.sqrt(size));
        int colBound = (int) (col/Math.sqrt(size));
        colBound = (int) (colBound * Math.sqrt(size));
        
        for(int i = rowBound; i < (rowBound + (int) (Math.sqrt(size))); i++) {
            for(int j = colBound; j < (colBound + (int) (Math.sqrt(size))); j++) {
                if(entry == getEntry(i, j))
                    return false;
            }
        }
        
        return true;
        
    }

    /**
     * Delete coordinate value
     * @param x
     * @param y
     */
    public void deleteEntry(int x, int y){
        squares.get(size*x + y).value = 0;
    }

    /**
     * Insert user's desired value into desired coordinate
     * @param x
     * @param y
     * @param entry
     */
    public void setEntry(int x, int y, int entry) {
        if(entry <= size && entry >= 0) {
        	squares.get(size*x + y).value = entry;
        	squares.get(size*x + y).updateSet(entry);
    	}
    }

    /**
     * Check if sudoku board is solved
     * @return
     */
    public boolean isSolved() {
        for(int i = 0; i < squares.size(); i++)
        	if(squares.get(i).value == 0)
        		return false;
        return true;
    }


	public Set<Integer> getPossibleValues(int x, int y) {
		Set<Integer> s = new HashSet<Integer>();
		s.addAll(Arrays.asList(new Integer[] {1,2,3,4,5,6,7,8}));
		
		for(int i = 0; i < size; i++) {
			if(getEntry(x,i) != 0) {
				s.remove(getEntry(x,i));
			}
		}
		
		if(s.isEmpty())
			return s;
		
		for(int i = 0; i < size; i++) {
			if(getEntry(i,y) != 0) {
				s.remove(getEntry(x,i));
			}
		}
		
		if(s.isEmpty())
			return s;
		
		return s;
		
	}
    
}