package lab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonkeyGenerator {

  private int timeInteval;
  private int oneTimeMkyNumber;
  private int mkyNumber;
  private int maxVelocity;
  private final ArrayList<Monkey> toLeftMonkeyList = new ArrayList<>();
  private final ArrayList<Monkey> toRightMonkeyList = new ArrayList<>();
  private final ArrayList<MonkeyThread> allMonkeyThreads = new ArrayList<>();
  private final Map<MonkeyThread, Integer> monkeyInfo = new HashMap<>();
  private ArrayList<Ladder> ladders;
  private ArrayList<String> line;
  private boolean hasStarted = false;
  private long start;
  private int timeDuration;
  
  /**Class builder, used to build a monkeyThread and a mainThread.
   * 13 variables included.
   * Mutable class.
   * REP: null
   * AF: 
   * timeInteval:  control the speed of creating monkeys.
   * oneTimeMkyNumber: the number of the new monkeys created one time.
   * mkyNumber: the number of all the monkeys.
   * maxVelocity: the max speed of the monkeys.
   * All the lists are used to record the thread infomation.
   * RI: the first 6 variables are final, can't be changed.
   * (done)
    */
  
  public long getStart() {
    return start;
  }

  public ArrayList<Monkey> getToLeftMonkeyList() {
    return toLeftMonkeyList;
  }

  public ArrayList<Monkey> getToRightMonkeyList() {
    return toRightMonkeyList;
  }
  
  public ArrayList<String> getLine() {
    return line;
  }

  /**
   * builder a monkey generator.
   * @param timeInteval
   * 
   * @param oneTimeMkyNumber
   * 
   * @param mkyNumber
   * 
   * @param maxVelocity
   * 
   */
  public MonkeyGenerator(
      int timeInteval, int oneTimeMkyNumber, 
      int mkyNumber,int maxVelocity) {
    this.timeInteval = timeInteval;
    this.oneTimeMkyNumber = oneTimeMkyNumber;
    this.mkyNumber = mkyNumber;
    this.maxVelocity = maxVelocity;
    start = System.currentTimeMillis();
  }
  
  public MonkeyGenerator(ArrayList<String> line) {
    this.line = line;
    start = System.currentTimeMillis();
  }
  
  /**
   * Create threads randomly.
   */
  public void init() {
    
    int size = mkyNumber / oneTimeMkyNumber;
    int lastSize = mkyNumber - oneTimeMkyNumber * size;
    int id = 1;
    for (int i = 0; i < size; i++) {
      try {
        Thread.sleep(timeInteval * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      for (int j = 0; j < oneTimeMkyNumber; j++) {
        double a = Math.random();
        while (a == 0) {
          a = Math.random();
        }
        double b = Math.random();
        double temp = maxVelocity * a;
        int velocity = (int)temp;
        if (velocity == 0) {
          velocity++;
        }
        Direction direction = null;
        if (b > 0.5) {
          direction = Direction.right;
        } else {
          direction = Direction.left;
        }
        MonkeyThread newThread = new MonkeyThread(id, velocity, direction, this);
        newThread.start();
        if (direction == Direction.left) {
          synchronized (toLeftMonkeyList) {
            toLeftMonkeyList.add(newThread.getMonkey());
            allMonkeyThreads.add(newThread);
          }
        } else {
          synchronized (toRightMonkeyList) {
            toRightMonkeyList.add(newThread.getMonkey());
            allMonkeyThreads.add(newThread);
          }
        }
        id++;
      }
    }
    for (int i = 0; i < lastSize; i++) {
      double a = Math.random();
      double b = Math.random();
      double temp = maxVelocity * a;
      int velocity = (int)temp;
      Direction direction = null;
      if (b > 0.5) {
        direction = Direction.right;
      } else {
        direction = Direction.left;
      }
      MonkeyThread newThread = new MonkeyThread(id, velocity, direction, this);
      newThread.start();
      id++; 
      if (direction == Direction.left) {
        synchronized (toLeftMonkeyList) {
          toLeftMonkeyList.add(newThread.getMonkey());
          allMonkeyThreads.add(newThread);
        }
      } else {
        synchronized (toRightMonkeyList) {
          toRightMonkeyList.add(newThread.getMonkey());
          allMonkeyThreads.add(newThread);
        }
      }
    }
    hasStarted = true;
  }

  /**
   * create thread based on the file.
   */
  public void init1() {
    for (int i = 0; i < line.size(); i++) {
      String[] temp = line.get(i).split("=");
      if (temp[0].equals("monkey")) {
        String[] info = temp[1].split(",");
        int monkeyTime = Integer.valueOf(info[0]);
        int id = Integer.valueOf(info[1]);
        Direction direction;
        int velocity = Integer.valueOf(info[3]);
        MonkeyThread newThread;
        if (info[2].equals("R-L")) {
          direction = Direction.left;
          newThread = new MonkeyThread(id, velocity, direction, this);
        } else {
          direction = Direction.right;
          newThread = new MonkeyThread(id, velocity, direction, this);
        }
        monkeyInfo.put(newThread, monkeyTime);
      }
    }
    for (Map.Entry<MonkeyThread, Integer> temp : monkeyInfo.entrySet()) {
      if (temp.getValue() > timeDuration) {
        timeDuration = temp.getValue();
      }
    }
    int time = 0;
    while (time <= timeDuration) {
      for (Map.Entry<MonkeyThread, Integer> temp : monkeyInfo.entrySet()) {
        if (temp.getValue() == time) {
          temp.getKey().start();
          MonkeyThread newThread = temp.getKey();
          if (newThread.getMonkey().getDirection() == Direction.left) {
            synchronized (toLeftMonkeyList) {
              toLeftMonkeyList.add(newThread.getMonkey());
              allMonkeyThreads.add(newThread);
            }
          } else {
            synchronized (toRightMonkeyList) {
              toRightMonkeyList.add(newThread.getMonkey());
              allMonkeyThreads.add(newThread);
            }
          }
        }
      }
      time++;
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    hasStarted = true;
  }
  
  public ArrayList<Ladder> getLadders() {
    return ladders;
  }

  public void setLadders(ArrayList<Ladder> ladders) {
    this.ladders = ladders;
  }

  public boolean hasStarted() {
    return hasStarted;
  }

  public ArrayList<MonkeyThread> getAllMonkeys() {
    return allMonkeyThreads;
  }

}
