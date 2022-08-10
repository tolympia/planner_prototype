import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// These are days when we have assemblies from 10:10-10:55am.
// For example: Thursday, September 15, 2022.
// or Tuesday, October 11, 2022.
// or Thursday, October 20, 2022.
// or Thursday, January 5, 2023.
// or Thursday, February 9, 2023.
// or Tuesday, April 25, 2023.
// or Thursday, April 27, 2023.
// or Thursday, May 11, 2023.

public class USAdjusted1 extends USSchedule {
  public USAdjusted1(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 10), 50));
        add(new Block(LocalTime.of(9, 10), 50));
        add(new Block(LocalTime.of(11, 05), 50));
        add(new Block(LocalTime.of(12, 05), 60));  // Lunch block.
        add(new Block(LocalTime.of(13, 40), 60));}
    };
    if (upperclassmen) {
      blocks.set(3, new Block(LocalTime.of(12, 30), 60));
    }
  }

}
