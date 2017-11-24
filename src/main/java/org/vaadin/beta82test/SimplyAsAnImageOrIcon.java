package org.vaadin.beta82test;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.label.MLabel;

public class SimplyAsAnImageOrIcon extends ExamplePanel {

    public SimplyAsAnImageOrIcon() {
        setCaption("Image and icon");
        addComponent(new MLabel("Following Image component (rendered as IMG element) contains SVG image. Note, that by using SVG in this way, it is treated as a static image. For eample the js changing the color on click is not executed. See the file example to see how to render an interactive SVG.").withFullWidth());
        Image image = new Image(null, new ClassResource("/pull.svg"));
        image.setWidth("300px");
        addComponent(image);
        
        addComponent(new MLabel("Following Button has SVG logo as an icon.").withFullWidth());
        
        Button button = new Button();
        button.setIcon(new ClassResource("/vaadin-logo.svg"));
        button.addStyleNames(ValoTheme.BUTTON_ICON_ONLY, ValoTheme.BUTTON_HUGE);
        addComponent(button);
    }

}
