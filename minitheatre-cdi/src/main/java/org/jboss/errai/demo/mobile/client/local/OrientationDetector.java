package org.jboss.errai.demo.mobile.client.local;

import javax.enterprise.event.Event;

import org.jboss.errai.demo.mobile.client.shared.Orientation;

public abstract class OrientationDetector {

  /**
   * Don't try to fire a CDI OrientationEvent event more than once every 250ms.
   * <p>
   * TODO we should remove this once the bus supports coalescing events!
   */
  private long minEventInterval = 175;

  /**
   * The time we last fired an OrientationEvent.
   * <p>
   * TODO we should remove this once the bus supports coalescing events!
   */
  private long lastEventFireTime;

  protected Event<Orientation> orientationEventSource;

  /**
   * Should be set by the main application when the username is set or updated.
   */
  private String clientId = "Anonymous";

  /**
   * Stops the periodic firing of CDI OrientationEvents. If this detector was
   * already in the stopped state, calling this method has no effect.
   */
  public abstract void stopFiringOrientationEvents();

  /**
   * Starts the periodic firing of CDI OrientationEvents. If this detector was
   * already in the started state, calling this method has no effect.
   */
  public abstract void startFiringOrientationEvents();

  /**
   * Fires an {@link Orientation} with the given parameters. This method is
   * meant to be called by the browser-specific logic that detects the device
   * orientation.
   */
  protected void fireOrientationEvent(double x, double y, double z) {
    long now = System.currentTimeMillis();
    if (now - lastEventFireTime < minEventInterval) {
      return;
    }
    lastEventFireTime = now;
    orientationEventSource.fire(new Orientation(clientId, x, y, z));
  }

  /**
   * The provider class that creates the detector calls this method to give us a
   * means of firing the event.
   */
  void setOrientationEventSource(Event<Orientation> orientationEventSource) {
    this.orientationEventSource = orientationEventSource;
  }

  /**
   * Sets the client ID (user name) that should be included with all orientation
   * events fired.
   *
   * @param clientId The client ID to use. Not null.
   */
  public void setClientId(String clientId) {
    if (clientId == null) throw new NullPointerException();
    this.clientId = clientId;
  }

  /**
   * Returns true if this detector can be started. Some detectors (for example,
   * PhoneGap) need to wait for subsystems to start before they can begin firing
   * orientation events.
   * <p>
   * TODO: we won't need this when client code can participate in Errai VoteForInit.
   */
  public abstract boolean isReady();
}