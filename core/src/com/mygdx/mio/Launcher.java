package com.mygdx.mio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Launcher extends Game {
	//Recognised Screens
	public MainMenu menu;
	public GameScreen gameScreen;
	public CreditScreen creditScreen;
	public SettingsScreen settingscreen;
	public gameover Gameover;
	//Recognised Music
	public static Music music;
	//Creates Random numbers
	public static Random random = new Random();
	@Override
	public void create() {
		//Initalised Screens
		menu = new MainMenu(this);
		gameScreen = new GameScreen(this);
		creditScreen = new CreditScreen(this);
		settingscreen = new SettingsScreen(this);
		Gameover = new gameover(this);
		//Displays what screen to show when launched
		setScreen(new MainMenu(this));
		//Gather the file for the background - https://www.free-stock-music.com/ron-gelinas-chillout-lounge-fulfilled.html
		music = Gdx.audio.newMusic(Gdx.files.internal("background.wav"));
		//Music Controls
		music.setVolume(0f);
		music.setLooping(true);
		music.play();
	}
	@Override
	public void resize(int width, int height) {
		//resises to fit any screen
		menu.resize(width, height);
		gameScreen.resize(width,height);
		Gameover.resize(width,height);
		settingscreen.resize(width,height);
		creditScreen.resize(width,height);
	}
	@Override
	public void render() {
		super.render();
	}
	@Override
	public void dispose() {
		super.dispose();
		music.dispose();
		menu.dispose();
	}
}
