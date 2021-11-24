// Contains logic for school days/blocks, block times, and days off.
// Does date/time wrangling.
// TODO: Extract hard coded values.

import java.time.*;
import java.util.*;

public class GAPlanner2122 {

  // TODO: Map dates to special schedules.
  // Keys are LocalDates, values are Day.
  private static Map<LocalDate, Schedule> adjustedSchedules = new TreeMap();

  public static boolean isThereClassToday(LocalDate date, String block, Map schoolDays) {
    // TODO: Look up specific schedule for this day (Use adjustedSchedules)
    Schedule today = getScheduleForDate(date, false);
    ArrayList<String> blocksToday = today.blocksForDayType();
    return blocksToday.contains(block);
  }

  public static Schedule getScheduleForDate(LocalDate date, boolean upperclassmen) {
    DayOfWeek day = date.getDayOfWeek();

    Schedule schedule;
    boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;
    if (flexDay) {
      schedule = new USFlexDay(date, upperclassmen);
    } else if (day == DayOfWeek.FRIDAY) {
      schedule = new USFriday(date, upperclassmen);
    } else {  // Wed schedule
      schedule = new USWednesday(date, upperclassmen);
    }
    return schedule;

  }

  public static LocalDateTime getTimeForBlock(LocalDate date, String blockChar, boolean upperclassmen, boolean start) {

    Schedule schedule = getScheduleForDate(date, upperclassmen);

    // TODO: Move this logic to Schedule.
    ArrayList<String> blockCharsToday = schedule.blocksForDayType();

    // What blockNum is the given block?
    int blockNum = blockCharsToday.indexOf(blockChar);
    // If -1, there is no block that day.
    if (blockNum == -1) {
      return null;
    }

    if (start) {
      return schedule.getStart(date, blockNum);
    } else {
      return schedule.getEnd(date, blockNum);
    }
  }

  public static void demo() {
    Map schoolDays = Schedule.getSchoolDaysMap();
    ArrayList<String> schoolDates = new ArrayList<String>(schoolDays.keySet());

    Scanner sc = new Scanner(System.in);
    System.out.println("Which date? yyyy-mm-dd format:");
    String desiredDate = sc.nextLine();
    System.out.println("Which block?:");
    String desiredBlock = sc.nextLine();
    System.out.println("1 for underclassmen, 2 for upper:");
    int year = sc.nextInt();
    LocalDate desiredDateObj = LocalDate.parse(desiredDate);
    System.out.println("That date is a Day " + schoolDays.get(desiredDate));
    Schedule sched = getScheduleForDate(desiredDateObj, year == 2);
    ArrayList<String> blocksToday = sched.blocksForDayType();
    System.out.println("Blocks that day :" + blocksToday);
    LocalDateTime blockStart = getTimeForBlock(desiredDateObj, desiredBlock, year == 2, true);
    System.out.println("Start time for the block:" + blockStart);
  }

  public static void main(String[] args) {
    // demo();
  }
}
