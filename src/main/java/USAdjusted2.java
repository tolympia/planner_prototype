import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// For example: Thursday, September 29, 2022.
public class USAdjusted2 extends USSchedule {
  public USAdjusted2(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 10), 45));
        add(new Block(LocalTime.of(9, 05), 45));
        // Assembly from 10-11am.
        add(new Block(LocalTime.of(11, 10), 45));
        add(new Block(LocalTime.of(12, 05), 60));  // Lunch block.
        add(new Block(LocalTime.of(13, 40), 60));}
    };
    if (upperclassmen) {
      blocks.set(3, new Block(LocalTime.of(12, 30), 60));
    }
  }

}
