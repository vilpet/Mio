package com.mygdx.mio.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.mio.Launcher;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height=640;
		config.width=360;
		new LwjglApplication(new Launcher(), config);
		config.addIcon("Miosmall.png", Files.FileType.Internal);
	}
}
