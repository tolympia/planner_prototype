import java.util.*;
import java.time.*;

public class USFriday extends Schedule {
  public USFriday(boolean upperclassmen) {
    blocks = new ArrayList<Block>() {
      {
          add(new Block(LocalTime.of(8, 10)));
          add(new Block(LocalTime.of(9, 20)));
          add(new Block(LocalTime.of(10, 30)));
          add(new Block(LocalTime.of(11, 40)));  // Lunch block.
          add(new Block(LocalTime.of(13, 15)));
      }
    };
    if (upperclassmen) {
      blocks.set(3, new Block(LocalTime.of(12, 05), 60));
    }
  }
}
