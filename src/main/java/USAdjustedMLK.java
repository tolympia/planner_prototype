import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// For Thursday, January 26, 2023.
public class USAdjustedMLK extends USSchedule {
  public USAdjustedMLK(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(9, 40), 45));
        add(new Block(LocalTime.of(10, 35), 45));
        add(new Block(LocalTime.of(11, 30), 45));
        add(new Block(LocalTime.of(12, 55), 45));
        add(new Block(LocalTime.of(13, 50), 45));
      }
    };
    if (upperclassmen) {
      blocks.set(2, new Block(LocalTime.of(12, 00), 45));
    }
  }

}
