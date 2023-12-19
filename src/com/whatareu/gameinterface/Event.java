package com.whatareu.gameinterface;

import java.awt.geom.Point2D.Float;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public final class Event {
   private final Event.Action action;
   private final Object data;
   private final int modifiers;
   private Set<Event.Modifier> modifierSet;

   Event(Event.Action action, int modifiers, Object data) {
      this.action = action;
      this.modifiers = modifiers;
      this.data = data;
   }

   public Event.Action getAction() {
      return this.action;
   }

   public Set<Event.Modifier> getModifiers() {
      return this.modifierSet != null ? this.modifierSet
            : (this.modifierSet = Event.Modifier.modifierSet(this.modifiers));
   }

   public Float getLocation() {
      return !(this.data instanceof Float) ? null : (Float) this.data;
   }

   public KeyboardKey getKey() {
      return !(this.data instanceof KeyboardKey) ? null : (KeyboardKey) this.data;
   }

   public String toString() {
      return this.action + " " + this.getModifiers() + " (" + this.data + ')';
   }

   public static enum Action {
      POINTER_DOWN,
      POINTER_UP,
      POINTER_MOVE,
      KEY_PRESSED,
      KEY_RELEASED;
   }

   public static enum Modifier {
      META(256),
      CTRL(128),
      ALT(512),
      SHIFT(64),
      ALT_GR(8192);

      final int modifier;
      private static final Event.Modifier[] MODIFIERS = values();

      private Modifier(int modifier) {
         this.modifier = modifier;
      }

      static Set<Event.Modifier> modifierSet(int intModifiers) {
         if (intModifiers == 0) {
            return Collections.emptySet();
         } else {
            EnumSet<Event.Modifier> set = EnumSet.noneOf(Event.Modifier.class);
            Event.Modifier[] var5;
            int var4 = (var5 = MODIFIERS).length;

            for (int var3 = 0; var3 < var4; ++var3) {
               Event.Modifier modifier = var5[var3];
               if ((intModifiers & modifier.modifier) != 0) {
                  set.add(modifier);
               }
            }

            return set;
         }
      }
   }
}
