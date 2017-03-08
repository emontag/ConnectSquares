import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public abstract class Interface implements Runnable{
   /** Board used to play the game*/
   //protected Board board;
   protected Game game;
   protected boolean ans;//this is extra needed to keep track of whether answer from database is correct 
   protected boolean[] numQuest;//track question used or not intilize to number of questions
   protected int countdown=15;
   
   /**
    * load question and answers into String. GUI decides what to do with it. 
    * @return String of question and answer
    */
   protected String loadQuestion(){
      try{
         BufferedReader buffer = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("database.txt")));
       boolean used=true;
         //expanding upon detail as needed
         for(int i=0;i<numQuest.length;i++){
            if(numQuest[i]==false) {
               used=false;
               break;
            }
         }
         if(used){
            for(int i=0;i<numQuest.length;i++){
               numQuest[i]=false;
            }
         }
         int num;
         do{
            Random r=new Random();
            num=r.nextInt(numQuest.length);
         }while(numQuest[num]==true);
         numQuest[num]=true;
         String line=buffer.readLine();
         for(int i=0;i<num;i++){
            for(int j=0;j<4;j++) buffer.readLine();
            line=buffer.readLine();
         }
         String question=line+" \n";
         Random r=new Random();
         num=r.nextInt(4);
         line=buffer.readLine();
         for(int i=0;i<num;i++) line=buffer.readLine();
         //Separate answer and whether true for ans
         String[] sp=new String[2];
         sp[0]=line.substring(0, 1);
         sp[1]=line.substring(1, line.length()).trim();
         if(sp[0].equals("T")) ans=true;
         else ans=false;
         buffer.close();
         return question+sp[1];
       
    }catch(IOException e){
       e.printStackTrace();
       System.exit(1);
    }
    return null;
   }
   
   public abstract void run();
   public abstract boolean chooseAnswer(String question);

   public abstract void ask_the_question();

   public abstract void sendOutMsg(String string);

   public abstract int getNumberToWin();

   public abstract boolean getComputer();
      
     
   
   
  
   

}
