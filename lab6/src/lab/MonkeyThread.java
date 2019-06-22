package lab;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MonkeyThread extends Thread {

  private final Monkey monkey;
  private final MonkeyGenerator monkeyGenerator;
  private int ladderNumber;
  private int ladderLength;
  private int position;
  private long start;
  private long end;
  private final int interval = 1000;
  
  /**
   * The thread of the monkey.
   * AF: the manager of a monkey.
   * ladderNumber: the number of ladder that the monkey is on.
   * ladderLength: the number of the rungs in a ladder.
   * position: record the position of the monkey
   * (done)
   */

  public MonkeyThread(int id, int velocity, 
      Direction direction, MonkeyGenerator monkeyGenerator) {
    monkey = new Monkey(id, velocity, direction);
    this.monkeyGenerator = monkeyGenerator;
    ladderLength = monkeyGenerator.getLadders().get(0).getRungs().size();
  }

  public Monkey getMonkey() {
    return monkey;
  }

  /**
   * choose the ladder, and move, and leave.
   */
  @Override
  public void run() {
    start = System.currentTimeMillis();
    Logger logger = LogManager.getLogger("第" + String.valueOf(monkey.getID()) + "个猴子 ");
    end = 0;
    while (true) {
      end = System.currentTimeMillis();
      String direction = "正在左岸等待";
      if (monkey.getDirection() == Direction.left) {
        direction = "正在右岸等待";
      }
      logger.info(direction + "离出生已经" + (end - start) / 1000 + "秒");
      boolean isfastest = true;
      if (monkey.getDirection() == Direction.right) {
        synchronized (monkeyGenerator.getToRightMonkeyList()) {
          for (int i = 0; i < monkeyGenerator.getToRightMonkeyList().size(); i++) {
            if (monkeyGenerator.getToRightMonkeyList().get(i).getVelocity() 
                > monkey.getVelocity()) {
              isfastest = false;
              break;
            }
          }
        }
      }
      if (monkey.getDirection() == Direction.left) {
        synchronized (monkeyGenerator.getToLeftMonkeyList()) {
          for (int i = 0; i < monkeyGenerator.getToLeftMonkeyList().size(); i++) {
            if (monkeyGenerator.getToLeftMonkeyList().get(i).getVelocity() 
                > monkey.getVelocity()) {
              isfastest = false;
              break;
            }
          }
        }
      }
      
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (isfastest) {
        break;
      }
    }
    while (!choose2()) {
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      end = System.currentTimeMillis();
      String direction = "正在左岸等待";
      if (monkey.getDirection() == Direction.left) {
        direction = "正在右岸等待";
      }
      logger.info(direction + "离出生已经" + (end - start) / 1000 + "秒");
    }

    if (monkey.getDirection() == Direction.right) {
      moveRight();
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (position == ladderLength - 1) {
        synchronized (monkeyGenerator.getLadders()
            .get(ladderNumber).getRungs().get(ladderLength - 1)) {
          monkeyGenerator.getLadders().get(ladderNumber)
          .getRungs().get(ladderLength - 1).setMonkey(null);
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(ladderLength - 1).unTake();
        }
        boolean isempty = true;
        for (int i = 0; i < ladderLength; i++) {
          if (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(i).isTaken()) {
            isempty = false;
            break;
          }
        }
        if (isempty) {
          monkeyGenerator.getLadders().get(ladderNumber).setDirection(Direction.neutral);
        }
      }
    } else {
      moveLeft();
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (position == 0) {
        synchronized (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(0)) {
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(0).setMonkey(null);
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(0).unTake();
        }
        boolean isempty = true;
        for (int i = 0; i < ladderLength; i++) {
          if (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(i).isTaken()) {
            isempty = false;
            break;
          }
        }
        if (isempty) {
          monkeyGenerator.getLadders().get(ladderNumber).setDirection(Direction.neutral);
        }
      }
    }
    end = System.currentTimeMillis();
    String direction = "抵达右岸";
    if (monkey.getDirection() == Direction.left) {
      direction = "抵达左岸";
    }
    logger.info(direction + "总耗时" + (end - start) / 1000 + "秒");
  }

  public long getStart() {
    return start;
  }

  public long getEnd() {
    return end;
  }

  /**
   * choosing strategy 1
   * choose a ladder that is not taken by any other monkeys.
   * @return
   */
  private boolean choose1() {
    int maxTake = monkeyGenerator.getLadders().size() / 2;
    synchronized (monkeyGenerator.getLadders()) {
      boolean selectAll = false;
      if (monkeyGenerator.getToLeftMonkeyList().isEmpty()) {
        selectAll = true;
        for (int i = maxTake; i < monkeyGenerator.getLadders().size(); i++) {
          if (monkeyGenerator.getLadders().get(i).getDirection() != Direction.neutral) {
            selectAll = false;
          }
        }
      }
      if (monkey.getDirection() == Direction.right) {
        if (selectAll) {
          maxTake = monkeyGenerator.getLadders().size();
          System.err.println(maxTake);
        }
        for (int i = 0; i < maxTake; i++) {
          if (monkeyGenerator.getLadders().get(i).getDirection() == Direction.neutral
              && !monkeyGenerator.getLadders().get(i).getRungs().get(0).isTaken()) {
            monkeyGenerator.getLadders().get(i).setDirection(Direction.right);
            monkeyGenerator.getLadders().get(i).getRungs().get(0).setTaken();
            monkeyGenerator.getLadders().get(i).getRungs().get(0).setMonkey(monkey);
            position = 0;
            synchronized (monkeyGenerator.getToRightMonkeyList()) {
              monkeyGenerator.getToRightMonkeyList().remove(monkey);
            }
            ladderNumber = i;
            return true;
          }
        }
      }
    }

    if (monkey.getDirection() == Direction.left) {
      synchronized (monkeyGenerator.getLadders()) {
        for (int i = maxTake; i < monkeyGenerator.getLadders().size(); i++) {
          if (monkeyGenerator.getLadders().get(i).getDirection() == Direction.neutral
              && !monkeyGenerator.getLadders().get(i).getRungs().get(ladderLength - 1).isTaken()) {
            monkeyGenerator.getLadders().get(i).setDirection(Direction.left);
            monkeyGenerator.getLadders().get(i).getRungs().get(ladderLength - 1).setTaken();
            monkeyGenerator.getLadders().get(i).getRungs().get(ladderLength - 1).setMonkey(monkey);
            position = ladderLength - 1;
            synchronized (monkeyGenerator.getToLeftMonkeyList()) {
              monkeyGenerator.getToLeftMonkeyList().remove(monkey);
            }
            ladderNumber = i;
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * choosing strategy 2
   * choose only when the monkey itself is the fastest monkey among the left.
   * @return
   */
  private boolean choose2() {
    int maxTake = monkeyGenerator.getLadders().size() / 2;
    boolean selectAll = false;
    if (monkeyGenerator.getToLeftMonkeyList().isEmpty()) {
      selectAll = true;
      for (int i = maxTake; i < monkeyGenerator.getLadders().size(); i++) {
        if (monkeyGenerator.getLadders().get(i).getDirection() != Direction.neutral) {
          selectAll = false;
        }
        if (monkeyGenerator.getLadders().get(i).getDirection() == Direction.right) {
          selectAll = true;
        }
      }
    }
    if (monkey.getDirection() == Direction.right) {
      if (maxTake == 0) {
        maxTake++;
      }
      if (selectAll) {
        maxTake = monkeyGenerator.getLadders().size();
      }
      synchronized (monkeyGenerator.getLadders()) {
        for (int i = 0; i < maxTake; i++) {
          if ((monkeyGenerator.getLadders().get(i).getDirection() == Direction.neutral
              || monkeyGenerator.getLadders().get(i).getDirection() == Direction.right)
              && !monkeyGenerator.getLadders().get(i).getRungs().get(0).isTaken()) {
            monkeyGenerator.getLadders().get(i).setDirection(Direction.right);
            monkeyGenerator.getLadders().get(i).getRungs().get(0).setTaken();
            monkeyGenerator.getLadders().get(i).getRungs().get(0).setMonkey(monkey);
            position = 0;
            synchronized (monkeyGenerator.getToRightMonkeyList()) {
              monkeyGenerator.getToRightMonkeyList().remove(monkey);
            }
            ladderNumber = i;
            return true;
          }
        }

      }
    }

    if (monkey.getDirection() == Direction.left) {
      synchronized (monkeyGenerator.getLadders()) {
        for (int i = maxTake; i < monkeyGenerator.getLadders().size(); i++) {
          if ((monkeyGenerator.getLadders().get(i).getDirection() == Direction.neutral
              || monkeyGenerator.getLadders().get(i).getDirection() == Direction.left)
              && !monkeyGenerator.getLadders().get(i).getRungs().get(ladderLength - 1).isTaken()) {
            monkeyGenerator.getLadders().get(i).setDirection(Direction.left);
            monkeyGenerator.getLadders().get(i).getRungs().get(ladderLength - 1).setTaken();
            monkeyGenerator.getLadders().get(i).getRungs().get(ladderLength - 1).setMonkey(monkey);
            position = ladderLength - 1;
            synchronized (monkeyGenerator.getToLeftMonkeyList()) {
              monkeyGenerator.getToLeftMonkeyList().remove(monkey);
            }
            ladderNumber = i;
            return true;
          }

        }

      }
    }
    return false;
  }

  /**
   * move towards right, if one of the rungs between the current position and
   * target position is taken, move to the rung in front of that.
   */
  private void moveRight() {
    Logger logger = LogManager.getLogger("第" + String.valueOf(monkey.getID()) + "个猴子 ");
    while (position + 1 < ladderLength) {
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      long end = System.currentTimeMillis();
      logger.info("正在第" + (ladderNumber + 1) + "个梯子第" + (position + 1) 
          + "个踏板，从左向右" + "离出生已经" + (end - start) / 1000 + "秒");
      int targetPosition = position + monkey.getVelocity();
      if (targetPosition + 1 > ladderLength) {
        targetPosition = ladderLength - 1;
      }
      int realPosition = targetPosition;
      for (int i = position + 1; i <= targetPosition; i++) {
        if (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(i).isTaken()) {
          realPosition = i - 1;
        }
      }
      if (realPosition > position) {
        synchronized (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(position)) {
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(realPosition).setTaken();
          monkeyGenerator.getLadders()
          .get(ladderNumber).getRungs().get(realPosition).setMonkey(monkey);
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(position).setMonkey(null);
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(position).unTake();
          position = realPosition;
        }
      }
    }
  }

  /**
   * move towards left, if one of the rungs between the current position and
   * target position is taken, move to the rung in front of that.
   */
  private void moveLeft() {
    Logger logger = LogManager.getLogger("第" + String.valueOf(monkey.getID()) + "个猴子 ");
    while (position > 0) {
      try {
        Thread.sleep(interval);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      long end = System.currentTimeMillis();
      logger.info("正在第" + (ladderNumber + 1) + "个梯子第" + (position + 1) 
          + "个踏板，从右向左" + "离出生已经" + (end - start) / 1000 + "秒");
      int targetPosition = position - monkey.getVelocity();
      if (targetPosition <= 0) {
        targetPosition = 0;
      }
      int realPosition = targetPosition;

      for (int i = position - 1; i >= targetPosition; i--) {
        if (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(i).isTaken()) {
          realPosition = i + 1;
        }
      }
      if (realPosition < position) {
        synchronized (monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(position)) {
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(realPosition).setTaken();
          monkeyGenerator.getLadders()
          .get(ladderNumber).getRungs().get(realPosition).setMonkey(monkey);
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(position).setMonkey(null);
          monkeyGenerator.getLadders().get(ladderNumber).getRungs().get(position).unTake();
          position = realPosition;
        }
      }
    }
  }

}
