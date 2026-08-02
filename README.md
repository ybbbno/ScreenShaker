# ScreenShaker

A simple Paper plugin that shakes players' screens by randomly offsetting their yaw and pitch for a configurable number of ticks.

## Requirements

- Paper 1.21+

## How it works

Every tick, for each player currently being shaken:

1. The remaining tick counter is decremented.
2. A random yaw and pitch offset is generated within the configured ranges (alternating between min-side and max-side each tick for a more natural shake).
3. The player's rotation is updated with the offset.

When the tick counter reaches 0 (or the player goes offline), the shake is automatically removed.

## Commands

### `/shake`

**Usage:**
```
/shake <player|@a> <ticks> [minYaw maxYaw minPitch maxPitch]
```

| Argument | Description | Default |
|----------|-------------|---------|
| `player` / `@a` | Target player name or all online players | — |
| `ticks` | Duration of the shake in ticks (20 ticks ≈ 1 second) | — |
| `minYaw` `maxYaw` | Yaw offset range | `-1` `1` |
| `minPitch` `maxPitch` | Pitch offset range | `-1` `1` |

**Examples:**
```
/shake Steve 40
/shake @a 100
/shake Steve 60 -2 2 -1.5 1.5
```

- If only `ticks` is provided, the default bounds (`-1` to `1` for both yaw and pitch) are used and the duration is **added** to any existing shake on that player.
- If custom bounds are provided, the shake is **set** (replaces any existing shake) with those bounds.

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `commandshaker.shake` | Allows use of the `/shake` command | `op` (or as configured) |

## License

This project is open source under the MIT License. Feel free to modify and distribute — credit is appreciated but not required.