import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Train implements Serializable {
    private static final long serialVersionUID = 40494049L;
    int trainNumber;
    String stationDest;
    int departureHour;
    int departureMinute;
    boolean isThereTickets;

    Train(int trainNumber, String stationDest, int departureHour, int departureMinute, boolean isThereTickets) {
        this.trainNumber = trainNumber;
        this.stationDest = stationDest;
        this.departureHour = departureHour;
        this.departureMinute = departureMinute;
        this.isThereTickets = isThereTickets;
    }

    static Main.Status saveTrains(List<Train> trains, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(trains);
        } catch (IOException e) {
            return Main.Status.UNWRITABLE_FILE;
        }
        return Main.Status.GOOD;
    }

    static Main.Status loadTrains(List<Train> trains, String fileName) {
        trains.clear();
        try (FileInputStream fos = new FileInputStream(fileName)) {
            ObjectInputStream ois = new ObjectInputStream(fos);
            trains.addAll((ArrayList<Train>) ois.readObject());
        } catch (InvalidClassException e) {
            return Main.Status.WRONG_SERIALIZATION;
        } catch (ClassCastException | ClassNotFoundException | IOException e) {
            return Main.Status.BAD_FILE;
        } catch (Exception e) {
            return Main.Status.UNREADABLE_FILE;
        }
            return Main.Status.GOOD;
        }
    }