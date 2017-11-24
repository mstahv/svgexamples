package org.vaadin.beta82test;

import com.vaadin.server.ClassResource;
import com.vaadin.ui.Embedded;
import org.vaadin.viritin.label.MLabel;

public class AnimationExample extends ExamplePanel {
	public AnimationExample() {
		setCaption("Animation");
		addComponent(new MLabel(
				"A simple example from an svg file. Also demonstrates SVG animations.").withFullWidth());
		Embedded svg = new Embedded();
		svg.setWidth("800px");
		svg.setHeight("400px");
		svg.setSource(new ClassResource("/svg2009.svg"));
		addComponent(svg);
	}

}
