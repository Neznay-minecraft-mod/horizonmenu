package com.horizonmenu;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

/** Screen navigation helpers (reflection keeps us safe from small API renames between 26.x builds). */
public final class Nav {
    private Nav() {}

    public static void open(Screen screen) {
        Minecraft mc = Minecraft.getInstance();
        Object gui = field(mc, "gui");
        if (gui != null && invoke(gui, "setScreen", screen)) return;
        if (invoke(mc, "setScreenAndShow", screen)) return;
        invoke(mc, "setScreen", screen);
    }

    public static Screen current() {
        Minecraft mc = Minecraft.getInstance();
        Object gui = field(mc, "gui");
        if (gui != null) {
            Object r = call0(gui, "screen");
            if (r instanceof Screen s) return s;
        }
        Object f = field(mc, "screen");
        return f instanceof Screen s ? s : null;
    }

    /** Opens a vanilla (or other mod) screen by class name, passing parent (and Options if needed). */
    public static boolean openVanilla(String className, Screen parent) {
        try {
            Class<?> cls = Class.forName(className);
            Object options = field(Minecraft.getInstance(), "options");
            for (Constructor<?> c : cls.getDeclaredConstructors()) {
                Class<?>[] pt = c.getParameterTypes();
                Object[] args = new Object[pt.length];
                boolean ok = true;
                for (int i = 0; i < pt.length; i++) {
                    if (pt[i].isInstance(parent)) {
                        args[i] = parent;
                    } else if (options != null && pt[i].isInstance(options)) {
                        args[i] = options;
                    } else {
                        ok = false;
                        break;
                    }
                }
                if (!ok) continue;
                c.setAccessible(true);
                Object o = c.newInstance(args);
                if (o instanceof Screen s) {
                    open(s);
                    return true;
                }
            }
        } catch (Throwable t) {
            HorizonMenuClient.LOGGER.warn("Cannot open {}: {}", className, t.toString());
        }
        return false;
    }

    static Object field(Object o, String name) {
        for (Class<?> c = o.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field f = c.getDeclaredField(name);
                f.setAccessible(true);
                return f.get(o);
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    private static Object call0(Object target, String name) {
        try {
            for (Method m : target.getClass().getMethods()) {
                if (m.getName().equals(name) && m.getParameterCount() == 0) {
                    return m.invoke(target);
                }
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    private static boolean invoke(Object target, String name, Object arg) {
        for (Method m : target.getClass().getMethods()) {
            if (!m.getName().equals(name) || m.getParameterCount() != 1) continue;
            Class<?> p = m.getParameterTypes()[0];
            if (arg != null && !p.isInstance(arg)) continue;
            try {
                m.invoke(target, arg);
                return true;
            } catch (InvocationTargetException e) {
                HorizonMenuClient.LOGGER.warn("{} failed: {}", name, e.getCause());
                return true;
            } catch (Throwable t) {
                // try next candidate
            }
        }
        return false;
    }

    /** Makes a vanilla button invisible but still clickable. */
    public static void hide(Object widget) {
        try {
            for (Method m : widget.getClass().getMethods()) {
                if (m.getName().equals("setAlpha") && m.getParameterCount() == 1) {
                    m.invoke(widget, 0.0F);
                    return;
                }
            }
        } catch (Throwable ignored) {
        }
        Object f = null;
        for (Class<?> c = widget.getClass(); c != null; c = c.getSuperclass()) {
            try {
                Field fl = c.getDeclaredField("alpha");
                fl.setAccessible(true);
                fl.setFloat(widget, 0.0F);
                return;
            } catch (Throwable ignored) {
            }
        }
    }

    public static boolean hovered(Object widget) {
        for (String name : new String[] {"isHoveredOrFocused", "isHovered"}) {
            try {
                for (Method m : widget.getClass().getMethods()) {
                    if (m.getName().equals(name) && m.getParameterCount() == 0) {
                        Object r = m.invoke(widget);
                        if (r instanceof Boolean b) return b;
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return false;
    }

    public static void quit() {
        Minecraft mc = Minecraft.getInstance();
        call0(mc, "stop");
    }
}
