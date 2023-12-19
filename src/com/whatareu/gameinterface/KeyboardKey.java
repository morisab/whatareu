package com.whatareu.gameinterface;

import java.util.Arrays;

public enum KeyboardKey {
   UP(38),
   DOWN(40),
   LEFT(37),
   RIGHT(39),
   P(80),
   UNDEFINED(0);

   private final int key;
   private static final KeyboardKey[] keys;

   static {
      int max = 0;
      KeyboardKey[] values = values();
      KeyboardKey[] var5 = values;
      int var4 = values.length;

      for (int var3 = 0; var3 < var4; ++var3) {
         KeyboardKey key = var5[var3];
         max = Math.max(max, key.key);
      }

      KeyboardKey[] array = new KeyboardKey[max + 1];
      Arrays.fill(array, UNDEFINED);
      KeyboardKey[] var6 = values;
      int var9 = values.length;

      for (var4 = 0; var4 < var9; ++var4) {
         KeyboardKey key = var6[var4];
         array[key.key] = key;
      }

      keys = array;
   }

   private KeyboardKey(int key) {
      this.key = key;
   }

   static KeyboardKey key(int key) {
      return key >= keys.length ? UNDEFINED : keys[key];
   }
}
