package org.jboss.errai.demo.mobile.client.local;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jboss.errai.demo.mobile.client.shared.Ongoing;
import org.jboss.errai.demo.mobile.client.shared.OrientationEvent;

import com.google.gwt.core.client.GWT;

@Singleton
public class OrientationDetectorProvider implements Provider<OrientationDetector> {

  @Inject @Ongoing Event<OrientationEvent> orientationEventSource;

  @Produces
  public OrientationDetector get() {
    GWT.log("Creating orientation detector...");
    OrientationDetector detector;
    if (supportsMotionEvents()) {
      detector = new Html5MotionDetector();
    }
    else if (supportsOrientationEvents()) {
      detector = new Html5OrientationDetector();
    }
    else {
      detector = new NoMotionDetector();
    }
    
    GWT.log("Created " + detector);
    detector.setOrientationEventSource(orientationEventSource);
    GWT.log("Added event source " + orientationEventSource);
    
    return detector;
  }

  private native boolean supportsOrientationEvents() /*-{
    return $wnd.DeviceOrientationEvent !== undefined;
  }-*/;

  private native boolean supportsMotionEvents() /*-{
    return $wnd.DeviceMotionEvent !== undefined;
  }-*/;

}
