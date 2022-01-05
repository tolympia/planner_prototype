import java.util.*;
import java.time.*;


public abstract class Schedule {
  public static LocalDate firstDayOfSchool;
  public static LocalDate lastDayOfSchool;

  protected LocalDate date;
  public int dayType = -1;
  public ArrayList<Block> blocks;
  public ArrayList<String> blockNames;

  public abstract int getDayType();
  public abstract boolean isThereBlockToday(String name);

  protected void printSummary() {
    System.out.println("DAY TYPE: " + this.dayType);
    System.out.println("BLOCKS:");
    for (int i = 0; i < this.blockNames.size(); i++) {
      System.out.print("[" + blockNames.get(i) +  "] ");
      System.out.println(blocks.get(i).toString());
    }
  }
}
