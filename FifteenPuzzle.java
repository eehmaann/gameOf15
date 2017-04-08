//  FifteenPuzzle.java

/****
 *  This program is a puzzle game that the user can play.  It will create a board that
 *	has the numbers 1-15 and then a blank space.  The user can shuffle the pieces of the 
 *  the board, and then the objective is to return the board to the original pattern.
 *
 *  @author   Eric Ehmann
 *  @version  1.0   Last modified 4/7/17
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;


public class FifteenPuzzle extends JFrame implements ActionListener {
    private static final int HEIGHT=350;
    private static final int WIDTH= 300;
    private static final int SIZE=4;
    private JButton[][] tile = new JButton[4][4];
    private JButton shuffleButton;
    private JButton quitButton;
    private JPanel  pieces = new JPanel ();
    private JPanel commands = new JPanel();
    private String [] answerArray = new String [16];

	/**
		*  This is the construction method.  It will create the puzzle
		*
		*/  
    public FifteenPuzzle() {
        setTitle("Puzzle");
        setSize (HEIGHT, WIDTH);
        setLocation (200,200);
        setDefaultCloseOperation ( EXIT_ON_CLOSE); 
	
        
        pieces.setLayout (new GridLayout (SIZE, SIZE, 3, 3) );
      	
        labelTiles();
        formatTiles( pieces);
        setAnswers();
        
        formatCommandsPanel(commands);
      	
        JPanel board = new JPanel();
        formatBoardPanel(board, commands);
	}

    public void labelTiles() {
        int idx = 0;
        // number the tiles
        for(int i=0; i<4; i++) {
            for(int j=0; j<4; j++) {
                tile[i][j] = new JButton( ""+(idx+1));
                idx++;
            }
        }
        // leave one tile blank
        tile[3][3].setText("");
                
    }
    public void formatTiles(JPanel pieces) {
        for (JButton [] row : tile)
            for(JButton i :row) {
                i.setFont (new Font ("Times", Font.BOLD, 24));
                i.setHorizontalAlignment(JButton.CENTER);
                i.setPreferredSize(new Dimension(100,100));
                pieces.add(i);
                i.addActionListener ( FifteenPuzzle.this );
            }
    }

    public void setAnswers() {

        for (int i=0; i<15; i++) { 
            answerArray[i]= ""+(i+1);
        } 
        answerArray[15]="";
    }

    public void formatCommandsPanel(JPanel commands) {
        
        shuffleButton = new JButton ("Shuffle");
        shuffleButton.addActionListener (FifteenPuzzle.this);
        quitButton = new JButton ("Quit");
        quitButton.addActionListener(FifteenPuzzle.this);
        commands.setLayout (new GridLayout (0,2));
        commands.add(shuffleButton);
        commands.add(quitButton);
    }

    public void formatBoardPanel(JPanel board, JPanel commands) {
        board.setLayout (new BorderLayout());
        getContentPane().add( pieces);
        board.add(pieces, BorderLayout.CENTER);
        getContentPane().add( commands);
        board.add(commands, BorderLayout.SOUTH);
        getContentPane().add( board );
        setVisible(true);
    }
	/**
		* This method will control what happens if a button is pressed.
		*
		*	@param e  the button that was pressed
		*/
    public void actionPerformed( ActionEvent e )
    {
    	for (int i=0; i<4; i++){
    		for (int j=0; j<4; j++){ 
         		if (e.getSource()== tile[i][j]){ 
         			doMove(i,j);
         		}        	
        	}
        }
        if (e.getSource()==quitButton) {
        	System.exit(0);        	
        }
        if (e.getSource()==shuffleButton) {
        	shuffle();
        }
        
         	
    }
    	
    /**
    	*	After a button with a number on it is pressed, this method will test whether
    	*   it is a legal move.  If it is then it will switch the tiles, and call 
    	*	a method to check for a winning condition
    	*
    	* @param int i		the row of the piece being checked
    	* @param int j		the column of the piece being checked
    	*/
    public void doMove(int i, int j)
    {
    	if(i>0) {
         //Test North
            if(tile[i-1][j].getText().equals("")){
                swapTiles(i,j, i-1,j);
            }
        }
        
        if(i<3) {
             //Test South
            if(tile[i+1][j].getText().equals("")){
            	swapTiles(i,j, i+1,j);
                            
            }
        }
        
        if(j>0){
            //Test West
            if(tile[i][j-1].getText().equals("")){
                swapTiles(i,j, i,j-1);
            }
         }

        if(j<3){
            //Test East
            if(tile[i][j+1].getText().equals("")){
                swapTiles(i,j, i,j+1);
            }
         }
           repaint();

         if(isSolved()){
        	 	JOptionPane.showMessageDialog(FifteenPuzzle.this, "Solved!");
        	 	System.exit(0);
    	}
    }
    /**
    	*  This method swaps a tile with a number on it with the blank tile
    	*	It will change the label of the blank on to the new number, and it will
    	*  make the new new button blank
    	*
    	*@oaram 	firstRow 	the row of the tile with the number on it
    	*@param		firstCol	the column of the tile with the number on it
    	*@param		secondRow	the row of the blank tile
    	*@param		secondCol	the column of the blank tile
    	*/ 
     private void swapTiles(int firstRow, int firstCol, int secondRow, int secondCol){
        tile[secondRow][secondCol].setText(tile[firstRow][firstCol].getText());
        tile[firstRow][firstCol].setText("");
     }
     /**
     	* This method will check whether the puzzle has been solved.
     	* 
     	*@return 	boolean	true if the puzzle has been solved.
     	*/
    private boolean isSolved(){
        int arrayPlace = 0;
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                String text = tile[i][j].getText();
                if (! text.equals(answerArray[arrayPlace]) ){
                    return false;
                }
                arrayPlace++;
            }
        }
        return true;
    }      
    /** 
    	* This method calls for the shuffling to be done and will keep count of the times
    	* the shuffle has happened.  It will find the location of the blank tile
    	* and pass that to the blankChange method
    	*
    	*/		
     private void shuffle(){
     	for(int turns=0; turns<500; turns++){
     		for(int i=0; i<4; i++){
                for(int j=0; j<4; j++){
                 	if (tile[i][j].getText().equals("")){
                 		blankChange(i, j);                 		
                 	}
                }
     		}
     		repaint();
     	}
     }
     /**
     *  This method does the actual process of the shuffle through taking the blank tile,
     *  and changing it with an adjacent tile randomly.  
     */
	public void blankChange (int i, int j){
		// choose swap across rows or swap across columns
		int path = (int) (2 * Math.random());
		if (path==0) // move based on i thus vertical
		{
			if (i==0){
				//can only move down
				swapTiles(i+1, j, i, j);
			}
			else if (i==1||i==2){
				//can move in either direction
				int direction = (int) (2 * Math.random());
				if (direction==1){
					swapTiles(i+1, j, i, j);
				}
                else{
                    swapTiles(i-1, j, i, j);
                }
			}
			else{
                    swapTiles(i-1, j, i, j);
                }
			}
		else //path==1 movement based on j thus horizontal
		{ 
			if(j==0){
				swapTiles(1, j+1, i, j);
			}
			else if (j==1||j==2){
				//can move in either direction
				int direction = (int) (2 * Math.random());
				if (direction==1){
					swapTiles(i, j+1, i, j);					
				}
				else{
					swapTiles(i, j-1, i, j);
				}
			}

			else{
					swapTiles(i, j-1, i, j);
				}
			}
		}

    public static void main(String[]arg){
       FifteenPuzzle f =  new FifteenPuzzle();
    }
 }