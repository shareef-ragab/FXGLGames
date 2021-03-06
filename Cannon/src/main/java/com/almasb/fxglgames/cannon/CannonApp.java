/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.fxglgames.cannon;

import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * A basic FXGL game demo.
 *
 * Game:
 * The player shoots from a "cannon" and tries to
 * get the projectile in-between the barriers.
 *
 * @author Almas Baimagambetov (AlmasB) (almaslvl@gmail.com)
 */
public class CannonApp extends GameApplication {

    private Entity cannon;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Cannon");
        settings.setVersion("0.2.1");
    }

    @Override
    protected void initInput() {
        onBtnDown(MouseButton.PRIMARY, "Shoot", () -> shoot());
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("score", 0);
    }

    @Override
    protected void initGame() {
        getGameWorld().addEntityFactory(new CannonFactory());

        initScreenBounds();
        initCannon();
        initBasket();
    }

    private void initScreenBounds() {
        //getGameWorld().addEntity(Entities.makeScreenBounds(100));
    }

    private void initCannon() {
        cannon = getGameWorld().spawn("cannon", 50, getAppHeight() - 30);
    }

    private void initBasket() {
        spawn("basketBarrier", 400, getAppHeight() - 300);
        spawn("basketBarrier", 700, getAppHeight() - 300);
        spawn("basketGround", 500, getAppHeight());
    }

    private void shoot() {
        spawn("bullet", cannon.getPosition().add(70, 0));
    }

    @Override
    protected void initPhysics() {
        onCollisionBegin(CannonType.BULLET, CannonType.BASKET, (bullet, basket) -> {
            bullet.removeFromWorld();
            inc("score", +1000);

            playBasketAnimation();
        });
    }

    @Override
    protected void initUI() {
        Text scoreText = getUIFactory().newText("", Color.BLACK, 24);
        scoreText.setTranslateX(550);
        scoreText.setTranslateY(100);
        scoreText.textProperty().bind(getGameState().intProperty("score").asString("Score: [%d]"));

        getGameScene().addUINode(scoreText);
    }

    private void playBasketAnimation() {
        animationBuilder()
                .duration(Duration.seconds(0.2))
                .interpolator(Interpolators.EXPONENTIAL.EASE_IN())
                .scale(getGameWorld().getEntitiesByType(CannonType.BASKET))
                .from(new Point2D(1.2, 1))
                .to(new Point2D(1, 1))
                .buildAndPlay();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
