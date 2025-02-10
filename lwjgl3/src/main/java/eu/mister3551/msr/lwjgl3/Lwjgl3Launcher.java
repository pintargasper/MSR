package eu.mister3551.msr.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import eu.mister3551.msr.Main;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) {
            return;
        }
        createApplication();
    }

    private static void createApplication() {
        new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("MSR");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);

        configuration.setWindowedMode(1500, 600);
        //configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
        //configuration.setDecorated(false);

        configuration.setWindowIcon("logo/logo128.png", "logo/logo64.png", "logo/logo32.png", "logo/logo16.png");
        return configuration;
    }
}
