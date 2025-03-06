import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class Main {
    public static final Scanner scan = new Scanner(System.in);
    public static final int MIN_CHOICE = 0;
    public static final int MAX_CHOICE = 6;
    public static final int MAX_TRAIN_CHOICE = 4;
    public static final int MIN_TRAINNUM = 0;
    public static final int MAX_TRAINNUM = 1000000;
    public static final int MIN_HOUR = 0;
    public static final int MAX_HOUR = 23;
    public static final int MIN_MINUTE = 0;
    public static final int MAX_MINUTE = 59;
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
        WRONG_SERIALIZATION,
        OUT_OF_RANGE_STR,
        OUT_OF_RANGE_TIME,
        OUT_OF_RANGE_TICKETS,
        BAD_FILE_NAME
    }


    static String[] ERRTEXT = {"",
            "Заданный файл имеет неверное расширение.\n",
            "Этот файл пустой.\n",
            "Данный файл невозможно прочитать.\n",
            "Недостаточно прав для записи в этот файл.\n",
            "Введенные данные не соответствуют опциям выбора.\n",
            "Файл содержит некорректные данные.\n",
            "Введенное число не является целым числом в заданном диапазоне.\n",
            "Введенное значение вне диапазона.\n",
            "Данный файл .dat был сохранен другой программой.\n",
            "Строка слишком большого размера.\n",
            "Время задано неправильно.\n",
            "Неправильный ввод. Попробуйте еще раз.\n",
            "Название файла должно быть на английском.\n"
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
            if(!pathToFile.matches("^[a-zA-Z0-9._-]+$")){
                err = Status.BAD_FILE_NAME;
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


    public static String createTrainsDat(List<Train> trains, String[] currentPath){
        Train.saveTrains(trains, currentPath[0]);
        currentPath[0] = getFilePath();
        File f = new File(currentPath[0]);
        if(f.exists()){
            if(askToDelete(f) == 1) {
                f.delete();
                Train.saveTrains(trains, currentPath[0]);
            }
            else {
                Train.loadTrains(trains, currentPath[0]);
            }
        }
            else {
            Train.saveTrains(trains, currentPath[0]);
        }

            return currentPath[0];
    }

    public static String openTrainsDat(List<Train> trains, String[] currentPath){
        boolean isReadable;
        do {
            isReadable = false;
            currentPath[0] = getFilePath();
            File f = new File(currentPath[0]);
            if (f.canRead()) {
                Train.loadTrains(trains, currentPath[0]);
                isReadable = true;
            } else {
                System.out.println(ERRTEXT[3]);
            }
        }while(!isReadable);

        return currentPath[0];
    }

    public static int getTrainNumber(){

        Status err;
        int number = 0;
        do {
            err = Status.GOOD;
            System.out.println("Введите номер поезда. (1-1000000)");

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


    public static void showTrain(Train train){
        System.out.println("\tНомер поезда: " + train.trainNumber);
        System.out.println("\tСтанция назначения: " + train.stationDest); //sta time tick
        System.out.println("\tВремя отправления: " + train.departureHour + ':' + train.departureMinute);
        if(train.isThereTickets){
            System.out.println("\tБилеты на поезд есть");
        }
        else {
            System.out.println("\tБилетов на поезд нет");
        }
    }

    public static void showTrainsInTime(List<Train> trains){
        int[][] timeInterval = new int[2][2];
        System.out.println("Введите начало временного интервала для поиска.");
        timeInterval[0] = getDepartureTime();
        System.out.println("Введите конец временного интервала для поиска.");
        timeInterval[1] = getDepartureTime();
        for(Train train : trains){
            if(train.departureHour > timeInterval[0][0] && train.departureHour < timeInterval[1][0]
            && train.departureMinute > timeInterval[0][1] && train.departureMinute < timeInterval[1][1]){
                showTrain(train);
            }
        }
    }

    public static void showAllTrains(List<Train> trains){

        for(int i = 0; i < trains.size(); i++){
            System.out.print(i + 1 + ")");
            showTrain(trains.get(i));
        }
    }

    public static int chooseTrain(List<Train> trains){
        showAllTrains(trains);
        Status err;
        int number = 0;
        do {
            err = Status.GOOD;
            System.out.println("Введите порядковый номер поезда.");

            try {
                number = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                err = Status.NOT_AN_INT;
            }
            if (err == Status.GOOD && (number < 1 || number > trains.size())) {
                err = Status.OUT_OF_RANGE;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        } while (err != Status.GOOD);

        return number;


    }

    public static void deleteTrain(List<Train> trains, String[] currentPath){
        int number = chooseTrain(trains) - 1;
        trains.remove(number);
        Train.saveTrains(trains, currentPath[0]);
    }

    public static void writeTrainOptions(){
        System.out.println("""
                Выберите что хотите изменить
                0 - ничего;
                1 - номер поезда;
                2 - станция назначения;
                3 - время отправления;
                4 - наличие билетов.
                """);
    }

    public static int getChoiceToChange(){
        Status err;
        int input = 0;
        do {
            err = Status.GOOD;
            writeTrainOptions();
            try {
                input = Integer.parseInt(scan.nextLine());
            } catch (NumberFormatException e) {
                err = Status.NOT_AN_INT;
            }
            if (err == Status.GOOD && (input < MIN_CHOICE || input > MAX_TRAIN_CHOICE)) {
                err = Status.OUT_OF_RANGE;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        } while (err != Status.GOOD);


        return input;

    }


    public static void changeTheTrain(List<Train> trains, String[] currentPath){
        int choice;
        Train temp;
        int number = chooseTrain(trains) - 1;
        do {
            choice = getChoiceToChange();

            switch (choice) {
                case 1:
                    temp = trains.get(number);
                    temp.trainNumber = getTrainNumber();
                    trains.set(number, temp);
                    break;
                case 2:
                    temp = trains.get(number);
                    temp.stationDest = getStation();
                    trains.set(number, temp);
                    break;
                case 3:
                    temp = trains.get(number);
                    int[] times;
                    times = getDepartureTime();
                    temp.departureHour = times[0];
                    temp.departureMinute = times[1];
                    trains.set(number, temp);
                    break;
                case 4:
                    temp = trains.get(number);
                    temp.isThereTickets = getTickets();
                    break;
            }

        }while(choice > 0);
        Train.saveTrains(trains, currentPath[0]);
    }

    public static String getStation(){
        String input = "";
        Status stat;
        do {
            stat = Status.GOOD;
            System.out.println("Введите станцию назначения. (Максимальная длина названия - 83 символа. ");
            input = scan.nextLine();
            if (input.length() > 83) {
                stat = Status.OUT_OF_RANGE;
            }
            System.out.println(ERRTEXT[stat.ordinal()]);
        }while(stat != Status.GOOD);

        return input;
    }

    public static int[] getDepartureTime(){

        Status err;
        String input = "";
        int[] numbers = new int[2];
        do {
            err = Status.GOOD;
            try {
                input = scan.nextLine();
                numbers[0] = Integer.parseInt(input.split(":")[0]);
                numbers[1] = Integer.parseInt(input.split(":")[1]);
            } catch (NumberFormatException e) {
                err = Status.NOT_AN_INT;
            }
            if (err == Status.GOOD && (numbers[0] < MIN_HOUR || numbers[0] > MAX_HOUR || numbers[1] < MIN_MINUTE || numbers[1] > MAX_MINUTE
                    || input.split(":")[0].length() != 2 || input.split(":")[1].length() != 2)) {
                err = Status.OUT_OF_RANGE_TIME;
            }
            System.err.print(ERRTEXT[err.ordinal()]);
        } while (err != Status.GOOD);




        return numbers;
    }

    public static boolean getTickets(){
        boolean isThereTickets = false;
        Status stat = Status.GOOD;
        String input;
        do{
            System.out.println("Введите наличие билетов (Есть/Нет).");
            input = scan.nextLine().toLowerCase().trim();
            if(input.equals("есть")){
                isThereTickets = true;
            }
            else if(input.equals("нет")){
                isThereTickets = false;
            }
            else {
                stat = Status.OUT_OF_CHOICE;
            }
            System.err.printf(ERRTEXT[stat.ordinal()]);

        }while(stat != Status.GOOD);

        return isThereTickets;
    }


    public static void addTheTrain(List<Train> trains, String[] currentPath){

        int trainNumber = getTrainNumber();
        boolean trainFound = false;
        String stationDest = "";
        for(Train train : trains){
            if(!trainFound && train.trainNumber == trainNumber){
                trainFound = true;
                stationDest = train.stationDest;

            }
        }

        if (trainFound) {
            System.out.println("Мы нашли поезд с этими данными (станция назначения автоматически заполнилась).");
        }
        else {
            stationDest = getStation();
        }
        System.out.println("Введите время отправления в формате HH:MM.");
        int[] departureTime = getDepartureTime();
        boolean isThereTickets = getTickets();





        Train trainNew = new Train(trainNumber, stationDest, departureTime[0], departureTime[1], isThereTickets);
        trains.add(trainNew);
        Status stat;
        do {
            stat = Train.saveTrains(trains, currentPath[0]);
            if (stat != Status.GOOD) {
                System.out.println(ERRTEXT[stat.ordinal()]);
                createTrainsDat(trains, currentPath);
            }
        }while(stat != Status.GOOD);

    }

    public static void workWithDatabase(){
        boolean cantLeave = true;
        List<Train> trains = new ArrayList<Train>();
        String[] currentPath = new String[1];
        currentPath[0] = "";
        createTrainsDat(trains, currentPath);
        while (cantLeave) {
            switch (getChoice()) {
                case 0:
                    cantLeave = false;
                    break;
                case 1:
                    addTheTrain(trains, currentPath);
                    break;
                case 2:
                    if(trains.isEmpty()) {
                        System.out.println("Список поездов пустой, сначала добавьте хоть один поезд.");
                    }
                    else {
                        changeTheTrain(trains, currentPath);
                    }
                    break;
                case 3:
                    if(trains.isEmpty()) {
                        System.out.println("Список поездов пустой, сначала добавьте хоть один поезд.");
                    }
                    else {
                        deleteTrain(trains, currentPath);
                    }
                    break;
                case 4:
                    if(trains.isEmpty()) {
                        System.out.println("Список поездов пустой, сначала добавьте хоть один поезд.");
                    }
                    else {
                        showAllTrains(trains);
                    }
                    break;
                case 5:
                    if(trains.isEmpty()) {
                        System.out.println("Список поездов пустой, сначала добавьте хоть один поезд.");
                    }
                    else {
                        showTrainsInTime(trains);
                    }
                    break;
                case 6:
                    if(trains.isEmpty()) {
                        System.out.println("Список поездов пустой, сначала добавьте хоть один поезд.");
                    }
                    else {
                        checkTickets(trains);
                    }
                    break;
                case 7:
                    currentPath[0] = openTrainsDat(trains, currentPath);
                    break;
                case 8:
                    currentPath[0] = createTrainsDat(trains, currentPath);
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