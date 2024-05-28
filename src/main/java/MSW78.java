import java.util.*;
import java.time.*;


public class MSW78 extends MSSchedule {
  public MSW78(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 45), 10));   // Advisory.
        add(new Block(LocalTime.of(9, 00), 40));   // 1st period.
        add(new Block(LocalTime.of(9, 45), 40));  // 2nd period.
        add(new Block(LocalTime.of(10, 30), 40));  // 3rd period.
        add(new Block(LocalTime.of(11, 15), 5));  // Snack.
        add(new Block(LocalTime.of(11, 25), 35));  // 4th period.
        add(new Block(LocalTime.of(12, 05), 35));  // 5th period.
        add(new Block(LocalTime.of(12, 45), 25));  // Lunch.
        add(new Block(LocalTime.of(13, 15), 40));  // Assembly.
        // TODO: Figure out VII 6th/7th period, VIII arts situation.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory (VII/VIII)"); // Advisory.
          add(blocksToday.get(0));
          add(blocksToday.get(1));
          add(blocksToday.get(2));  // 3rd period.
          add("Snack (VII/VIII)");  // Snack.
          add(blocksToday.get(3));  // 4th period.
          add(blocksToday.get(4));  // 5th period.
          add("Lunch (VII/VIII)");  // Lunch.
          add("Assembly (VII/VIII)");  // Lunch.
        }
      };
      printSummary();
    }

  }
}
