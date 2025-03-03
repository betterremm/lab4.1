import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static final int MIN_CHOICE = 0;
    public static final int MAX_CHOICE = 6;
    public static final int MIN_TRAINNUM = 0;
    public static final int MAX_TRAINNUM = 10000;
    public enum Status
    {
        GOOD,
        NOT_DAT,
        EMPTY_FILE,
        UNREADABLE_FILE,
        UNWRITABLE_FILE,
        OUT_OF_CHOICE,
        BAD_FILE,
        NOT_AN_INT,
        OUT_OF_RANGE,
        WRONG_SERIALIZATION
    }


    static String[] ERRTEXT = {"",
            "Заданный файл имеет неверное расширение.\n",
            "Этот файл пустой.\n",
            "Данный файл невозможно прочитать.\n",
            "Недостаточно прав для записи в этот файл.\n",
            "Введенное число не соответствует опциям выбора.\n",
            "Файл содержит некорректные данные.\n",
            "Введенное число не является целым числом в заданном диапазоне.\n",
            "Введенное значение вне диапазона.\n",
            "Данный файл .dat был сохранен другой программой.\n"
    };











    public static void writeTask() {
        System.out.println("Данная программа позволяет работать с записями, хранящими данные о поезде.");
        System.out.println("Ввод/вывод производится через файлы .dat.");
    }

    public static void writeOptions() {
        System.out.println("""
                Выберите, что вы хотите сделать с данными.
                0 - Выход из программы;
                1 - Ввести новую запись;
                2 - Корректировать существующую запись;
                3 - Удалить запись;
                4 - Просмотреть запись на экране;
                5 - Вывести время отправления поездов в город Х во временном интервале от А до Б часов;
                6 - Вывести сведения о наличии билетов на поезде номером XXX.
                7 - Открыть существующий файл.
                8 - Создать новый файл.
                """);
    }

    public static int getChoice(){
        Status err;
        int input = 0;
        do {
            err = Status.GOOD;
            writeOptions();
            try {
                input = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                err = Status.NOT_AN_INT;
            }
            if (err == Status.GOOD && (input < MIN_CHOICE || input > MAX_CHOICE)) {
                err = Status.OUT_OF_RANGE;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        } while (err != Status.GOOD);


        return input;
    }

    public static String getFilePath(){
        String pathToFile;
        Status err;
        System.out.println("Введите название файла (путь к файлу).");
        do {
            pathToFile = scan.nextLine();
            if (pathToFile.endsWith(".dat")) {
                err = Status.GOOD;
            }
            else {
                err = Status.NOT_DAT;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        }while(err != Status.GOOD);
        return  pathToFile;
    }

    public static int askToDelete(File f){
        Status err;
        int input = 0;
        do {
            err = Status.GOOD;
            System.out.println("Файл под названием \"" + f.getName() + "\" уже существует, желаете удалить? (1 - Да, 0 - Нет)");
            try {
                input = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                err = Status.NOT_AN_INT;
            }
            if (err == Status.GOOD && (input < 0 || input > 1)) {
                err = Status.OUT_OF_RANGE;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        } while (err != Status.GOOD);


        return input;
    }


    public static String createTrainsDat(List<Train> trains, String currentPath){
        Train.saveTrains(trains, currentPath);
        currentPath = getFilePath();
        File f = new File(currentPath);
        if(f.exists()){
            if(askToDelete(f) == 1) {
                f.delete();
                Train.saveTrains(trains, currentPath);
            }
            else {
                Train.loadTrains(trains, currentPath);
            }
        }
            else {
            Train.saveTrains(trains, currentPath);
        }

            return currentPath;
    }

    public static String openTrainsDat(List<Train> trains, String currentPath){
        boolean isReadable;
        do {
            isReadable = false;
            currentPath = getFilePath();
            File f = new File(currentPath);
            if (f.canRead()) {
                Train.loadTrains(trains, currentPath);
                isReadable = true;
            } else {
                System.out.println(ERRTEXT[3]);
            }
        }while(!isReadable);

        return currentPath;
    }

    public static int getTrainNumber(){

        Status err;
        int number = 0;
        do {
            err = Status.GOOD;
            writeOptions();
            try {
                number = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                err = Status.NOT_AN_INT;
            }
            if (err == Status.GOOD && (number < MIN_TRAINNUM || number > MAX_TRAINNUM)) {
                err = Status.OUT_OF_RANGE;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        } while (err != Status.GOOD);


        return number;
    }

    public static void checkTickets(List<Train> trains){

        boolean trainFound = false;
        int number = getTrainNumber();
        for(Train train : trains){
            if(train.trainNumber == number){
                if(train.isThereTickets){
                    System.out.println("Билеты на поезд номер " + number + " есть.");
                }
                else{
                    System.out.println("Билетов на поезд номер " + number + " нет.");
                }
                trainFound = true;
            }
        }
        if(!trainFound){
            System.out.println("Информация о поезде " + number + " отсутствует");
        }
    }

    public static void showTrainsInTime(List<Train> trains){
        //get times (A - B)
        //find trains that are between A and B
        //show them
    }

    public static void showAllTrains(List<Train> trains){
        //show trains)
    }

    public static void deleteTrain(List<Train> trains, String currentPath){
        //get a train to delete
        //delete a train
        //train.savetrains
    }

    public static void changeTheTrain(List<Train> trains, String currentPath){
        //get a train to change
        //while(boolean smth)
        //get what to change or change nothing
        //train.savetrains
    }

    public static void addTheTrain(List<Train> trains, String currentPath){
        //get a train number
        //stationDest
        //departure hour and minute
        //is there tickets

        //train.saveTrains
    }

    public static void workWithDatabase(){
        boolean cantLeave = true;
        List<Train> trains = new ArrayList<Train>();
        String currentPath = "";
        openTrainsDat(trains, currentPath);
        while (cantLeave) {
            switch (getChoice()) {
                case 0:
                    cantLeave = false;
                    break;
                case 1:
                    addTheTrain(trains, currentPath);
                    break;
                case 2:
                    changeTheTrain(trains, currentPath);
                    break;
                case 3:
                    deleteTrain(trains, currentPath);
                    break;
                case 4:
                    showAllTrains(trains);
                    break;
                case 5:
                    showTrainsInTime(trains);
                    break;
                case 6:
                    checkTickets(trains);
                    break;
                case 7:
                    currentPath = openTrainsDat(trains, currentPath);
                    break;
                case 8:
                    currentPath = createTrainsDat(trains, currentPath);
                    break;
            }
        }

    }


    public static void main(String[] args) {
        writeTask();
        workWithDatabase();


        scan.close();
    }
}