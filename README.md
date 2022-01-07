# planner_prototype

A quick and dirty prototype script to automatically add Google Calendar events that correspond to class periods for the GA Upper School schedule.

IMPORTANT:
1. Delete `tokens/` directory if authenticating a new user.
2. Replace calendarId. (Google Calendar web interface -> Settings)
3. `gradle run --console=plain --args=[MODE]` to run, where MODE can be "delete", "ms", or "us".
4. In `build.gradle`, update class name to runner class.
