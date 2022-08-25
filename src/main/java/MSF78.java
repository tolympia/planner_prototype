import java.util.*;
import java.time.*;


public class MSF78 extends MSSchedule {
  public MSF78(LocalDate date, ArrayList<ArrayList<String>> daysToBlocks) {
    super(date);

    this.blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(7, 45), 5));   // Advisory.
        add(new Block(LocalTime.of(7, 55), 45));  // DEAR VII / Arts VIII
        add(new Block(LocalTime.of(8, 45), 40));  // 1st period.
        add(new Block(LocalTime.of(9, 30), 5));  // Snack.
        add(new Block(LocalTime.of(9, 40), 40));  // 2nd period.
        add(new Block(LocalTime.of(10, 25), 40));  // 3rd period.
        add(new Block(LocalTime.of(11, 10), 40));  // 4th period.
        add(new Block(LocalTime.of(11, 55), 25));  // Lunch.
        add(new Block(LocalTime.of(12, 25), 40));  // 5th period.
      }
    };

    if (this.dayType != -1) {
      ArrayList<String> blocksToday = daysToBlocks.get(this.dayType - 1);
      this.blockNames = new ArrayList<String>() {
        {
          add("Advisory (VII/VIII)"); // Advisory.
          add("DEAR VII / Arts VIII");
          add(blocksToday.get(0));
          add("Snack (VII/VIII)");  // Snack.
          add(blocksToday.get(1));
          add(blocksToday.get(2));  // 3rd period.
          add(blocksToday.get(3));  // 4th period.
          add("Lunch (VII/VIII)");  // Lunch.
          add(blocksToday.get(4));  // 5th period.
        }
      };
      printSummary();
    }

  }
}
