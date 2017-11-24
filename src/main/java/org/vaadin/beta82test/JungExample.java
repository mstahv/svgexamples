package org.vaadin.beta82test;

import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Embedded;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Stroke;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vaadin.ui.Label;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationImageServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import java.io.ByteArrayInputStream;

@SuppressWarnings("serial")
public class JungExample extends ExamplePanel {

    public JungExample() {

        setCaption("Graph using JUNG");

        addComponent(new Label(
                "This graphic is created with JUNG and made to SVG with Batik."));
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

            VisualizationImageServer<Integer, String> server = new SimpleGraphView2()
                    .getServer();
            server.printAll(graphic2d);

            // svgweb (IE fallback) needs size somehow defined
            Element el = graphic2d.getRoot();
            el.setAttributeNS(null, "viewBox", "0 0 350 350");
            el.setAttributeNS(null, "style", "width:100%;height:100%;");

            // Finally, stream out SVG to the standard output using
            // UTF-8 encoding.
            boolean useCSS = true; // we want to use CSS style attributes
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            Writer out = new OutputStreamWriter(bout, "UTF-8");
            graphic2d.stream(el, out, useCSS, false);

            Embedded svgComponent = new Embedded();
            svgComponent.setWidth(350, Unit.PIXELS);
            svgComponent.setHeight(350, Unit.PIXELS);
            Resource svg = new StreamResource(() -> new ByteArrayInputStream(bout.toByteArray()), "graphics.svg");
            svgComponent.setSource(svg);
            addComponent(svgComponent);

        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }

    }

    /**
     * Adapted example from Jung tutorial
     */
    class SimpleGraphView2 extends SparseMultigraph<Integer, String> {

        /**
         * Creates a new instance of SimpleGraphView
         */
        public SimpleGraphView2() {
            addVertex(1);
            addVertex(2);
            addVertex(3);
            addEdge("Edge-A", 1, 3);
            addEdge("Edge-B", 2, 3, EdgeType.DIRECTED);
            addEdge("Edge-C", 2, 1, EdgeType.DIRECTED);

        }

        /**
         * @param args the command line arguments
         * @return
         */
        public VisualizationImageServer<Integer, String> getServer() {
            // Layout<V, E>, VisualizationComponent<V,E>
            Layout<Integer, String> layout = new CircleLayout<>(
                    this);
            layout.setSize(new Dimension(300, 300));
            VisualizationImageServer<Integer, String> vv = new VisualizationImageServer<>(
                    layout, new Dimension(350, 350));
            // Setup up a new vertex to paint transformer...
            // Set up a new stroke Transformer for the edges
            float dash[] = {10.0f};
            final Stroke edgeStroke = new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash,
                    0.0f);
            vv.getRenderContext().setVertexFillPaintTransformer(i -> Color.GREEN);
            vv.getRenderContext().setEdgeStrokeTransformer(
                    s -> edgeStroke);
            vv.getRenderContext().setVertexLabelTransformer(
                    new ToStringLabeller());
            vv.getRenderContext().setEdgeLabelTransformer(
                    new ToStringLabeller());
            vv.getRenderer().getVertexLabelRenderer()
                    .setPosition(Position.CNTR);

            return vv;
        }
    }
}
