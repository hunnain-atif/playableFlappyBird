package com.hunnainatif.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBirdClone extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	TextureAtlas birdAtlas;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;
	Animation animation;
	Circle birdCircle;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;
	BitmapFont font;
	float timePassed = 0;
	float birdPosition = 0;
	float birdVelocity = 0;
	float tubeVelocity = 4;
	float gap = 400;
	float distanceBetweenTubes;
	int gameState = 0;
	int numOfTubes = 4;
	int score = 0;
	int scoringTube = 0;
	float[] tubePosition = new float[numOfTubes];
	float[] tubeOffset = new float[numOfTubes];


	Random random;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		birdCircle = new Circle();
		topTubeRectangles = new Rectangle[numOfTubes];
		bottomTubeRectangles = new Rectangle[numOfTubes];
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(7);

		background = new Texture("bg.png");
		birdAtlas = new TextureAtlas(Gdx.files.internal("bird.atlas"));
		animation = new Animation(1/5f, birdAtlas.getRegions());
		birdPosition = Gdx.graphics.getHeight() / 2 - 48;
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		gameOver = new Texture("flappyBirdGameOver.png");
		random = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

		startGame();



	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gameState == 1) {
			if(tubePosition[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;
				Gdx.app.log("score", String.valueOf(score));
				if(scoringTube < numOfTubes - 1) {
					scoringTube ++;
				} else {
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()) {
				birdVelocity = -15;

			}

			for(int i = 0; i < numOfTubes; i++) {
				if (tubePosition[i] < -topTube.getWidth()) {
					tubePosition[i] += numOfTubes*distanceBetweenTubes;
					tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 680);
				} else {
					tubePosition[i] -= tubeVelocity;
				}

				batch.draw(topTube, tubePosition[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubePosition[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubePosition[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubePosition[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
			}

			if(birdPosition  > 0) {
				birdVelocity ++;
				birdPosition -= birdVelocity;
			} else {
				gameState = -1;
			}


		} else if (gameState == 0) {
			if(Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else {
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gameOver.getHeight() /2);
			if(Gdx.input.justTouched()) {
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				birdVelocity = 0;
			}
		}
		timePassed += Gdx.graphics.getDeltaTime();
		batch.draw((TextureRegion) animation.getKeyFrame(timePassed, true), Gdx.graphics.getWidth() / 2 - 68,  birdPosition);
		font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() * 7/8);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdPosition + 48, 68);

		for(int i = 0; i < numOfTubes; i++) {
			if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
				gameState = -1;

			}

		}


	}

	public void startGame() {
		birdPosition = Gdx.graphics.getHeight() / 2 - 48;

		for(int i = 0; i < numOfTubes; i++) {
			tubeOffset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 680);
			tubePosition[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		birdAtlas.dispose();
		topTube.dispose();
		bottomTube.dispose();
	}
}
