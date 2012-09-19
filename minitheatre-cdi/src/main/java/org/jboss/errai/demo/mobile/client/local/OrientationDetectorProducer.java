package org.jboss.errai.demo.mobile.client.local;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.errai.demo.mobile.client.shared.Ongoing;
import org.jboss.errai.demo.mobile.client.shared.OrientationEvent;

@Singleton
public class OrientationDetectorProducer {

  @Inject @Ongoing Event<OrientationEvent> orientationEventSource;

  @Produces
  public OrientationDetector get() {
    OrientationDetector detector;
    if (Html5MotionDetector.isSupported()) {
      detector = new Html5MotionDetector();
    }
    else if (Html5OrientationDetector.isSupported()) {
      detector = new Html5OrientationDetector();
    }
    else {
      detector = new NoMotionDetector();
    }
    
    detector.setOrientationEventSource(orientationEventSource);
    return detector;
  }
}
