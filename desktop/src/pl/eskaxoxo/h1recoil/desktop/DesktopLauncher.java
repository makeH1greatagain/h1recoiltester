package pl.eskaxoxo.h1recoil.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import pl.eskaxoxo.h1recoil.H1Recoil;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.width = 800;
		config.height = 450;
		
		new LwjglApplication(new H1Recoil(), config);
	}
}
