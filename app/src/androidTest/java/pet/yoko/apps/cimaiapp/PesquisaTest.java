package pet.yoko.apps.cimaiapp;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest

public class PesquisaTest {
    private IdlingResource mIdlingResource;

    @Rule
    public ActivityTestRule<PesquisaActivity> activityRule
            = new ActivityTestRule<>(PesquisaActivity.class);


    @Before
    public void registerIdlingResource() {
        ActivityScenario activityScenario = ActivityScenario.launch(PesquisaActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<PesquisaActivity>() {
            @Override
            public void perform(PesquisaActivity activity) {
                mIdlingResource = activity.getIdlingResource();
                // To prove that the test fails, omit this call:
                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
    }

    @Test
    public void testAbrirPesquisaProducoes() {
        onView(withText("PRODUÇÃO BIBLIOGRÁFICA")).perform(click());
        onView(withId(R.id.btnArea)).perform(click());
        onView(withId(R.id.spinAno)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.spinAno)).check(matches(withSpinnerText(containsString("2014"))));
        onView(withId(R.id.spinAno)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.spinAno)).perform(click());
        onData(anything()).atPosition(3).perform(click());

    }

    @Test
    public void testAbrirPesquisaProducoesPorProducao() {
        onView(withText("PRODUÇÃO BIBLIOGRÁFICA")).perform(click());
        onView(withId(R.id.btnPorProducao)).perform(click());
        pressBack();
    }

    @Test
    public void testPesquisaProducoesResumo() {
        onView(withText("PRODUÇÃO BIBLIOGRÁFICA")).perform(click());
        onView(withId(R.id.spinAno)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.txtPeriodicos)).check(matches((not(withText("0")))));
        onView(withId(R.id.txtLivros)).check(matches((not(withText("0")))));
        onView(withId(R.id.txtAnais)).check(matches((not(withText("0")))));
        onView(withId(R.id.txtCapitulos)).check(matches((not(withText("0")))));
    }

    @Test
    public void testAbrirPesquisaGrupos() {
        onView(withText("GRUPOS DE PESQUISA")).perform(click());
    }

    @Test
    public void testAbrirPesquisaProjetos() {
        onView(withText("PROJETOS DE PESQUISA")).perform(click());
        onView(withId(R.id.spinProjetoAno)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.spinProjetoAno)).perform(click());
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.spinProjetoAno)).perform(click());
        onData(anything()).atPosition(3).perform(click());

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
