package lab;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class BuilderRainy extends Thread {

  private int ladderNumber;
  private int ladderLength;
  private long time;
  private long start;
  private long end;
  private boolean isStopped = false;
  private double index;
  private String infoString = "";
  /**
   * All variables are the same as the Builder's.
   * Specially designed for V3.
   * (done)
   */
  
  public String getInfoString() {
    return infoString;
  }

  private final ArrayList<String> line = new ArrayList<>();
  
  public long getTime() {
    return time;
  }
  
  public long getStart() {
    return start;
  }
  
  public long getEnd() {
    return end;
  }
  
  public boolean isStopped() {
    return isStopped;
  }
  
  public double getIndex() {
    return index;
  }
  
  public ArrayList<String> getLine() {
    return line;
  }
  
  /**
   * Read the file of the pathname.
   * @param pathname
   * 
   */
  public BuilderRainy(String pathname) {
    
    try {
      FileReader reader = new FileReader(pathname);
      BufferedReader br = new BufferedReader(reader);
      String tempString;
      while ((tempString = br.readLine()) != null) {
        line.add(tempString);
      }
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    for (int i = 0; i < line.size(); i++) {
      line.set(i, line.get(i).replaceAll(" ", ""));
      line.set(i, line.get(i).replaceAll("<", ""));
      line.set(i, line.get(i).replaceAll(">", ""));
    }
    for (int i = 0; i < line.size(); i++) {
      String[] infoStrings = line.get(i).split("=");
      if (infoStrings[0].equals("n")) {
        ladderNumber = Integer.valueOf(infoStrings[1]);
      }
      if (infoStrings[0].equals("h")) {
        ladderLength = Integer.valueOf(infoStrings[1]);
      }
    }
  }
  
  @Override
  public void run() {
    start = System.currentTimeMillis();
    MonkeyGenerator monkeyGenerator = new MonkeyGenerator(line);
    MainThreadRainy mainThreadRainy = 
        new MainThreadRainy(ladderNumber, ladderLength, monkeyGenerator);
    monkeyGenerator.setLadders(mainThreadRainy.getLadders());
    mainThreadRainy.start();
    printGraph(mainThreadRainy, monkeyGenerator);
    isStopped = true;
  }
  
  private void printGraph(MainThreadRainy mainThreadRainy, 
      MonkeyGenerator monkeyGenerator) {
    boolean exit = false;
    while (!exit) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      StringBuilder temp = new StringBuilder();
      System.out.println(mainThreadRainy.getMonkeyGenerator().getToLeftMonkeyList().size() + ":"
          + mainThreadRainy.getMonkeyGenerator().getToRightMonkeyList().size());
      temp.append(mainThreadRainy.getMonkeyGenerator().getToLeftMonkeyList().size() + ":"
          + mainThreadRainy.getMonkeyGenerator().getToRightMonkeyList().size() + "\n");
      for (int i = 0; i < ladderNumber; i++) {
        System.out.print(i + 1);
        temp.append(String.valueOf(i + 1));
        for (int j = 0; j < ladderLength; j++) {
          System.out.print("|");
          temp.append("|");
          synchronized (mainThreadRainy.getLadders().get(i).getRungs().get(j)) {
            if (mainThreadRainy.getLadders().get(i).getRungs().get(j).isTaken()) {
              System.out.printf("%3d",
                  mainThreadRainy.getLadders().get(i).getRungs().get(j).getMonkey().getID());
              temp.append(String.format("%03d",
                  mainThreadRainy.getLadders().get(i).getRungs().get(j).getMonkey().getID()));
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
      if (mainThreadRainy.getMonkeyGenerator().getToLeftMonkeyList().size() != 0) {
        isTmpty = false;
      }
      if (mainThreadRainy.getMonkeyGenerator().getToRightMonkeyList().size() != 0) {
        isTmpty = false;
      }
      for (int i = 0; i < mainThreadRainy.getLadders().size(); i++) {
        if (mainThreadRainy.getLadders().get(i).getDirection() != Direction.neutral) {
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

}
