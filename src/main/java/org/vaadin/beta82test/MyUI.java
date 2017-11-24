package org.vaadin.beta82test;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.navigator.View;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.annotation.WebServlet;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of an HTML page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("valo")
@PushStateNavigation
public class MyUI extends UI {

    TabSheet tabs = new TabSheet();

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setNavigator(new Navigator(this, (View view) -> {
            tabs.setSelectedTab((Component) view);
        }));

        registerExample(SvgInVaadin.class);
        registerExample(SimplyAsAnImageOrIcon.class);
        registerExample(FileExample.class);
        registerExample(AnimationExample.class);
        registerExample(Java2DExample.class);
        registerExample(JungExample.class);

        getNavigator().setErrorView(SvgInVaadin.class);
        tabs.addSelectedTabChangeListener(e -> {
            if (e.isUserOriginated()) {
                getNavigator().navigateTo(e.getTabSheet().getSelectedTab().getClass().getSimpleName());
            }
        });
        String state = getNavigator().getState();
        getNavigator().navigateTo(state);

        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);
        setContent(tabs);
    }

    private void registerExample(Class<? extends ExamplePanel> aClass) {
        try {
            ExamplePanel newInstance = aClass.newInstance();
            tabs.addComponent(newInstance);
            getNavigator().addView(aClass.getSimpleName(), newInstance);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(MyUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
