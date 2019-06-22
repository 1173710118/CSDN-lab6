package lab;

import java.util.ArrayList;

public class MainThreadRainy extends Thread {

  private final int ladderNumber;
  private final int ladderLength;
  private final MonkeyGenerator monkeyGenerator;
  private final ArrayList<Ladder> ladders = new ArrayList<>();
  /**
   * Another version of the mainThread, 
   * specially designed for V3.
   * (done)
   */
  
  public MainThreadRainy(int ladderNumber, int ladderLength, 
      MonkeyGenerator monkeyGenerator) {
    this.ladderNumber = ladderNumber;
    this.ladderLength = ladderLength;
    this.monkeyGenerator = monkeyGenerator;
    init();
    
  }
  
  /**
   * build ladders.
   */
  public synchronized void init() {
    for (int i = 0; i < ladderNumber; i++) {
      Ladder newLadder = new Ladder(i, ladderLength);
      ladders.add(newLadder);
    }
  }
  
  @Override
  public void run() {
    monkeyGenerator.init1();
  }
  
  public int getLadderNumber() {
    return ladderNumber;
  }

  public int getLadderLength() {
    return ladderLength;
  }

  public MonkeyGenerator getMonkeyGenerator() {
    return monkeyGenerator;
  }

  public ArrayList<Ladder> getLadders() {
    return ladders;
  }
}
