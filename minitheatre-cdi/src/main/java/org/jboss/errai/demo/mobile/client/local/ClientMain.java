/*
 * Copyright 2012 JBoss, a division of Red Hat Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.errai.demo.mobile.client.local;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.jboss.errai.demo.mobile.client.shared.Orientation;
import org.jboss.errai.ioc.client.api.EntryPoint;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Main application entry point.
 */
@EntryPoint
public class ClientMain {

  @Inject 
  private OrientationDetector orientationDetector;
  
  private WelcomeDialog welcomeDialog;
  private final Map<String, PerspectiveAnimator> animators = new HashMap<String, PerspectiveAnimator>();

  private final AnimationScheduler animScheduler = AnimationScheduler.get();

  @PostConstruct
  public void init() {
    welcomeDialog = new WelcomeDialog(new Runnable() {
      @Override
      public void run() {
        orientationDetector.setClientId(welcomeDialog.getNameBoxContents());
        RootPanel.get("rootPanel").remove(welcomeDialog);

        // TODO: could block startup using InitBallot/voteForInit()
        GWT.log("Starting to poll for readiness! Orientation detector: " + orientationDetector);
        // poll for readiness; when it's ready, start watching device orientation.
        Timer t = new Timer() {
          @Override
          public void run() {
            GWT.log("Orientation detector: " + orientationDetector);
            if (orientationDetector.isReady()) {
              orientationDetector.startFiringOrientationEvents();
            } else {
              schedule(100);
            }
          }
        };
        t.schedule(100);
      }
    });
    RootPanel.get("rootPanel").add(welcomeDialog);
    welcomeDialog.nameBox.setFocus(true);

    animScheduler.requestAnimationFrame(new AnimationCallback() {
      @Override
      public void execute(double timestamp) {
        for (PerspectiveAnimator animator : animators.values()) {
          animator.nextFrame();
        }
        animScheduler.requestAnimationFrame(this);
      }
    });
  }

  public void visualizeOrientationEvent(@Observes Orientation e) {
    GWT.log("Received: " + e);
    Element rotateMe = Document.get().getElementById("rotateMe-" + e.getClientId());
    if (rotateMe == null) {
      // must be a new client! We will clone the template for this new client.
      Element template = Document.get().getElementById("rotateMeTemplate");
      rotateMe = (Element) template.cloneNode(true);
      rotateMe.setId("rotateMe-" + e.getClientId());
      rotateMe.getFirstChildElement().setInnerText(e.getClientId());
      template.getParentElement().appendChild(rotateMe);
    }

    PerspectiveAnimator animator = animators.get(e.getClientId());
    if (animator == null) {
      animator = new PerspectiveAnimator(rotateMe);
      animators.put(e.getClientId(), animator);
    }

    animator.updateTargets(e);
  }
}
