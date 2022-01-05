import java.util.*;
import java.time.*;


public class MSW78 extends MSSchedule {
  public MSW78(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(9, 00), 35));   // 1st period.
        add(new Block(LocalTime.of(9, 40), 35));  // 2nd period.
        add(new Block(LocalTime.of(10, 20), 35));  // 3rd period.
        add(new Block(LocalTime.of(11, 05), 10));  // Snack.
        add(new Block(LocalTime.of(11, 20), 35));  // 4th period.
        add(new Block(LocalTime.of(12, 00), 35));  // 5th period.
        add(new Block(LocalTime.of(12, 35), 35));  // Lunch.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory"); // Advisory.
          add(blocksToday.get(0));
          add(blocksToday.get(1));
          add(blocksToday.get(2));  // 3rd period.
          add("Snack");  // Snack.
          add(blocksToday.get(3));  // 4th period.
          add(blocksToday.get(4));  // 5th period.
          add("Lunch");  // Lunch.
        }
      };
      printSummary();
    }

  }
}
