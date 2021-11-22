import java.util.*;
import java.time.*;

public class USFlexDay extends Schedule {
<<<<<<< HEAD
  public USFlexDay() {
=======
  public USFlexDay(boolean upperclassmen) {
>>>>>>> ms
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(8, 10), 60));
        add(new Block(LocalTime.of(9, 20), 60));
        add(new Block(LocalTime.of(11, 05), 50));
        add(new Block(LocalTime.of(12, 05), 60));  // Lunch block.
        add(new Block(LocalTime.of(13, 40), 60));}
    };
<<<<<<< HEAD
  }

  public void adjustForUpperclassmen() {
    blocks.set(3, new Block(LocalTime.of(12, 30), 60));
  }
=======
    if (upperclassmen) {
      blocks.set(3, new Block(LocalTime.of(12, 30), 60));
    }
  }

>>>>>>> ms
}
