import java.util.*;
import java.time.*;


public class MSMTTh78 extends Schedule {
  public MSMTTh78(LocalDate date) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 00), 50));   // 1st period.
        add(new Block(LocalTime.of(8, 55), 50));
        add(new Block(LocalTime.of(9, 50), 50));  // 3rd period.
        add(new Block(LocalTime.of(10, 45), 10));  // Snack.
        add(new Block(LocalTime.of(10, 55), 50));  // 4th period.
        add(new Block(LocalTime.of(11, 50), 50));  // 5th period.
        add(new Block(LocalTime.of(12, 45), 25));  // Lunch.
      }
    };
  }
}
