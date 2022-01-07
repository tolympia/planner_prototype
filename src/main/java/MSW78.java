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
        add(new Block(LocalTime.of(13, 15), 40));  // Assembly.
        // TODO: Figure out VII 6th/7th period, VIII arts situation.
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
          add("Assembly (VII/VIII)");  // Lunch.
        }
      };
      printSummary();
    }

  }
}
