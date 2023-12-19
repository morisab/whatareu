package com.whatareu.gameinterface;

import java.awt.Graphics2D;
import java.util.function.Consumer;

public interface ApplicationContext {
   ScreenInfo getScreenInfo();

   void exit(int var1);

   Event pollEvent();

   Event pollOrWaitEvent(long var1);

   void renderFrame(Consumer<Graphics2D> var1);
}
