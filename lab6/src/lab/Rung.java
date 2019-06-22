package lab;

public class Rung {

  private final int rungID;
  private boolean isTaken;
  private Monkey monkey;
  /**
   * class Rung
   * mutable class.
   * AF: the rungs of a ladder.
   * RI: ID is final
   * (done)
   */

  public Rung(int rungID) {
    this.rungID = rungID;
    this.isTaken = false;
  }

  public synchronized boolean isTaken() {
    return isTaken;
  }

  public synchronized void setTaken() {
    this.isTaken = true;
  }

  public synchronized void unTake() {
    this.isTaken = false;
  }
  
  public synchronized Monkey getMonkey() {
    return monkey;
  }

  public synchronized void setMonkey(Monkey monkey) {
    this.monkey = monkey;
  }

  public synchronized int getRungID() {
    return rungID;
  }

}
