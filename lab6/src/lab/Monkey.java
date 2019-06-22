package lab;

public class Monkey {

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Monkey other = (Monkey) obj;
    if (id != other.id) {
      return false;
    }
    return true;
  }

  private final int id;
  private final int velocity;
  private final Direction direction;
  /**
   * monkey class.
   * Immutable class.
   * REP: null
   * AF: 
   * ID: mark the sequence of the monkey.
   * velocity: the speed.
   * Direction: the moving direction.
   * (done)
   */

  public Monkey(int id, int velocity, Direction direction) {
    this.id = id;
    this.velocity = velocity;
    this.direction = direction;
  }

  public int getVelocity() {
    return velocity;
  }

  public Direction getDirection() {
    return direction;
  }

  public int getID() {
    return id;
  }

}
