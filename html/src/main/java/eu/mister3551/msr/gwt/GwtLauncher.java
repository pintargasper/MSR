package eu.mister3551.msr.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.preloader.Preloader;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.Panel;
import eu.mister3551.msr.Main;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration gwtApplicationConfiguration = new GwtApplicationConfiguration(true);
        gwtApplicationConfiguration.padVertical = 0;
        gwtApplicationConfiguration.padHorizontal = 0;
        return gwtApplicationConfiguration;
    }

    @Override
    public ApplicationListener createApplicationListener () {
        return new Main();
    }

    @Override
    public Preloader.PreloaderCallback getPreloaderCallback() {
        return createPreloaderPanel(GWT.getHostPageBaseURL() + "files/img/logo/preload.png");
    }

    @Override
    protected void adjustMeterPanel(Panel meterPanel, Style meterStyle) {
        meterPanel.setStyleName("gdx-meter");
        meterPanel.setHeight("25");
        meterStyle.setProperty("background", "linear-gradient(to right, #ce1212, #ec2727)");
    }
}
