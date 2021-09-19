package com.lya_cacoi.lotylops.SenteceCheck;

import java.util.ArrayList;

import static com.lya_cacoi.lotylops.SenteceCheck.Decoder.allParts;

public class Tester{

    private static boolean isFullResult = false;

    private static int counter = 0;

    private static void generate(ArrayList<Part> arr, int[] current, int next, boolean[] used) {
        if (next == arr.size()) {//Если уже полностью выбрали очередной вариант
            check(current);
            return;
        }

        for (int i = 0; i < arr.size(); i++) {//смотрим на каждый элемент из исхождного массива
            if (!used[i]) {//если он еще не использовался - ставим его следующим
                used[i] = true;//отмечаем его как уже использованный
                current[next] = i;//записываем его в наш сейчашний вариант
                generate(arr, current, next + 1, used);//рекурсивно выбираем следующий элемент и т.д.
                used[i] = false;//пробуем дальше - убираем этот элемент, чтобы поставить какой-то другой
            }
        }
    }

    private static void check(int[] current){
        ArrayList<String> sentence = new ArrayList<>();
        for (int i: current){
            sentence.add(allParts.get(i).name);
        }

        for (SentenceChecker.Check check : SentenceChecker.Check.values()) {
            check.check(sentence, part);
        }
        if (!isFullResult) {
            if (SentenceChecker.isRight())
                System.out.println("right: " + sentence);
        } else{
            if (SentenceChecker.isRight())
                System.out.println(sentence + " --- right");
            else{
                System.out.println(sentence);
                SentenceChecker.showCommand();
            }
            System.out.println("-------\n\n--------");
        }
        reset();


    }

    public static void TestForOne(String sentence, String check){
        part = Decoder.decoder(sentence);
        if (part == null) return;
        ArrayList<String> checking = SentenceChecker.prepare(check);
        for (SentenceChecker.Check c : SentenceChecker.Check.values()) {
            c.check(checking, part);
        }
        if (SentenceChecker.isRight())
            System.out.println("right");
        else {
            System.out.println(sentence);
            SentenceChecker.showCommand();
        }
        reset();
    }

    private static Part part;
        public static void Test(String sentence) {

            part = Decoder.decoder(sentence);
            if (part == null) return;

            int[] current = new int[allParts.size()];
            boolean[] used = new boolean[allParts.size()];

            int empties = 0;


            for (int i = 0; i < allParts.size(); i++){
                if (allParts.get(i).name.length() == 0 || allParts.get(i).name.equals(" ")) {
                    empties++;
                    current[i] = -1;
                    used[i] = true;
                    continue;
                }
                current[i-empties] = i;
                used[i-empties] = false;
            }


            generate(allParts, current, 0, used);
        }
        public static void reset(){

            for (Part p : allParts){
                p.watched = 0;
                p.id = -1;
            }
            if (SentenceChecker.id != null) {
                SentenceChecker.id.clear();
                SentenceChecker.empty_id.clear();
                SentenceChecker.unfounded_id.clear();
                SentenceChecker.commands.clear();
                //allParts.clear();
            }

        }

        public static ArrayList<String> generateRight(String string){
            //Part sentence = Decoder.decoder(string);
            //while
            return null;
        }

    public static void setIsFullResult(boolean type) {
            isFullResult = type;
    }
}
