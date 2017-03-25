package com.karumi.screenshot;

import android.app.Activity;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.karumi.screenshot.di.MainComponent;
import com.karumi.screenshot.di.MainModule;
import com.karumi.screenshot.model.SuperHero;
import com.karumi.screenshot.model.SuperHeroesRepository;
import com.karumi.screenshot.ui.view.MainActivity;
import com.karumi.screenshot.ui.view.SuperHeroDetailActivity;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import it.cosenonjaviste.daggermock.DaggerMockRule;

import static org.mockito.Mockito.when;

public class SuperHeroDetailActivityTest extends ScreenshotTest {
    @Rule
    public DaggerMockRule<MainComponent> daggerRule =
            new DaggerMockRule<>(MainComponent.class, new MainModule()).set(
                    new DaggerMockRule.ComponentSetter<MainComponent>() {
                        @Override public void setComponent(MainComponent component) {
                            SuperHeroesApplication app =
                                    (SuperHeroesApplication) InstrumentationRegistry.getInstrumentation()
                                            .getTargetContext()
                                            .getApplicationContext();
                            app.setComponent(component);
                        }
                    });

    @Rule public IntentsTestRule<SuperHeroDetailActivity> activityRule =
            new IntentsTestRule<>(SuperHeroDetailActivity.class, true, false);

    @Mock
    SuperHeroesRepository repository;

    @Test
    public void showsAnySuperHeroe() {
        SuperHero superhero = givenAnySuperHeroe("Calico Electronico",
                "El superheroe mas español de todos los tiempos", false);

        Activity activity = startActivity(superhero);

        compareScreenshot(activity);
    }

    @Test
    public void showsAnySuperHeroeWithoutName() {
        SuperHero superhero = givenAnySuperHeroe(null,
                "El superheroe mas español de todos los tiempos", false);

        Activity activity = startActivity(superhero);

        compareScreenshot(activity);
    }

    @Test
    public void showsAnySuperHeroeWithoutDescription() {
        SuperHero superhero = givenAnySuperHeroe("Calico Electronico",
                null, false);

        Activity activity = startActivity(superhero);

        compareScreenshot(activity);
    }

    @Test
    public void showsAnyAvenger() {
        SuperHero superhero = givenAnySuperHeroe("Calico Electronico Avenger Version",
                "El superheroe mas español de todos los tiempos", true);

        Activity activity = startActivity(superhero);

        compareScreenshot(activity);
    }

    private SuperHero givenAnySuperHeroe(String name, String description, boolean avenger) {
        String superHeroName = name;
        String superHeroDescription = description;
        SuperHero superHero = new SuperHero(superHeroName, null, avenger, superHeroDescription);
        when(repository.getByName(superHeroName)).thenReturn(superHero);
        return superHero;
    }


    private SuperHeroDetailActivity startActivity(SuperHero superHero) {
        Intent intent = new Intent();
        intent.putExtra("super_hero_name_key", superHero.getName());
        return activityRule.launchActivity(intent);
    }
}
