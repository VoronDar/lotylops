
/*

    Перая запускаемая активность
    Главное меню - содержит фрагменты для плана дня, библиотеки, свободной практики, настроек.

    Иерархия:
    Ruler - определяет что делать дальше и направляет к новой активности
    CardRepeatManager - управляет показом кард (конкретно - repeatDay repeatLevel и practiceLevel)
    TransportSQL - отвечает только за транспортировку данных из баз данных в Ruler и обратно
        TransportSql            -   абстрактный класс родитель
        MainTransportSql        -   фабрика
        TransportSqlInterface   -   интерфейс
        TransportSqlVocabulary  -   конкретный класс для доступа к лексическим картам
        TransportSqlPhraseology -   конкретный класс для доступа к фразеологии

    База данных:

    Contract - обозначения для базы данных
        VocabularyContract
        PhraseologyContract
    DBHelper (_|todayRepeatVocabulary|today(study)Vocabulary) - сами базы данных

    активности:

    MainActivity    - карточка
    CardPhraseology - карточка для фразеологии
    testSentence    - тесты
    testSound
    testTranslate
    testTranslateNative
    testWriting
    mainPlain       - главное меню

    фрагменты этой активности содержат постфикс    Fragment
    DayPlanFragment         - фрагмент плана дня
    FreePoolFragment        - фрагмент свободной практики
    LibraryFragment         - фрагмент библиотеки карточек
    SettingsFragment        - фрагмент настройки

    TrasportActivities - содержит статические функции перемещения между активностями
    ActivitiesUtils - общие функции для активностей, которые никак не касаются перемещения между
        активностями

    вспомогательные классы:
    VocabularyCard - карточка карты слова

    классы для передачи данных в адаптеры
    toDoUnit            - в фрагменте DayPlanFragment
    TextBlock           - в активности MainActivity
    ButtonLetterUnit    - для активностей написания слов - для кнопочек
    Им же соотвествуют адаптеры

    transportPreferences - статические функции транспортировки преференсов
    consts - список обозначений


 */




package com.lya_cacoi.lotylops.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.lya_cacoi.lotylops.R;
import com.lya_cacoi.lotylops.ruler.Ruler;

import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.removeTitleBar;
import static com.lya_cacoi.lotylops.activities.utilities.ActivitiesUtils.setOrientation;


public class startAppActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Fragment fragment;
    public FragmentTransaction ft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_holder);
        setOrientation(this);
        removeTitleBar(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        /*

        настройка для первого запуска приложения


        1. Определение цели:
            1.1 переезд в другую страну
            1.2 обучение для чтения
            (влияет на аудиальный/традиционный подходы)

            2. Уровень владения языка (для каждого раздела свой тест)
                можно пройти тест
                можно выбрать учителя
                можно отказаться от определения уровня и самому выбрать курсы

            3. В будущем (выбор тем - подписывает на новости и курсы)

            4. выбрать скорость обучения (настройки для каждой секции будут определены раннее)
               5-10-15-20 минут


         */

        /*

        СНАЧАЛА РЕАЛИЗУЙ ПОМОЩЬ, ПОТОМ ВХОДНЫЕ НАСТРОЙКИ

        есть 2 варианта 1 - уже выбраны материалы для изучения - тогда сразу кидаем на обучение
        2  - еще не выбраны - тогда сначала показываем, где взять, а потом кидаем на обучение


        помощь - используй transportPreferences, чтобы хранить инфу о том, какое сообщение показывать

         */

        fragmentManager = getSupportFragmentManager();
        final FragmentTransaction ft = fragmentManager.beginTransaction();

        fragment = new StartHelpFragment();
        ft.add(R.id.fragment_container, fragment);
        ft.commit();

    }

    public void slideFragment(Fragment fragment){
        this.fragment = fragment;
        ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_right, R.anim.slide_left);
        ft.replace(R.id.fragment_container, this.fragment);
        ft.commit();
    }

    public void moveToMain(){
        Ruler.finishStart(getApplicationContext());

        Intent intent = new Intent(getApplicationContext(), mainPlain.class);
        startActivity(intent);
        finish();
    }

}