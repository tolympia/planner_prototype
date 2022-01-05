import java.util.*;
import java.time.*;


public class USFriday extends USSchedule {
  public USFriday(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 10), 60));
        add(new Block(LocalTime.of(9, 20), 60));
        add(new Block(LocalTime.of(10, 30), 60));
        add(new Block(LocalTime.of(11, 40), 60));  // Lunch block.
        add(new Block(LocalTime.of(13, 15), 60));
      }
    };
    if (upperclassmen) {
      blocks.set(3, new Block(LocalTime.of(12, 05), 60));
    }
  }
}
