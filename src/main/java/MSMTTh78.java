import java.util.*;
import java.time.*;


public class MSMTTh78 extends MSSchedule {
  public MSMTTh78(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(7, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(8, 00), 50));   // 1st period.
        add(new Block(LocalTime.of(8, 55), 50));
        add(new Block(LocalTime.of(9, 50), 50));  // 3rd period.
        add(new Block(LocalTime.of(10, 45), 5));  // Snack.
        add(new Block(LocalTime.of(10, 55), 50));  // 4th period.
        add(new Block(LocalTime.of(11, 50), 50));  // 5th period.
        add(new Block(LocalTime.of(12, 45), 25));  // Lunch.
        add(new Block(LocalTime.of(13, 15), 40));  // Advisory.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory (VII/VIII)"); // Advisory.
          add(blocksToday.get(1));
          add(blocksToday.get(2));
          add(blocksToday.get(3));  // 3rd period.
          add("Snack (VII/VIII)");  // Snack.
          add(blocksToday.get(4));  // 4th period.
          add(blocksToday.get(5));  // 5th period.
          add("Lunch (VII/VIII)");  // Lunch.
          add("Advisory (VII/VIII)");
        }
      };
      printSummary();
    }

  }
}
