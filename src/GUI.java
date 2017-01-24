import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
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
   Game game;
   Board bore;
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
      for(int i=0;i<8;i++) {
         for(int j=0;j<8;j++){
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
      numQuest=new boolean[18];
      jframe.setVisible(true);
   }

   @Override
   public void run() {
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
   public boolean ask_about_computer() {
      String[] modes={"No Computer", "Have Computer"};
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
      else return true;
   }

   @Override
   public void sendOutMsg(String string) {
      textarea.append(string);
      
   }
}
