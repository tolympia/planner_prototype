import java.util.*;
import java.time.*;


public class MSMTTh56 extends MSSchedule {
  public MSMTTh56(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(7, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(8, 00), 45));   // 1st period (NOT 0).
        add(new Block(LocalTime.of(8, 50), 45));  // 2nd period.
        add(new Block(LocalTime.of(9, 40), 20));  // Snack.
        add(new Block(LocalTime.of(10, 05), 45));  // 3rd period.
        add(new Block(LocalTime.of(10, 55), 45));  // 4th period.
        add(new Block(LocalTime.of(11, 45), 30));  // Lunch.
        add(new Block(LocalTime.of(12, 20), 50));  // 5th period.
        add(new Block(LocalTime.of(13, 15), 50));  // Advisory.
        add(new Block(LocalTime.of(14, 00), 40));  // 6th.
        add(new Block(LocalTime.of(14, 45), 40));  // 7th.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory"); // Advisory.
          add(blocksToday.get(1));  // For grades 5-6, there is a 0th period.
          add(blocksToday.get(2));
          add("Snack/Recess");
          add(blocksToday.get(3));
          add(blocksToday.get(4));
          add("Lunch");
          add(blocksToday.get(5));
          add("Advisory");
          add(blocksToday.get(6));
          add(blocksToday.get(7));
        }
      };
      printSummary();
    }

  }

}
