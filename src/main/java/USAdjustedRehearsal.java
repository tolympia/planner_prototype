import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// For Wednesday, May 17, 2023.
public class USAdjustedRehearsal extends USSchedule {
  public USAdjustedRehearsal(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(9, 10), 30));
        add(new Block(LocalTime.of(9, 50), 30));
        add(new Block(LocalTime.of(10, 30), 30));
        add(new Block(LocalTime.of(11, 10), 30));
        add(new Block(LocalTime.of(11, 50), 30));}  // Lunch block.
    };
    if (upperclassmen) {
      blocks.set(4, new Block(LocalTime.of(12, 35), 30));
    }
  }

}
