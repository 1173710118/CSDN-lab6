package lab;

public class Builder extends Thread {

  private final int ladderNumber;
  private final int ladderLength;
  private final int timeInteval;
  private final int oneTimeMkyNumber;
  private final int mkyNumber;
  private final int maxVelocity;
  private long time;
  private long start;
  private long end;
  private boolean isStopped = false;
  private double index;
  private String infoString = "";
  /**Class builder, used to build a monkeyThread and a mainThread.
   * 12 variables included.
   * Mutable class.
   * REP: null
   * AF: 
   * ladderNumber: the number of the ladders.
   * ladderLength: the number of the rungs in a single ladder.
   * timeInteval:  control the speed of creating monkeys.
   * oneTimeMkyNumber: the number of the new monkeys created one time.
   * mkyNumber: the number of all the monkeys.
   * maxVelocity: the max speed of the monkeys.
   * time: used to record the time taken.
   * start: used to compute the time.
   * end: used to compute the time.
   * isStopped: mark the end of this thread.
   * index: give the index to the Draw.
   * infoString: print the info to the GUI.
   * RI: the first 6 variables are final, can't be changed.
   * (done)
    */
  
  public double getIndex() {
    return index;
  }

  public String getInfoString() {
    return infoString;
  }

  public boolean isStopped() {
    return isStopped;
  }

  public long getTime() {
    return time;
  }

  /**
   * build the whole system.
   * @param ladderNumber
   * 
   * @param ladderLength
   * 
   * @param timeInteval
   * 
   * @param oneTimeMkyNumber
   * 
   * @param mkyNumber
   * 
   * @param maxVelocity
   * 
   */
  public Builder(int ladderNumber, int ladderLength, int timeInteval, 
      int oneTimeMkyNumber, int mkyNumber, int maxVelocity) {
    this.ladderNumber = ladderNumber;
    this.ladderLength = ladderLength;
    this.timeInteval = timeInteval;
    this.oneTimeMkyNumber = oneTimeMkyNumber;
    this.mkyNumber = mkyNumber;
    this.maxVelocity = maxVelocity;
  }

  /**
   * First of all, create a new MonkeyGenerator, then the mainThread used to create the
   * ladder and start the monkeyGenerator.
   * Then the generator gets the ladders, mainThread start the generator.
   */
  @Override
  public void run() {
    start = System.currentTimeMillis();
    MonkeyGenerator monkeyGenerator = 
        new MonkeyGenerator(timeInteval, oneTimeMkyNumber, mkyNumber, maxVelocity);
    MainThread mainThread = 
        new MainThread(ladderNumber, ladderLength, timeInteval, oneTimeMkyNumber, mkyNumber,
        maxVelocity, monkeyGenerator);
    monkeyGenerator.setLadders(mainThread.getLadders());
    mainThread.start();

    printGraph(mainThread, monkeyGenerator);
    isStopped = true;
  }

  /**
   * used to print the info to the console, and give the update the info String.
   * @param mainThread
   * 
   * @param monkeyGenerator
   * 
   */
  private void printGraph(MainThread mainThread, MonkeyGenerator monkeyGenerator) {
    boolean exit = false;
    while (!exit) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      StringBuilder temp = new StringBuilder();
      System.out.println(mainThread.getMonkeyGenerator().getToLeftMonkeyList().size() + ":"
          + mainThread.getMonkeyGenerator().getToRightMonkeyList().size());
      temp.append(mainThread.getMonkeyGenerator().getToLeftMonkeyList().size() + ":"
          + mainThread.getMonkeyGenerator().getToRightMonkeyList().size() + "\n");
      for (int i = 0; i < ladderNumber; i++) {
        System.out.print(i + 1);
        temp.append(String.valueOf(i + 1));
        for (int j = 0; j < ladderLength; j++) {
          System.out.print("|");
          temp.append("|");
          synchronized (mainThread.getLadders().get(i).getRungs().get(j)) {
            if (mainThread.getLadders().get(i).getRungs().get(j).isTaken()) {
              System.out.printf("%3d",
                  mainThread.getLadders().get(i).getRungs().get(j).getMonkey().getID());
              temp.append(String.format("%03d",
                  mainThread.getLadders().get(i).getRungs().get(j).getMonkey().getID()));
            } else {
              System.out.print("   ");
              temp.append("000");
            }
          }
        }
        System.out.print("|");
        System.out.println();
        temp.append("|\n");
      }
      System.out.println();
      temp.append("\n\n\n");
      boolean isTmpty = true;
      if (mainThread.getMonkeyGenerator().getToLeftMonkeyList().size() != 0) {
        isTmpty = false;
      }
      if (mainThread.getMonkeyGenerator().getToRightMonkeyList().size() != 0) {
        isTmpty = false;
      }
      for (int i = 0; i < mainThread.getLadders().size(); i++) {
        if (mainThread.getLadders().get(i).getDirection() != Direction.neutral) {
          isTmpty = false;
        }
      }
      if (isTmpty && monkeyGenerator.hasStarted()) {
        exit = true;
      }
      end = System.currentTimeMillis();
      time = (end - start) / 1000;
      temp.append("ÓÃÊ±: " + String.valueOf((end - start) / 1000) + "Ãë");
      infoString = temp.toString();
    }
    for (int i = 0; i < monkeyGenerator.getAllMonkeys().size(); i++) {
      System.out.println((monkeyGenerator.getAllMonkeys().get(i).getStart() 
          - monkeyGenerator.getStart()) / 1000 + ":" 
          + (monkeyGenerator.getAllMonkeys().get(i).getEnd() 
              - monkeyGenerator.getStart()) / 1000);
    }
    index = 0;
    for (int i = 0; i < monkeyGenerator.getAllMonkeys().size(); i++) {
      for (int j = i + 1; j < monkeyGenerator.getAllMonkeys().size(); j++) {
        double startDiff = monkeyGenerator.getAllMonkeys().get(i).getStart()
            - monkeyGenerator.getAllMonkeys().get(j).getStart();
        double endDiff = monkeyGenerator.getAllMonkeys().get(i).getEnd()
            - monkeyGenerator.getAllMonkeys().get(j).getEnd();
        if (startDiff * endDiff > 0) {
          index++;
        } else {
          index--;
        }
      }
    }
    int n = monkeyGenerator.getAllMonkeys().size();
    index = index / (n * (n - 1) / 2);
  }

  public int getMkyNumber() {
    return mkyNumber;
  }
}
