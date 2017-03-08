import java.util.Random;


public class Game {
   /** the board of the game*/
   private Board board;
   /**first player*/
   private Player player_one;
   /** second player*/
   private Player player_two;
   /**how many you need to win game*/
   private int number_to_win;
   /**the superclass used for user interaction*/
   private Interface face;
   /** boolean whether player two is a computer*/
   private boolean computer_playing;
   /**
    * 
    */
   public Game(){
      player_one=new Player(1);
      player_two=new Player(2);
<<<<<<< HEAD
      //number_to_win=5; //testing
=======
      /*value hard coded in. Option can be made to change this. */
      number_to_win=5;
>>>>>>> origin/master
      board=new Board();
      face=new GUI(this, board);
      face.ask_the_question();
      number_to_win=face.getNumberToWin();
      computer_playing=face.getComputer();
      if(computer_playing){
         player_one.setActive();
         player_two.setInactive();
      }
      else{
         Random r=new Random();
         if(r.nextBoolean()) {
            player_one.setActive();
            player_two.setInactive();
         }
         else {
            player_two.setActive();
            player_one.setInactive();
         }
      }
      
   }
  
  
   public boolean win(Board b){
      int[][] board_copy=b.getBoard();
      for(int i=0;i<board.getBoard().length;i++){
         for(int j=0;j<board.getBoard().length;j++){
            //right
            int count_player_one=0;
            int count_player_two=0;
            for(int num=0;num<number_to_win;num++){
               if(i+num < board.getBoard().length && board_copy[i+num][j] == 1 ) count_player_one++;
               if(i+num < board.getBoard().length && board_copy[i+num][j] == 2 ) count_player_two++;
            }
            if(count_player_one == number_to_win || count_player_two == number_to_win ) return true;
            //right-down
            count_player_one=0;
            count_player_two=0;
            for(int num=0;num<number_to_win;num++){
               if(j-num >= 0 && i+num < board.getBoard().length && board_copy[i+num][j-num] == 1 ) count_player_one++;
               if(j-num >= 0 && i+num < board.getBoard().length && board_copy[i+num][j-num] == 2 ) count_player_two++;
            }
            if(count_player_one == number_to_win || count_player_two == number_to_win ) return true;
            //down
            count_player_one=0;
            count_player_two=0;
            for(int num=0;num<number_to_win;num++){
               if(j-num >= 0 && board_copy[i][j-num] == 1 ) count_player_one++;
               if(j-num  >= 0  && board_copy[i][j-num] == 2 ) count_player_two++;
            }
            if(count_player_one == number_to_win || count_player_two == number_to_win ) {
               return true;
            }
               
         }
      }
      return false;
      
      
      
   }
   protected void addToBoard(int r) throws BoardException{
      Board temp=board.clone();
      if(face.chooseAnswer(face.loadQuestion())) {
         temp.add(r, getActivePlayer().get_player_id());
         board.add(r, getActivePlayer().get_player_id());
      }
      else {
         temp.add(r, getOtherPlayer().get_player_id());
         if(!win(temp))
            board.add(r, getOtherPlayer().get_player_id());
      }
      if(!computer_playing){
         if(player_one.getActive()) {
            player_two.setActive();
            player_one.setInactive();
         }
         else{
            player_one.setActive();
            player_two.setInactive();
         }
      }
      else {
         if(win(temp)) return;
         Random rand= new Random();
         int choice=rand.nextInt(8)+1;
         if(rand.nextBoolean()) {
            face.sendOutMsg("\nComputer right, they get credit!");
            board.add(choice, getOtherPlayer().get_player_id());
         }
         else {
            temp.add(r, getOtherPlayer().get_player_id());
            if(!win(temp)){
               face.sendOutMsg(("\nComputer is wrong. You get the credit!\n"));
               board.add(choice, getActivePlayer().get_player_id());
            }
            else {
               face.sendOutMsg(("\nComputer is wrong. No Credit! \n"));
            }
            
         }
      }
   }
      
   private Player getActivePlayer() {
      if(player_one.getActive()) return player_one;
      else return player_two;
   }
   private Player getOtherPlayer(){
      if(player_one.getActive()) return player_two;
      else return player_one;
   }
   public int[][] getBoard(){
      return board.getBoard();
   }
   public int[] getSpots(){
      return board.getSpots();
   }

}
