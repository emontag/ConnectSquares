import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class GUI extends Interface implements ActionListener, Observer{
   JFrame jframe;
   private JLabel[][] board;
   private JButton[] controls;
   private Container content;
   private TextArea textarea;
   private boolean showedPane;
   private Thread thread;
   private Game game;
   private Board bore;
   private boolean computer_playing;
   private int number_to_win;
   //resizable TBC for resizable board
   public GUI(Game g, Board b){
      bore=b;
      bore.addObserver(this);
      game=g;
      jframe=new JFrame();
      jframe.setSize(800,800);
      content=jframe.getContentPane();
      jframe.setLocation(500,200);
      jframe.setTitle("The Game");
      jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      createFileMenu();
      JPanel panel=new JPanel();
      panel.setLayout(new GridLayout(9,9));
      textarea=new TextArea("",100,100,TextArea.SCROLLBARS_VERTICAL_ONLY );
      content.add(panel);
      content.add(textarea);
      jframe.setLayout(new GridLayout(1,2));
      board=new JLabel[8][8];
      for(int i=0;i<board.length;i++) {
         for(int j=0;j<board[i].length;j++){
            board[i][j]=new JLabel(" ");
            board[i][j].setEnabled(false);
            panel.add(board[i][j]);
            board[i][j].setBackground(new Color(217,253,255));
         }
      }
      controls=new JButton[board.length];
      for(int i=0;i<8;i++) {
         controls[i]=new JButton(Integer.toString(i+1));
         panel.add(controls[i]);
         controls[i].setBackground(new Color(217,253,255));
         controls[i].addActionListener(this);
      }
      //update based on number of questions
      numQuest=new boolean[18];
      jframe.setVisible(true);
   }

   @Override
   public void run() {
      textarea.append("\n");
      for(int i=15;i>0;i--){
         try {
            Thread.sleep(1000);
         } catch (InterruptedException e) {         }
       //added statement so returns if pane closed
         if(!showedPane) return;
         textarea.append("Timer: "+ Integer.toString(i)+" Seconds Remaining"+"\n"); 
      }
      showedPane=false;
      JOptionPane.getRootFrame().dispose();
      
   }

   @Override
   public boolean chooseAnswer(String question) {
      thread=new Thread(this);
      showedPane=true;
      String [] array={"True","False"};
      thread.start();
      int rep=JOptionPane.showOptionDialog(jframe, question, "Choice", JOptionPane.YES_NO_CANCEL_OPTION 
                                   , JOptionPane.QUESTION_MESSAGE,null , array, null);
      showedPane=false;
      if(rep==-1) {
         textarea.append("Bad luck! You're wrong!\n");
         ans=true;
         return false;
      }
      boolean b=rep==0;
      if(b==ans) textarea.append("You are Correct!\n");
      else textarea.append("Foolish choice!\n");
      return b==ans;
   }


   @Override
   public void actionPerformed(ActionEvent e) {
      String event=e.getActionCommand();
      if(event == "New Game"){
         jframe.dispose();
         new Game();
         return;
      }
      
      try {
         
         game.addToBoard(Integer.parseInt(event));
         
      } catch (NumberFormatException | BoardException e1) {
         e1.printStackTrace();
      }
      //no board exception thrown
      int[] spots=game.getSpots();
      for(int i=0;i<spots.length;i++){
         if(spots[i]== 8) controls[i].setEnabled(false);
      }
      if(game.win(bore)) {
         textarea.append("Winner");
         for(int i=0;i<controls.length;i++){
            controls[i].setEnabled(false);
         }
      }
     
   }

   @Override
   public void update(Observable o, Object arg) {
      int[][] board_copy=game.getBoard();
      for(int i=0;i<board_copy.length;i++){
         for(int j=0;j<board_copy[i].length;j++){
            board[i][j].setText(Integer.toString(board_copy[i][j]));
         }
      }
      
   }
   public void createFileMenu() {
      JMenuBar menuBar = new JMenuBar();
      menuBar.setOpaque(true);
      menuBar.setBackground(new Color(217,253,255));
      JMenuItem item;//menu option
      JMenu fileMenu = new JMenu("File");//menu name
      fileMenu.setOpaque(true);
      fileMenu.setBackground(new Color(217,253,255));
      item = new JMenuItem("New Game"); //Open
      item.setBackground(new Color(217,253,255));
      item.addActionListener(this);
      fileMenu.add( item);
      jframe.setJMenuBar(menuBar);
      menuBar.add(fileMenu);
   }

   @Override
   /**
    * determines computer and number to win
    * 
    */
   public void ask_the_question() {
     /* String[] modes={"No Computer", "Have Computer"};
      int rep = JOptionPane.showOptionDialog(null,
              "Choose whether to have a computer!",
              "Mode",
               JOptionPane.YES_NO_CANCEL_OPTION,
              JOptionPane.QUESTION_MESSAGE,
               null,
               modes,
                null);
      if(rep==-1) System.exit(0);;//adding to exit on abrupt termination to take care of contingencies
      if(rep==0) return false;
      else return true;*/
      String[] modes = {"Have Computer","No Computer"};
      String[] numbers={"3","4","5","6"};
      JPanel panel=new JPanel();
      panel.setLayout(new GridLayout(2,2));
      JComboBox list=new JComboBox(modes);
      JComboBox num_list=new JComboBox(numbers);
      JLabel cLabel=new JLabel("Computer?");
      panel.add(cLabel);
      panel.add(list);
      JLabel nLabel=new JLabel("How Many to Win?");
      panel.add(nLabel);
      panel.add(num_list);
      JOptionPane pane=new JOptionPane(panel);
      JDialog dialog = pane.createDialog(pane, "Game");
      dialog.setVisible(true);
      Object value=pane.getValue();
      if (value == null) System.exit(1);
      if(list.getSelectedItem()=="Have Computer"){
         computer_playing=true;
      }
      else computer_playing=false;
      number_to_win=Integer.parseInt(num_list.getSelectedItem().toString());
   }

   @Override
   public void sendOutMsg(String string) {
      textarea.append(string);
      
   }
   public boolean getComputer(){
      return computer_playing;
   }
   public int getNumberToWin(){
      return number_to_win;
   }
   
}
