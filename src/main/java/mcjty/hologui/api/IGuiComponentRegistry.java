package mcjty.hologui.api;

import mcjty.hologui.api.components.*;

/**
 * Use this interface to create basic components for a gui. Typically you call this in
 * your IGuiTile.createGui() implementation.
 *
 * All gui's work in a screen resolution of 8x8. You can use coordinates like 3.5 or 4.1 for
 * precise position control. The 'width' and 'height' of a component are only used for
 * components that can be interacted with. They have no effect on rendering.
 */
public interface IGuiComponentRegistry {

    /**
     * Create a button which shows an icon instead of text
     */
    IIconButton iconButton(double x, double y, double w, double h);

    /**
     * An icon with no interaction
     */
    IIcon icon(double x, double y, double w, double h);

    /**
     * An icon cycle button supporting multiple values.
     */
    IIconChoice iconChoice(double x, double y, double w, double h);

    /**
     * An icon toggle button supporting two icons.
     */
    IIconToggle iconToggle(double x, double y, double w, double h);

    /**
     * Create a number label. This cannot be interacted with
     */
    INumber number(double x, double y, double w, double h);

    /**
     * Create a panel that can itself hold other components
     */
    IPanel panel(double x, double y, double w, double h);

    /**
     * Create a non-interactable itemstack display
     */
    IStackIcon stackIcon(double x, double y, double w, double h);

    /**
     * Create an interactible itemstack display
     */
    IStackToggle stackToggle(double x, double y, double w, double h);

    /**
     * Just text
     */
    IText text(double x, double y, double w, double h);

    /**
     * A text button
     */
    IButton button(double x, double y, double w, double h);

    /**
     * A set of inventory slots
     */
    ISlots slots(double x, double y, double w, double h);

    /**
     * A set of inventory slots for the player
     */
    IPlayerSlots playerSlots(double x, double y, double w, double h);
}
