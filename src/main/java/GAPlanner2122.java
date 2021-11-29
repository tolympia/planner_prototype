// Contains logic for school days/blocks, block times, and days off.
// Does date/time wrangling.
// TODO: Extract hard coded values.

import java.time.*;
import java.util.*;

public class GAPlanner2122 {

  // TODO: Map dates to special schedules.
  // Keys are LocalDates, values are Day.
  private static Map<LocalDate, Schedule> adjustedSchedules = new TreeMap();

  public static LocalDate msFirstDay = LocalDate.of(2021, Month.SEPTEMBER, 8);
  public static LocalDate msLastDay = LocalDate.of(2022, Month.JUNE, 1);;
  public static LocalDate usFirstDay = LocalDate.of(2021, Month.SEPTEMBER, 13);
  public static LocalDate usLastDay = LocalDate.of(2022, Month.JANUARY, 12);

  public static Schedule getScheduleForDate(LocalDate date, boolean upperSchool, boolean upperclassmen) {
    DayOfWeek day = date.getDayOfWeek();

    Schedule schedule;
    if (upperSchool) {
      boolean flexDay = day == DayOfWeek.MONDAY || day == DayOfWeek.TUESDAY || day == DayOfWeek.THURSDAY;
      if (flexDay) {
        schedule = new USFlexDay(date, upperclassmen);
      } else if (day == DayOfWeek.FRIDAY) {
        schedule = new USFriday(date, upperclassmen);
      } else {  // Wed schedule
        schedule = new USWednesday(date, upperclassmen);
      }
    } else {  // Middle school schedule.
      schedule = new MSMTTh78(date);
    }
    if (schedule.getDayType() == -1) {
      // Not a real school day.
      return null;
    }
    return schedule;

  }

  public static LocalDateTime getTimeForBlock(
    LocalDate date, boolean upperSchool, String blockChar,
    boolean upperclassmen, boolean start) {

    Schedule schedule = getScheduleForDate(date, upperSchool, upperclassmen);

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
    Scanner sc = new Scanner(System.in);
    System.out.println("Division? ms or us:");
    String division = sc.nextLine();
    System.out.println("Which date? yyyy-mm-dd format:");
    String desiredDate = sc.nextLine();
    System.out.println("Which block?:");
    String desiredBlock = sc.nextLine();
    System.out.println("1 for underclassmen, 2 for upper:");
    int year = sc.nextInt();
    LocalDate desiredDateObj = LocalDate.parse(desiredDate);
    boolean us = division == "us";
    Schedule sched = getScheduleForDate(desiredDateObj, us, year == 2);
    System.out.println("That date is a Day " + sched.getDayType());
    ArrayList<String> blocksToday = sched.blocksForDayType();
    System.out.println("Blocks that day :" + blocksToday);
    LocalDateTime blockStart = getTimeForBlock(
      desiredDateObj, us, desiredBlock, year == 2, true);
    System.out.println("Start time for the block:" + blockStart);
  }

  public static void main(String[] args) {
    demo();
  }
}
