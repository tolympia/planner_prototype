import java.util.*;
import java.time.*;

public class USWednesday extends Schedule {
<<<<<<< HEAD
  public USWednesday() {
=======
  public USWednesday(boolean upperclassmen) {
>>>>>>> ms
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(9, 10), 50));
        add(new Block(LocalTime.of(10, 10), 50));
        add(new Block(LocalTime.of(11, 10), 50));
        add(new Block(LocalTime.of(12, 10), 50));  // Lunch block.
        add(new Block(LocalTime.of(13, 30), 50));
      }
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
