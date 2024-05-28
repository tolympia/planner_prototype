# planner_prototype

LAST UPDATED: May 2024

AUTHOR: Tina O. Zhu

A quick and dirty prototype script to automatically add Google Calendar events that correspond to class periods for the GA ~Upper School~ (deprecated, use version 2 instead) and Middle School schedules.

USAGE NOTES:
1. Main "runner" file is called `ClassTimes.java`.
1. Find your "calendar ID" via Google Calendar web interface -> Settings.
1. Add teacher's schedule as a .csv file to `schedules/` directory (see `example.csv`, `will.csv`, or `lee56.csv` for examples).
1. `./gradlew run --console=plain --args=[MODE]` to run, where MODE can be "delete", "ms", "days" (for MS), or "us".
- Use mode "ms" to generate events for an MS schedule based on .csv file listing a particular teacher's schedule.
- Use mode "days" to generate "Day [X]" all-day events based on the MS rotation.
- **(CAUTION)** Use mode "delete" to delete ALL events on a particular calendar.
     
Don't forget:
1. Delete `tokens/` directory if authenticating a new user.
1. In `build.gradle`, update class name to runner class if `ClassTimes.java` filename is ever changed.
1. In `settings.gradle.kts`, update `rootProject.name` if root directory name (`planner_prototype`) is ever changed.
