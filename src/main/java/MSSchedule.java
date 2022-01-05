import java.util.*;
import java.time.*;


public abstract class MSSchedule extends Schedule {
  protected int[] DAY_TYPES = {1, 2, 3, 4, 5, 6};  // e.g., Day 1 in Planner.

  public static LocalDate firstDayOfSchool = LocalDate.of(2021, Month.SEPTEMBER, 8);
  public static LocalDate lastDayOfSchool = LocalDate.of(2022, Month.JUNE, 3);

  public MSSchedule(LocalDate date) {
    this.date = date;
    this.dayType = getDayType();
  }

  // Dates where we don't have school.
  public static ArrayList<LocalDate> holidays = new ArrayList<LocalDate>() {
    {
      add(LocalDate.of(2021, Month.SEPTEMBER, 16));
      add(LocalDate.of(2021, Month.OCTOBER, 11));
      add(LocalDate.of(2021, Month.NOVEMBER, 24));
      add(LocalDate.of(2021, Month.NOVEMBER, 25));
      add(LocalDate.of(2021, Month.NOVEMBER, 26));
      add(LocalDate.of(2022, Month.JANUARY, 17));
      add(LocalDate.of(2022, Month.JANUARY, 21));

      add(LocalDate.of(2022, Month.FEBRUARY, 17));
      add(LocalDate.of(2022, Month.FEBRUARY, 18));
      add(LocalDate.of(2022, Month.FEBRUARY, 21));
      add(LocalDate.of(2022, Month.APRIL, 15));
      add(LocalDate.of(2022, Month.APRIL, 29));
      add(LocalDate.of(2022, Month.MAY, 6));
      add(LocalDate.of(2022, Month.MAY, 30));
    }
  };

  static {
    // Winter break is December 20 to January 2.
    LocalDate firstDayOfWinterBreak = LocalDate.of(2021, Month.DECEMBER, 20);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfWinterBreak.plusDays(i));
    }

    // Spring break is March 11 to March 27.
    LocalDate firstDayOfSpringBreak = LocalDate.of(2022, Month.MARCH, 12);
    for (int i = 0; i < 7 * 2; i++) {
      holidays.add(firstDayOfSpringBreak.plusDays(i));
    }
  }

  // ** Helper functions **

  // Return start LocalDateTime for a block based on the date and blockNum.
  public LocalDateTime getStart(LocalDate date, int blockNum) {
    Block blk = blocks.get(blockNum);
    return LocalDateTime.of(date, blk.startTime);
  }

  // Return end LocalDateTime for a block based on the date and blockNum.
  public LocalDateTime getEnd(LocalDate date, int blockNum) {
    Block blk = blocks.get(blockNum);
    return LocalDateTime.of(date, blk.endTime);
  }

  // Returns the blocks for a given day type.
  public ArrayList<String> blocksForDayType() {
    return this.blockNames;
  }

  private boolean isThereSchool(LocalDate date) {
    DayOfWeek day = date.getDayOfWeek();
    if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
      return false;
    }
    if (holidays.contains(date)) {
      return false;
    }
    return true;
  }

  public int getDayType() {
    // Returns -1 if not a school day.
    if (!isThereSchool(this.date)) {
      return -1;
    }
    int dayTypeIdx = 0;
    int dayCounter = 0;
    // TODO: This could be a lot cleaner.
    while (true) {
      LocalDate thisDate = firstDayOfSchool.plusDays(dayCounter);
      if (thisDate.isAfter(lastDayOfSchool)) {
        return -1;
      }
      if (thisDate.equals(this.date)) {
        return DAY_TYPES[dayTypeIdx];
      }
      if (isThereSchool(thisDate)) {
        dayTypeIdx++;
        dayTypeIdx = dayTypeIdx % DAY_TYPES.length;
      }
      dayCounter++;
    }
  }

  public boolean isThereBlockToday(String blockName) {
    if (this.blockNames != null) {
      return this.blockNames.contains(blockName);
    }
    return false;
  }

}
