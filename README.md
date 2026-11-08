# HoloGui
HoloGui is a mod about holographic gui's

## Text entry and color picking

The component registry now provides `textEntry(x, y, width, height)` and
`colorPicker(x, y, width, height)` in the standard 8 × 8 GUI coordinates.

Give each text entry a stable, unique `name`, a live `text` supplier, an optional
`maxLength` (up to 2048), and a server-side `changed` callback. Clicking the field
opens a multiline keyboard editor over the still-visible world. It supports
selection, clipboard shortcuts, arrows, Home/End, Backspace/Delete and Enter.
Escape or clicking outside commits once and releases keyboard focus. The server
accepts changes only for the player and named component that initiated editing,
while the GUI, tag and distance still match. GUI owners should also validate any
external editing context in their callback, such as the held item or block.

A color picker exposes a live `color` supplier and a server-side `selected`
callback receiving opaque `0xRRGGBB`. Its bottom strip changes hue; clicking the
shade palette selects a color. Use `switchGui` or `switchTag` in that callback
to return to the previous GUI, and provide a separate close button when needed.
Text buttons also accept `scale(float)` for compact control labels. Add
`swatch(() -> selectedColor)` to give a color-picker button the shared control
frame, a vertically centered label and a live color sample. Both the label and
swatch open the picker through the button's normal hit callback.

Keyboard editor classes remain client-only; text sanitization and edit-session
validation run on dedicated servers. Text sends one commit and a small focus
keepalive every five seconds while editing, rather than a packet per key.

## Shared cycle and numeric controls

`textChoice` renders its current choice as one framed button with a two-arrow loop
on the right. The whole button advances the choice. Icon choices carry the same
loop badge. The symbol is drawn as geometry and does not depend on font glyphs.

Use `integerSlider(x, y, width, height)` for bounded numeric settings. Give it a
stable unique `name` to enable dragging, a `label`, `range(min, max)`, a live
`getter((player, gui) -> value)` and a server-side `changed` callback. Clicking
or holding the track selects values; the outer arrows adjust by one (or the
configured `step`). `logarithmic(true)` keeps very large positive ranges usable,
including inventory thresholds, while preserving exact arrow steps. Use
`suffix("%")` (or another unit) to append units to the displayed value.

Dragging uses the existing in-world cursor: hold the attack button and aim along
the track. Updates are limited to ten per second, and only intent is sent. The
server recomputes the ray using its player eye position and the submitted view
direction, then calculates and bounds the value and accepts updates only for
the player and named slider that started the drag. Release commits the final
position. GUI changes, leaving the interaction range and expired sessions end
the drag. Use the changed callback to preserve machine-specific validation and
side effects.

## Conditional panels

Use `panel(...).visible(() -> condition)` to show a group only when it applies to
the current settings. The condition is evaluated live on both sides: hidden
panels do not render, expose named children, hover, or handle clicks. Child
coordinates remain absolute in the normal GUI coordinate system. This also keeps
hidden sliders and text entries unavailable to server interaction handlers.


Development target: Minecraft 1.21.1, NeoForge 21.1.215, Java 21, branch `1.21_neo`.
Build with `./gradlew build` standalone, or `./gradlew :ModHoloGui:build` from MultiWorkspace.
The workspace uses the included McJtyLib and The One Probe sources. Ariente and ArienteWorld also use included HoloGui/Ariente sources. Published Maven artifacts are standalone fallbacks.
Unpublished port dependencies can be provided with `-Pariente_dependency_repository=<repository URL>`; `-Pgradletools_path=<file>` selects an explicit shared Gradle script.
