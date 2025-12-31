package hms.view.common;

import hms.view.MainFrame;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;

public class ActionFeedbackNotifier {

  public static void showInformation(Component ownerComponent, String message) {
    showNotification(ownerComponent, normalizeMessage(message));
  }

  public static void showSuccess(Component ownerComponent, String message) {
    showNotification(ownerComponent, normalizeMessage(message));
  }

  public static void showError(Component ownerComponent, String message) {
    showNotification(ownerComponent, normalizeMessage(message));
  }

  private static void showNotification(Component ownerComponent, String message) {
    String safeMessage = normalizeMessage(message);
    if (safeMessage.isEmpty()) {
      return;
    }

    appendLogLine(ownerComponent, safeMessage);

    SwingUtilities.invokeLater(() -> {
      Window ownerWindow = ownerComponent == null ? null : SwingUtilities.getWindowAncestor(ownerComponent);
      JWindow notificationWindow = ownerWindow == null ? new JWindow() : new JWindow(ownerWindow);

      JLabel messageLabel = new JLabel(safeMessage);
      messageLabel.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createLineBorder(new Color(180, 180, 180)),
          BorderFactory.createEmptyBorder(10, 12, 10, 12)
      ));
      messageLabel.setFont(messageLabel.getFont().deriveFont(Font.PLAIN, 13f));
      messageLabel.setOpaque(true);
      messageLabel.setBackground(new Color(250, 250, 250));
      messageLabel.setForeground(new Color(20, 20, 20));

      notificationWindow.getContentPane().add(messageLabel);
      notificationWindow.pack();
      notificationWindow.setAlwaysOnTop(true);

      Point location = calculateBottomRightLocation(ownerWindow, notificationWindow.getSize());
      notificationWindow.setLocation(location);
      notificationWindow.setVisible(true);

      Timer hideTimer = new Timer(2200, event -> {
        notificationWindow.setVisible(false);
        notificationWindow.dispose();
      });
      hideTimer.setRepeats(false);
      hideTimer.start();
    });
  }

  private static void appendLogLine(Component ownerComponent, String message) {
    if (ownerComponent == null) {
      return;
    }

    Window ownerWindow = SwingUtilities.getWindowAncestor(ownerComponent);
    if (ownerWindow instanceof MainFrame) {
      ((MainFrame) ownerWindow).appendLogLine(message);
    }
  }

  private static Point calculateBottomRightLocation(Window ownerWindow, Dimension notificationSize) {
    int padding = 16;

    if (ownerWindow == null) {
      return new Point(padding, padding);
    }

    Insets insets = ownerWindow.getInsets();
    int windowRight = ownerWindow.getX() + ownerWindow.getWidth() - insets.right;
    int windowBottom = ownerWindow.getY() + ownerWindow.getHeight() - insets.bottom;

    int x = windowRight - notificationSize.width - padding;
    int y = windowBottom - notificationSize.height - padding;

    return new Point(Math.max(x, padding), Math.max(y, padding));
  }

  private static String normalizeMessage(String message) {
    if (message == null) {
      return "";
    }
    return message.trim();
  }
}