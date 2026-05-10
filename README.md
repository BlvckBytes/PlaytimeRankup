# PlaytimeRewards

A simple yet effective play-time and afk-time tracker with global as well as calendar-bucketed
statistics that directly integrates with LuckPerms as to append players with new parent-groups.

For now, this plugin remains as an internal tool and will not be provided publicly - feel free
to do whatever your heart desires with the source-code at your own risk. It is also still a
work-in-progress, being extended by features and having bugs fixed as time goes on.

## Placeholders

`play` refers to the time actively played, while `afk` represents the duration spent in AFK-mode,
as marked by EssentialsX. `global` is the total, all-time statistic while the calendar-buckets
`day`, `week`, `month` and `year` are calendar-aligned accumulators, based on the configured locale.

Accessing just the statistic itself is always relative to the executing player (or hologram-viewer, etc.),
while the suffix `_top_<N>` yields the top `N` place of all statistics within the current scope; by itself,
it returns the time-value and with another suffix of `_name`, the last known name of that player is returned.

```
%playtime_play_global%
%playtime_play_global_top_<N>%
%playtime_play_global_top_<N>_name%
%playtime_play_day%
%playtime_play_day_top_<N>%
%playtime_play_day_top_<N>_name%
%playtime_play_week%
%playtime_play_week_top_<N>%
%playtime_play_week_top_<N>_name%
%playtime_play_month%
%playtime_play_month_top_<N>%
%playtime_play_month_top_<N>_name%
%playtime_play_year%
%playtime_play_year_top_<N>%
%playtime_play_year_top_<N>_name%

%playtime_afk_global%
%playtime_afk_global_top_<N>%
%playtime_afk_global_top_<N>_name%
%playtime_afk_day%
%playtime_afk_day_top_<N>%
%playtime_afk_day_top_<N>_name%
%playtime_afk_week%
%playtime_afk_week_top_<N>%
%playtime_afk_week_top_<N>_name%
%playtime_afk_month%
%playtime_afk_month_top_<N>%
%playtime_afk_month_top_<N>_name%
%playtime_afk_year%
%playtime_afk_year_top_<N>%
%playtime_afk_year_top_<N>_name%
```