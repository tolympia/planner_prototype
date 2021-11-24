import java.util.*;
import java.time.*;

// TODO: Fix block durations!

public class USFriday extends Schedule {
  public USFriday(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 10), 50));
        add(new Block(LocalTime.of(9, 20), 50));
        add(new Block(LocalTime.of(10, 30), 50));
        add(new Block(LocalTime.of(11, 40), 30));  // Lunch block.
        add(new Block(LocalTime.of(13, 15), 50));
      }
    };
    if (upperclassmen) {
      blocks.set(3, new Block(LocalTime.of(12, 05), 60));
    }
  }
}
