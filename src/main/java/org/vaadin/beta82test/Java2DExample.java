package org.vaadin.beta82test;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vaadin.ui.Label;
import java.io.ByteArrayInputStream;

@SuppressWarnings("serial")
public class Java2DExample extends ExamplePanel {
    
    private int height = 300;
    private int widht = 400;
    
    public Java2DExample() {
        setCaption("SVG using java.awt.Graphics2D");
        
        addComponent(new Label(
                "This graphic is created with java.awt.Graphics2D API and made to SVG with Batik."));
        
        try {
            
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder docBuilder = null;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.newDocument();
            Element svgelem = document.createElement("svg");
            document.appendChild(svgelem);

            // Create an instance of the SVG Generator
            SVGGraphics2D graphic2d = new SVGGraphics2D(document);
            drawDemo(widht, height, graphic2d);

            // svgweb (IE fallback) needs size somehow defined
            Element el = graphic2d.getRoot();
            el.setAttributeNS(null, "viewBox", "0 0 " + widht
                    + " " + height + "");
            el.setAttributeNS(null, "style",
                    "width:100%;height:100%;");

            // Finally, stream out SVG to the standard output using
            // UTF-8 encoding.
            boolean useCSS = true; // we want to use CSS style attributes
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Writer out = new OutputStreamWriter(bout, "UTF-8");
            graphic2d.stream(el, out, useCSS, false);
            
            Embedded svgComponent = new Embedded();
            svgComponent.setWidth(widht, Unit.PIXELS);
            svgComponent.setHeight(height, Unit.PIXELS);
            
            Resource svg = new StreamResource(() -> new ByteArrayInputStream(bout.toByteArray()), "graphics.svg");
            svgComponent.setSource(svg);
            addComponent(svgComponent);
            
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
        
    }
    
    private static Color colors[] = {Color.blue, Color.green, Color.red};

    /**
     * Example code borrowed from Sun demos:
     * http://java.sun.com/products/java-media
     * /2D/samples/suite/Arcs_Curves/Curves.java
     *
     */
    public void drawDemo(int w, int h, Graphics2D g2) {
        
        g2.setColor(Color.black);

        // draws the word "QuadCurve2D"
        FontRenderContext frc = g2.getFontRenderContext();
        TextLayout tl = new TextLayout("QuadCurve2D", g2.getFont(), frc);
        float xx = (float) (w * .5 - tl.getBounds().getWidth() / 2);
        tl.draw(g2, xx, tl.getAscent());

        // draws the word "CubicCurve2D"
        tl = new TextLayout("CubicCurve2D", g2.getFont(), frc);
        xx = (float) (w * .5 - tl.getBounds().getWidth() / 2);
        tl.draw(g2, xx, h * .5f);
        g2.setStroke(new BasicStroke(5.0f));
        
        float yy = 20;

        // draws 3 quad curves and 3 cubic curves.
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                Shape shape = null;
                
                if (i == 0) {
                    shape = new QuadCurve2D.Float(w * .1f, yy, w * .5f, 50,
                            w * .9f, yy);
                } else {
                    shape = new CubicCurve2D.Float(w * .1f, yy, w * .4f,
                            yy - 15, w * .6f, yy + 15, w * .9f, yy);
                }
                g2.setColor(colors[j]);
                if (j != 2) {
                    g2.draw(shape);
                }
                
                if (j == 1) {
                    g2.setColor(Color.lightGray);

                    /*
					 * creates an iterator object to iterate the boundary of the
					 * curve.
                     */
                    PathIterator f = shape.getPathIterator(null);

                    /*
					 * while iteration of the curve is still in process fills
					 * rectangles at the endpoints and control points of the
					 * curve.
                     */
                    while (!f.isDone()) {
                        float[] pts = new float[6];
                        switch (f.currentSegment(pts)) {
                            case PathIterator.SEG_MOVETO:
                            case PathIterator.SEG_LINETO:
                                g2.fill(new Rectangle2D.Float(pts[0], pts[1], 5, 5));
                                break;
                            case PathIterator.SEG_CUBICTO:
                            case PathIterator.SEG_QUADTO:
                                g2.fill(new Rectangle2D.Float(pts[0], pts[1], 5, 5));
                                if (pts[2] != 0) {
                                    g2.fill(new Rectangle2D.Float(pts[2], pts[3],
                                            5, 5));
                                }
                                if (pts[4] != 0) {
                                    g2.fill(new Rectangle2D.Float(pts[4], pts[5],
                                            5, 5));
                                }
                        }
                        f.next();
                    }
                    
                } else if (j == 2) {
                    // draws red ellipses along the flattened curve.
                    PathIterator p = shape.getPathIterator(null);
                    FlatteningPathIterator f = new FlatteningPathIterator(p,
                            0.1);
                    while (!f.isDone()) {
                        float[] pts = new float[6];
                        switch (f.currentSegment(pts)) {
                            case PathIterator.SEG_MOVETO:
                            case PathIterator.SEG_LINETO:
                                g2.fill(new Ellipse2D.Float(pts[0], pts[1], 3, 3));
                        }
                        f.next();
                    }
                }
                yy += h / 6;
            }
            yy = h / 2 + 15;
        }
    }
    
}
