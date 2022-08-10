import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// For Tuesday, November 22, 2022.
public class USAdjustedIngathering extends USSchedule {
  public USAdjustedIngathering(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 10), 45));
        add(new Block(LocalTime.of(9, 05), 45));
        // Assembly from 10-10:25am.
        add(new Block(LocalTime.of(10, 35), 45));
        add(new Block(LocalTime.of(12, 05), 45));  // Lunch block.
        add(new Block(LocalTime.of(13, 00), 45));}
    };
  }

}
