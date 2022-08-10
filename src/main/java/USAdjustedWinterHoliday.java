import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

// For Friday, December 16, 2022.
public class USAdjustedWinterHoliday extends USSchedule {
  public USAdjustedWinterHoliday(LocalDate date, boolean upperclassmen) {
    super(date);
    blocks = new ArrayList<Block>() {
      {
        add(new Block(LocalTime.of(10, 00), 35));
        add(new Block(LocalTime.of(10, 45), 35));
        add(new Block(LocalTime.of(11, 30), 35));
        add(new Block(LocalTime.of(12, 55), 35));
        add(new Block(LocalTime.of(13, 40), 35));}
    };
  }

}
