package org.vaadin.beta82test;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Image;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.label.RichText;

public class SvgInVaadin extends ExamplePanel {

    public SvgInVaadin() {
        setCaption("About: SVG in Vaadin");
        addComponent(new RichText().withMarkDownResource("/about.md"));
    }

}
