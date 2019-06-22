package lab;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ladder {

  private final int ladderID;
  private Direction direction = Direction.neutral;
  private final List<Rung> rungs = Collections.synchronizedList(new ArrayList<Rung>());
  /**
   * ladder class.
   * 3 variables.
   * mutable class.
   * REP: null
   * AF:
   * ladderID: mark the ID of the ladders.
   * Direction: the current direction of the monkeys one this ladder.
   * rungs: includes all the rungs of the ladder.
   * RI:ladderID is final.
   * (done)
   */

  public Ladder(int ladderID, int ladderLength) {
    this.ladderID = ladderID;
    for (int i = 0; i < ladderLength; i++) {
      Rung newRung = new Rung(i);
      rungs.add(newRung);
    }
  }

  public Direction getDirection() {
    return direction;
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
  }

  public List<Rung> getRungs() {
    return rungs;
  }

  public int getLadderID() {
    return ladderID;
  }

}
