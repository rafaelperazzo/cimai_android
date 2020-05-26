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

import java.util.Random;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
@LargeTest

public class PesquisaTest {
    private IdlingResource mIdlingResource;
    Random gerador = new Random();

    public void repetir(int id) {
        for (int i = 0; i<8; i++) {
            onView(withId(id)).perform(click());
            onData(anything()).atPosition(i).perform(click());
        }
    }

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
        repetir(R.id.spinAno);
    }

    @Test
    public void testAbrirPesquisaProducoesBotoes() {
        onView(withText("PRODUÇÃO BIBLIOGRÁFICA")).perform(click());
        for (int i = 0; i<8; i++) {
            onView(withId(R.id.spinAno)).perform(click());
            onData(anything()).atPosition(i).perform(click());
            onView(withId(R.id.btnPorProducao)).perform(click());
            onView(withId(R.id.porProducoesChart)).check(matches(isDisplayed()));
            pressBack();
            onView(withId(R.id.btnGrandeArea)).perform(click());
            onView(withId(R.id.porProducoesChart)).check(matches(isDisplayed()));
            pressBack();
            onView(withId(R.id.btnBolsistas)).perform(click());
            onView(withId(R.id.porProducoesChart)).check(matches(isDisplayed()));
            pressBack();
            onView(withId(R.id.btnArea)).perform(click());
        }
    }

    @Test
    public void testPesquisaProducoesResumo() {
        onView(withText("PRODUÇÃO BIBLIOGRÁFICA")).perform(click());
        onView(withId(R.id.spinAno)).perform(click());
        onData(anything()).atPosition(0).perform(click());
        for (int i = 0; i<8; i++) {
            onView(withId(R.id.spinAno)).perform(click());
            onData(anything()).atPosition(i).perform(click());
            onView(withId(R.id.txtPeriodicos)).check(matches((not(withText("0")))));
            onView(withId(R.id.txtLivros)).check(matches((not(withText("0")))));
            onView(withId(R.id.txtAnais)).check(matches((not(withText("0")))));
            onView(withId(R.id.txtCapitulos)).check(matches((not(withText("0")))));
        }

    }

    @Test
    public void testAbrirPesquisaGrupos(){
        onView(withText("GRUPOS DE PESQUISA")).perform(click());
        onView(withId(R.id.btnGrupoListaProjetos)).perform(click());
        onView(withId(R.id.btnGrupoArea)).perform(click());
        onView(withId(R.id.txtGrupoCertificados)).check(matches((not(withText("0")))));
        onView(withId(R.id.txtGrupoDocentes)).check(matches((not(withText("0")))));
        onView(withId(R.id.txtGrupoTecnicos)).check(matches((not(withText("0")))));
        onView(withId(R.id.txtGrupoDiscentes)).check(matches((not(withText("0")))));
    }

    @Test
    public void testAbrirPesquisaProjetos() {
        onView(withText("PROJETOS DE PESQUISA")).perform(click());
        for (int i = 0; i<8; i++) {
            onView(withId(R.id.spinProjetoAno)).perform(click());
            onData(anything()).atPosition(i).perform(click());
            onView(withId(R.id.txtProjetos)).check(matches((not(withText("0")))));
            onView(withId(R.id.txtProjetoCoordenadores)).check(matches((not(withText("0")))));
            onView(withId(R.id.txtProjetoDiscentes)).check(matches((not(withText("0")))));
        }

    }

    @Test
    public void testAbrirPesquisaProjetosBotoes() {
        onView(withText("PROJETOS DE PESQUISA")).perform(click());
        for (int i = 0; i<8; i++) {
            onView(withId(R.id.spinProjetoAno)).perform(click());
            onData(anything()).atPosition(i).perform(click());
            onView(withId(R.id.btnProjetoLista)).perform(click());
            onView(withId(R.id.searchProjetos)).perform(typeText("materi"));
            onView(withId(R.id.searchProjetos)).perform(closeSoftKeyboard());
            onView(withId(R.id.btnProjetoGrandeArea)).perform(click());
            onView(withId(R.id.porProducoesChart)).check(matches(isDisplayed()));
            pressBack();
            onView(withId(R.id.btnProjetoPorAno)).perform(click());
            onView(withId(R.id.porProducoesChart)).check(matches(isDisplayed()));
            pressBack();
            onView(withId(R.id.btnProjetoArea)).perform(click());
            onView(withId(R.id.txtPeriodicos)).perform(click());
            pressBack();
        }
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
