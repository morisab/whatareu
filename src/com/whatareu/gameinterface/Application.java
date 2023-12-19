package com.whatareu.gameinterface;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public final class Application {
   private Application() {
      throw null;
   }

   public static void run(Color backgroundColor, Consumer<ApplicationContext> applicationCode) {
      Objects.requireNonNull(applicationCode);
      if (EventQueue.isDispatchThread()) {
         throw new IllegalStateException("This code should be executed by the main thread");
      } else {
         final Frame frame = new Frame();
         frame.setUndecorated(true);
         frame.setBackground(backgroundColor);
         final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue(1024);
         frame.addKeyListener(new KeyAdapter() {
            private void generateKeyEvent(Event.Action action, KeyEvent e) {
               eventQueue.offer(new Event(action, e.getModifiersEx(), KeyboardKey.key(e.getKeyCode())));
            }

            public void keyPressed(KeyEvent e) {
               this.generateKeyEvent(Event.Action.KEY_PRESSED, e);
            }

            public void keyReleased(KeyEvent e) {
               this.generateKeyEvent(Event.Action.KEY_RELEASED, e);
            }
         });
         frame.setExtendedState(6);
         frame.setVisible(true);
         final Thread applicationThread = Thread.currentThread();
         ApplicationContext context = new ApplicationContext() {
            private volatile Image image;

            void checkThread() {
               if (Thread.currentThread() != applicationThread) {
                  throw new IllegalStateException("Only the application thread can interact with the application context");
               }
            }

            public void exit(int exitStatus) {
               this.checkThread();
               frame.dispose();
               System.exit(exitStatus);
            }

            public ScreenInfo getScreenInfo() {
               this.checkThread();
               return new ScreenInfo() {
                  public float getWidth() {
                     checkThread();
                     return (float)frame.getWidth();
                  }

                  public float getHeight() {
                     checkThread();
                     return (float)frame.getHeight();
                  }
               };
            }

            public Event pollEvent() {
               this.checkThread();
               return (Event)eventQueue.poll();
            }

            public Event pollOrWaitEvent(long timeout) {
               this.checkThread();

               try {
                  return (Event)eventQueue.poll(timeout, TimeUnit.MILLISECONDS);
               } catch (InterruptedException var4) {
                  Thread.currentThread().interrupt();
                  return null;
               }
            }

            public void renderFrame(Consumer<Graphics2D> consumer) {
               Objects.requireNonNull(consumer);
               this.checkThread();
               this.renderOneFrame(consumer);
            }

            private void renderOneFrame(Consumer<Graphics2D> consumer) {
               if (this.image == null) {
                  this.image = frame.createImage(frame.getWidth(), frame.getHeight());
                  this.image.setAccelerationPriority(1.0F);
               }

               Graphics2D bufferGraphics = (Graphics2D)this.image.getGraphics();

               try {
                  consumer.accept(bufferGraphics);
               } finally {
                  bufferGraphics.dispose();
               }

               EventQueue.invokeLater(() -> {
                  Graphics frameGraphics = frame.getGraphics();

                  try {
                     frameGraphics.drawImage(this.image, 0, 0, (ImageObserver)null);
                  } finally {
                     frameGraphics.dispose();
                  }

                  Toolkit.getDefaultToolkit().sync();
               });
            }
         };

         class Waiter implements Runnable {
            private boolean opened;
            private final Object lock = new Object();

            public void run() {
               if (frame.getWidth() <= 1) {
                  EventQueue.invokeLater(this);
               } else {
                  frame.setResizable(false);
                  synchronized(this.lock) {
                     this.opened = true;
                     this.lock.notify();
                  }
               }
            }

            void await() throws InterruptedException {
               synchronized(this.lock) {
                  while(!this.opened) {
                     this.lock.wait();
                  }

               }
            }
         }

         Waiter waiter = new Waiter();
         EventQueue.invokeLater(waiter);

         try {
            waiter.await();
         } catch (InterruptedException var9) {
            Thread.currentThread().interrupt();
            return;
         }

         applicationCode.accept(context);
      }
   }
}

