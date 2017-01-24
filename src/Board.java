import java.util.Observable;

import javax.swing.JOptionPane;


public class Board extends Observable{
   /**used to keep track of the board */
   private int[][] board;
   /**number of rows */ 
   private int rows;
   /**number of columns */ 
   private int cols;
   /** pointers to current open spot in row*/
   private int[] spots;
   /**
    * sets default 8 x 8 2d array
    */
   public Board(){
      board=new int[8][8];
      rows=8;
      cols=8;
      spots=new int[rows];
      clearBoard();
   }
   public Board(int r, int c){} //resizable TBC
   /**
    * add to the board
    * @param r row number to add to
    * @param player_character corresponding character to player
    * @throws BoardException 
    */
   public void add(int r, int player_character) throws BoardException{
      r--;
      if(spots[r] >= cols) throw new BoardException("Cannot use this row");
      board[board[r].length-1-spots[r]][r]=player_character;
      spots[r]++;
      setChanged(); 
      notifyObservers();
      /*for(int i=0;i<8;i++){
         for(int j=0;j<8;j++){
            System.out.print(board[i][j] + " ");
         }
         System.out.println();
      }
      System.out.println("Next");*/
     
   }
   /**
    * clears the board for use to blanks
    */
   public void clearBoard(){
      for(int r = 0; r < rows; r++){
         for(int c = 0; c < cols; c++){
            board[r][c]=0;
         }
         spots[r]=0;
      }
            
      setChanged();
      notifyObservers();
   }
   public int[][] getBoard(){
      return board;
   }
   public int[] getSpots(){
      return spots;
   }
   public Board clone(){
      Board b = new Board();
      for(int i=0;i<board.length;i++){
         for(int j=0;j<board[i].length;j++){
            b.getBoard()[i][j]=board[i][j];
         }
      }
      return b;
   }

}
