import java.util.*;
import java.time.*;


public class MSMTTh78 extends Schedule {
  public MSMTTh78(LocalDate date) {
    super(date);
    String[] blockNames = {"advisory", "1", "2", "3", "snack", "4", "5", "lunch"};
    int numBlocks = 8;
    int[] dayTypes = {1, 2, 3, 4, 5, 6};  // e.g., Day 1 in Planner.

    this.firstDayOfSchool = GAPlanner2122.msFirstDay;
    this.lastDayOfSchool = GAPlanner2122.msLastDay;
    this.setup(blockNames, numBlocks, dayTypes);
    System.out.println("DAY TYPE: " + this.dayType);


    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(7, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(8, 00), 50));   // 1st period.
        add(new Block(LocalTime.of(8, 55), 50));
        add(new Block(LocalTime.of(9, 50), 50));  // 3rd period.
        add(new Block(LocalTime.of(10, 45), 10));  // Snack.
        add(new Block(LocalTime.of(10, 55), 50));  // 4th period.
        add(new Block(LocalTime.of(11, 50), 50));  // 5th period.
        add(new Block(LocalTime.of(12, 45), 25));  // Lunch.
      }
    };
    System.out.println("BLOCKS:");
    for (int i = 0; i < blockNames.length; i++) {
      System.out.print("[" + blockNames[i] +  "] ");
      System.out.println(blocks.get(i).toString());
    }
  }
}
