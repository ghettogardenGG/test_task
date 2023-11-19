import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        String csvFile = "C:\\test_task\\абоненты.csv";
        String line = "";
        String[] abonent = new String[0];
        ArrayList<ArrayList<String>> sourceData = new ArrayList<>();
        int numColumn = 8;


        for(int i = 0;i < numColumn; ++i){      //Инициализируем двумерный массив sourceData
            ArrayList<String> column = new ArrayList<>();
            sourceData.add(column);
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFile));            //Прочтение файла
            while ((line = br.readLine()) != null){                                     // по строчно
                abonent = line.split(";");                                        //Разделение целой строки на массив стро по разделителю ;
                for(int i = 0;i < 8; ++i){
                    sourceData.get(i).add(abonent[i]);
                }
            }
            br.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ArrayList<String> rowBill = new ArrayList<>();
        rowBill.add("Начислено");
        for(int j = 1;j < sourceData.get(5).size(); ++j){                           //Заполнение и расчёт столбца Начислено
            if(sourceData.get(5).get(j).equals("2")){
                int currValue = Integer.parseInt(sourceData.get(7).get(j));
                int prevValue = Integer.parseInt(sourceData.get(6).get(j));
                double result = (currValue - prevValue) * 1.52;
                rowBill.add(String.format(Locale.US,"%.2f", result));                 //Установка двух знаков после запятой
            }else{
                rowBill.add(Double.toString(301.26));
            }
        }
        sourceData.add(rowBill);
        try {                                                           //Создание файла и его заполнение
            BufferedWriter writer = new BufferedWriter(new FileWriter("Начисления_абоненты.csv"));
            for(int i = 0;i < sourceData.get(0).size(); ++i){
                String row = "";
                for(int j = 0;j < sourceData.size(); ++j){
                    row = row + sourceData.get(j).get(i) + ";";
                }
                row += "\n";
                writer.write(row);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for(int i = 0; i < sourceData.size(); ++i){
            for(int j = 0; j < sourceData.get(i).size(); ++j){
                System.out.println(sourceData.get(i).get(j));
            }
        }

        HashSet<String> setStreet = new HashSet<>();
        for(int i = 0; i < sourceData.size(); ++i){                     //Получение множества улиц(набор всех улиц без повторения)
            for(int j = 1; j < sourceData.get(2).size(); ++j){
                setStreet.add(sourceData.get(2).get(j));
            }
        }

        ArrayList<ArrayList<String>> array2dListClientHome = new ArrayList<>();         //Двумерный массив, где каждая колонка это список порядковых номеров клиентов
                                                                                        // по определенной улице, название улицы это первый элемент столбца

        ArrayList<HashSet<String>> arraySetHouses = new ArrayList<>();                  //Множество домов по улицам, где каждый элемент списка это множество домов,
                                                                                        //порядок улиц такой же как и в двумерном масссиве array2dListClientHome
        for(String s : setStreet){
            ArrayList<String> row = new ArrayList<>();
            row.add(s);
            HashSet<String> setHouse = new HashSet<>();
            for(int i = 0; i < sourceData.get(2).size(); ++i){
                if(sourceData.get(2).get(i).equals(s)){
                    row.add(sourceData.get(0).get(i));
                    setHouse.add(sourceData.get(3).get(i));
                }
            }
            array2dListClientHome.add(row);
            arraySetHouses.add(setHouse);
        }

        ArrayList<ArrayList<String>> houseBills = new ArrayList<>();            //Двумерный массив будущего csv файла
        for(int i = 0;i < 4; ++i){
            ArrayList<String> column = new ArrayList<>();
            houseBills.add(column);
        }
        houseBills.get(0).add("№ строки");
        houseBills.get(1).add("Улица");
        houseBills.get(2).add("№ дома");
        houseBills.get(3).add("Начислено");
        double sum = 0;
        int count = 1;
        for(int i = 0; i < arraySetHouses.size(); ++i){                             //Прохождение массива улиц по множеству домов
            for (String s : arraySetHouses.get(i)){                                 //Прохождение по множеству домов определённой улицы
                   for(int j = 1;j < array2dListClientHome.get(i).size(); ++j){     //Прохождении по массиву номеров домов на определенной улице
                       if(sourceData.get(3).get(Integer.parseInt(array2dListClientHome.get(i).get(j))).equals(s)){      //Если номер дома из списка домов на определенной улице
                           //равен дому из множества, то складываем начисления в сумму
                            sum += Double.parseDouble(rowBill.get(Integer.parseInt(array2dListClientHome.get(i).get(j))));
                       }
                   }
                   houseBills.get(0).add(Integer.toString(count));
                   houseBills.get(1).add(array2dListClientHome.get(i).get(0));
                   houseBills.get(2).add(s);
                   houseBills.get(3).add(String.format(Locale.US,"%.2f", sum));
                   count++;
                   sum = 0;
            }
        }



        try {                                                           //Создание файла и его заполнение
            BufferedWriter writer = new BufferedWriter(new FileWriter("Начисления_дома.csv", Charset.forName("cp1251")));
            for(int i = 0;i < houseBills.get(0).size(); ++i){
                String row = "";
                for(int j = 0;j < houseBills.size(); ++j){
                    row = row + houseBills.get(j).get(i) + ";";
                }
                row += "\n";
                writer.write(row);
            }
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}