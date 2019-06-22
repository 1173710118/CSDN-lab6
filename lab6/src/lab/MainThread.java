package lab;

import java.util.ArrayList;

public class MainThread extends Thread {

  private final int ladderNumber;
  private final int ladderLength;
  private final int timeInteval;
  private final int oneTimeMkyNumber;
  private final int mkyNumber;
  private final int maxVelocity;
  private final MonkeyGenerator monkeyGenerator;
  private final ArrayList<Ladder> ladders = new ArrayList<>();
  /**Class mainThread, used to create ladders and start the monkeyGenerator.
   * 8 variables included.
   * Mutable class.
   * REP: null
   * AF: 
   * ladderNumber: the number of the ladders.
   * ladderLength: the number of the rungs in a single ladder.
   * timeInteval:  control the speed of creating monkeys.
   * oneTimeMkyNumber: the number of the new monkeys created one time.
   * mkyNumber: the number of all the monkeys.
   * maxVelocity: the max speed of the monkeys.
   * monkeyGenerator: used to generator monkeys.
   * RI: the first 6 variables are final, can't be changed.
   * (done)
    */

  public MainThread(
      int ladderNumber, int ladderLength, int timeInteval, 
      int oneTimeMkyNumber, int mkyNumber,int maxVelocity,
      MonkeyGenerator monkeyGenerator) {
    this.ladderNumber = ladderNumber;
    this.ladderLength = ladderLength;
    this.timeInteval = timeInteval;
    this.oneTimeMkyNumber = oneTimeMkyNumber;
    this.mkyNumber = mkyNumber;
    this.maxVelocity = maxVelocity;
    this.monkeyGenerator = monkeyGenerator;
    init();
  }
  
  private synchronized void init() {
    for (int i = 0; i < ladderNumber; i++) {
      Ladder newLadder = new Ladder(i, ladderLength);
      ladders.add(newLadder);
    }
  }
  
  @Override
  public void run() {
    monkeyGenerator.init();
  }

  public MonkeyGenerator getMonkeyGenerator() {
    return monkeyGenerator;
  }

  public int getLadderNumber() {
    return ladderNumber;
  }

  public int getLadderLength() {
    return ladderLength;
  }

  public int getTimeInteval() {
    return timeInteval;
  }

  public int getOneTimeMkyNumber() {
    return oneTimeMkyNumber;
  }

  public int getMkyNumber() {
    return mkyNumber;
  }

  public int getMaxVelocity() {
    return maxVelocity;
  }

  public ArrayList<Ladder> getLadders() {
    return ladders;
  }



}
