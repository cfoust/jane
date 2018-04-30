package com.sqweebloid.jane.controls;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.***REMOVED***.api.Client;
import net.***REMOVED***.api.MenuEntry;
import net.***REMOVED***.api.Point;
import net.***REMOVED***.api.events.MenuOpened;
import net.***REMOVED***.api.widgets.Widget;
import net.***REMOVED***.api.widgets.WidgetInfo;
import net.***REMOVED***.client.ui.ClientUI;
import net.***REMOVED***.client.util.Text;

import net.***REMOVED***.client.plugins.jane.JanePlugin;

/**
 * Performs everything related to raw user input like clicks and
 * keypresses.
 */
@Singleton
public class Input {
    @Inject
    private ClientUI clientUI;

    @Inject
    private Client client;

    public void sleep(long delay) {
        try { Thread.sleep(delay); } catch (Exception e) {}
    }

    public int rand(int max) {
        if (max <= 0) {
            return 0;
        }

        return ThreadLocalRandom.current().nextInt(0, max);
    }

    int lastClickY = 0;

    private void doClick(int x, int y, int button) {
        lastClickY = y;
        Component component = clientUI.client.getComponent(0);

        int mask = button == MouseEvent.BUTTON1 ?
            MouseEvent.BUTTON1_MASK :
            MouseEvent.BUTTON3_MASK;

        //System.out.printf("Clicking at %d %d with button %d\n", x, y, button);

        component.dispatchEvent(new MouseEvent(component,
                    MouseEvent.MOUSE_MOVED,
                    System.currentTimeMillis(),
                    0,
                    x, y,
                    0,
                    button == MouseEvent.BUTTON3));

        sleep(50);

        component.dispatchEvent(new MouseEvent(component,
                    MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(),
                    mask,
                    x, y,
                    0,
                    button == MouseEvent.BUTTON3));

        sleep(30);

        component.dispatchEvent(new MouseEvent(component,
                    MouseEvent.MOUSE_RELEASED,
                    System.currentTimeMillis(),
                    mask,
                    x, y,
                    0,
                    button == MouseEvent.BUTTON3));
    }

    // Tighten up the bounds just a little bit
    private static final double FUZZ_FACTOR = 0.3;
    public Point getPointInBounds(Rectangle r) {
        int horizontal = (int) Math.floor(FUZZ_FACTOR * ((double) r.getWidth()));
        int vertical = (int) Math.floor(FUZZ_FACTOR * ((double) r.getHeight()));

        int x = (int) r.getX() + horizontal;
        int y = (int) r.getY() + vertical;

        return new Point(x + rand((int) r.getWidth() - horizontal),
                y + rand((int) r.getHeight() - horizontal));
    }

    public void click(Rectangle r) {
        Point dest = getPointInBounds(r);
        click((int) dest.getX(), (int) dest.getY());
    }

    public void rightClickBounds(Rectangle r) {
        Point dest = getPointInBounds(r);
        rightClick((int) dest.getX(), (int) dest.getY());
    }

    public void click(int x, int y) {
        doClick(x, y, MouseEvent.BUTTON1);
    }

    public void clickMenu(String verb, String target) {
    }

    public void onMenuOpened(MenuOpened event) {
        entries.clear();

        for (MenuEntry entry : event.getMenuEntries()) {
            entries.add(0, entry);
        }
    }

    public void rightClick(int x, int y) {
        doClick(x, y, MouseEvent.BUTTON3);
    }

    private void doKey(int keyCode, boolean down) {
        Component component = clientUI.client.getComponent(0);

        component.dispatchEvent(new KeyEvent(component,
                    down ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED,
                    System.currentTimeMillis(),
                    0,
                    keyCode,
                    (char) 0));
    }

    public void pressKey(int keyCode) {
        doKey(keyCode, true);
    }

    public void releaseKey(int keyCode) {
        doKey(keyCode, false);
    }

    public void typeKey(int keyCode) {
        doKey(keyCode, true);
        doKey(keyCode, false);
    }

    public void delay(long time) {
        try { Thread.sleep(time); } catch (Exception e) {}
    }

    List<MenuEntry> entries = new ArrayList();

    public void dumpWidgets(boolean onlyVisible) {
        System.out.println("DUMPING WIDGETS");

        net.***REMOVED***.api.Point p = client.getMouseCanvasPosition();

        List<Widget> within = new ArrayList();
        for (int i = 0; i < 65536; i++) {
            for (int j = 0; j < 1000; j++) {
                Widget w = client.getWidget(i, j);

                if (w == null || (w.isHidden() && onlyVisible)) continue;

                Rectangle bounds = w.getBounds();
                if (bounds != null && bounds.contains(p.getX(), p.getY())) {
                    within.add(w);
                }

                //System.out.printf("%d %d -> %s\n", i, j, formatWidget(w));
                recurseRootNode(i, j, w);
            }
        }
        System.out.println("DUMPED WIDGETS");

        if (within.size() > 0) {
            System.out.println("THESE WIDGETS CONTAINED CURSOR");

            for (Widget w : within) {
                System.out.printf("%s\n", formatWidget(w));
            }
        }
    }

    private void recurseRootNode(int groupId, int childId, Widget w) {
        printBlock();
        String rootString = formatWidget(w);
        System.out.printf("ROOT group=%d child=%d\n", groupId, childId);
        System.out.println(rootString);
        printBlock();

        Widget[] children = w.getChildren();
        if (children != null) {
            for (Widget child : w.getChildren()) {
                recurseNode(0, child);
            }
        }

        printBlock();
    }

    public void recurseNode(int indent, Widget w) {
        String base = "";

        for (int i = 0; i < indent; i++) {
            base += "----";
        }

        System.out.printf("%s%s\n", base, formatWidget(w));
        Widget[] children = w.getChildren();

        if (children != null) {
            for (Widget child : w.getChildren()) {
                recurseNode(indent + 1, child);
            }
        }
    }

    private void printBlock() {
        System.out.println("=======================================");
    }

    public String formatWidget(Widget w) {
        if (w == null) return null;

        String widgetString = String.format("id=%d parentId=%d ", w.getId(), w.getParentId());

        Widget[] children = w.getChildren();
        if (children != null) {
            widgetString += String.format("children=%d ", children.length);
        }

        String name = w.getName();
        if (name != null) {
            widgetString += String.format("name=%s ", name);
        }

        String text = w.getText();
        if (text != null) {
            widgetString += String.format("text=%s ", text);
        }

        Rectangle bounds = w.getBounds();
        if (bounds != null) {
            widgetString += String.format("bounds=%s ", bounds.toString());
        }

        return widgetString;
    }
}
