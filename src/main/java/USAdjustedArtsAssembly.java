import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// For Fri, Oct. 28, 2022.
public class USAdjustedArtsAssembly extends USSchedule {
  public USAdjustedArtsAssembly(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(9, 45), 40));
        add(new Block(LocalTime.of(10, 35), 40));
        add(new Block(LocalTime.of(11, 25), 40));
        add(new Block(LocalTime.of(12, 40), 40));
        add(new Block(LocalTime.of(13, 30), 40));
      }
    };
  }
}
