package org.vaadin.beta82test;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.JavaScript;

import com.vaadin.ui.Notification;
import elemental.json.JsonArray;
import org.vaadin.viritin.label.MLabel;

public class FileExample extends ExamplePanel {

    public FileExample() {
        setCaption("Interactive SVG");
        addComponent(new MLabel(
                "A simple example from an svg file using Embedded component. Unlike with Image component, the SVGs JS etc are active. The example also demonstrates how to provide a trivial server side integration API for the SVG."));
        Embedded svg = new Embedded();
        svg.setWidth("400px");
        svg.setHeight("400px");
        svg.setSource(new ClassResource("/pull.svg"));

        // Expose a JS hook that pull.svg file calls when clicked
        JavaScript.getCurrent().addFunction("callMyVaadinFunction", (JsonArray arguments) -> {
            Notification.show("Message from SVG:" + arguments.getString(0));
        });

        addComponent(svg);
    }

}
