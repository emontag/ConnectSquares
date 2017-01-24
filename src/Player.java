
public class Player {
   private int id;
   private boolean active;

   public Player(int i){
      id=i;
   }

   public int get_player_id() {
      return id;
   }
   public void setActive(){
      active=true;
   }
   public void setInactive(){
      active=false;
   }
   public boolean getActive(){
      return active;
   }

}
