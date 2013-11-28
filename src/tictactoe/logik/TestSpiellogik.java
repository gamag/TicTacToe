package tictactoe.logik;

public class TestSpiellogik {
  
  public static void main(String[] args) {
    testUnentschieden(); 
    testGewinner();     
  }
  
  public static void testUnentschieden() {
    // 122
    // 211
    // 112
    Spiellogik l = new Spiellogik();
    l.setzeFeld(0,0,1);
    l.setzeFeld(1,0,2);
    l.setzeFeld(1,1,1);
    l.setzeFeld(2,0,2);
    l.setzeFeld(2,1,1);
    l.setzeFeld(0,1,2);
    l.setzeFeld(0,2,1);
    l.setzeFeld(2,2,2);
    l.setzeFeld(1,2,1); 
    assertTrue(l.spielFertig(), "Spiel muss fertig sein");
  }
  
  public static void testGewinner() {
    //121
    //21 
    //1 2
    Spiellogik l = new Spiellogik();
    l.setzeFeld(0,0,1);
    l.setzeFeld(2,2,2);
    l.setzeFeld(2,0,1);
    l.setzeFeld(1,0,2);
    l.setzeFeld(0,2,1);
    l.setzeFeld(0,1,2);
    l.setzeFeld(1,1,1);
    
    assertTrue(l.spielFertig(), "Spiel muss fertig sein");
    assertTrue(l.gewinner() == 1, "Spieler 1 Gewonnen");
    
  }  
  
  public static void assertTrue(boolean t) {
    if (!t) {
      throw new RuntimeException("Test failed");
    }
  }
  
  public static void assertTrue(boolean t, String reason) {
    if (!t) {
      throw new RuntimeException("Test failed: " + reason);
    }
  }
}