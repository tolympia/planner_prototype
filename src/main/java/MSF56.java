import java.util.*;
import java.time.*;


public class MSF56 extends MSSchedule {
  public MSF56(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(7, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(8, 00), 40));   // 0th period.
        add(new Block(LocalTime.of(8, 45), 40));   // 1st period.
        add(new Block(LocalTime.of(9, 30), 40));  // 2nd period.
        add(new Block(LocalTime.of(10, 15), 5));  // Snack.
        add(new Block(LocalTime.of(10, 25), 40));  // 3rd period.
        add(new Block(LocalTime.of(11, 10), 40));  // 4th period.
        add(new Block(LocalTime.of(11, 55), 40));  // 5th period.
        add(new Block(LocalTime.of(12, 40), 30));  // Lunch.
        add(new Block(LocalTime.of(13, 10), 40));  // 6th.
        add(new Block(LocalTime.of(13, 55), 40));  // 7th.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory"); // Advisory.
          add(blocksToday.get(0));  // 0th.
          add(blocksToday.get(1));
          add(blocksToday.get(2));
          add("Snack");
          add(blocksToday.get(3));
          add(blocksToday.get(4));
          add(blocksToday.get(5));
          add("Lunch");
          add(blocksToday.get(6));
          add(blocksToday.get(7));  // 7th.
        }
      };
      printSummary();
    }

  }
}
