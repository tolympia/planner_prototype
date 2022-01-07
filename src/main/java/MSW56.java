import java.util.*;
import java.time.*;


public class MSW56 extends MSSchedule {
  public MSW56(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(9, 00), 30));   // 1st period.
        add(new Block(LocalTime.of(9, 35), 30));  // 2nd period.
        add(new Block(LocalTime.of(10, 10), 15));  // Snack/recess.
        add(new Block(LocalTime.of(10, 30), 30));  // 3rd.
        add(new Block(LocalTime.of(11, 05), 35));  // 4th.
        add(new Block(LocalTime.of(11, 45), 35));  // Lunch.
        add(new Block(LocalTime.of(12, 25), 45));  // 5th.
        add(new Block(LocalTime.of(13, 15), 40));  // Assembly.
        add(new Block(LocalTime.of(14, 00), 40));  // 6th.
        add(new Block(LocalTime.of(14, 45), 40));  // 7th.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory (V/VI)"); // Advisory.
          add(blocksToday.get(1)); // 1st period (NOT 0th).
          add(blocksToday.get(2));
          add("Snack/Recess (V/VI)");  // Snack.
          add(blocksToday.get(3));
          add(blocksToday.get(4));
          add("Lunch (V/VI)");  // Lunch.
          add(blocksToday.get(5));
          add("Assembly (V/VI)");
          add(blocksToday.get(6));
          add(blocksToday.get(7));
        }
      };
      printSummary();
    }

  }
}
