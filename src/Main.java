import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.*;

public class Main {

    static Scanner sc = new Scanner(System.in);

    private static Connection connect() {
        String url = "jdbc:sqlite:/Users/wkarl/Desktop/Databaser/Lab3.db";
        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return connection;
    }

    public static void main(String[] args) {

        boolean flag = true;
        
        while (flag) {

            showMenuOptions();
            String userOption = sc.nextLine();

            switch (userOption) {
                case "1" -> showAllItems();
                case "2" -> addNewCar();
                case "3" -> updateCar();
                case "4" -> deleteACar();
                case "5" -> searchForACar();
                case "6" -> showTotalCarsAndAvgPrice();
                case "0" -> {
                    System.out.println("Shutting down..");
                    flag = false;
                }
                default -> System.out.println("Not an option, try again: ");
            }
        }
    }

    private static void showTotalCarsAndAvgPrice() {

        String sql = "SELECT COUNT(carID) AS NumOfCars, AVG(price) AS AvgPrice FROM CarDealership";

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            System.out.println("Number of cars: " + resultSet.getInt("NumOfCars") + "\t"
                    + "Average price: " + resultSet.getInt("AvgPrice"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    private static void searchForACar() {
        System.out.println("Searching..");
        String searchSQL = "SELECT * FROM CarDealership WHERE manufacturer = ? ";


        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

            System.out.println("Enter manufacturer: ");
            String userSearch = sc.nextLine().toUpperCase();

            preparedStatement.setString(1, userSearch);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                System.out.println("ID: " + resultSet.getInt("carID") + "\t" +
                        "Manufacturer: " + resultSet.getString("manufacturer") +  "\t" +
                        "Model: " + resultSet.getString("model") + "\t" +
                        "Year: " + resultSet.getInt("yearOfModel") + "\t" +
                        "Price: " + resultSet.getInt("price"));
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteACar() {
        System.out.println("Enter the ID of the car you want to delete: ");
        int inputCarId = sc.nextInt();
        deleteFromDB(inputCarId);
        sc.nextLine();
    }

    private static void deleteFromDB(int carId) {
        String sql = "DELETE FROM CarDealership WHERE carID = ?";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, carId);
            preparedStatement.executeUpdate();
            System.out.println("The car was deleted successfully.");

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void updateCar() {

        System.out.println("Enter the manufacturer: ");
        String manufacturer = sc.nextLine().toUpperCase();
        System.out.println("Enter the model: ");
        String model = sc.nextLine().toUpperCase();
        System.out.println("Enter the year of model: ");
        int inputYear = sc.nextInt();
        System.out.println("Enter the new price of the car: ");
        int price = sc.nextInt();
        System.out.println("Enter the ID of the car: ");
        int inputID = sc.nextInt();
        updateCarInSQL(manufacturer, model, inputYear, price, inputID);
        sc.nextLine();


    }

    private static void updateCarInSQL( String manufacturer, String model, int year, int price, int inputID) {
        String updateSQL = "UPDATE CarDealership SET manufacturer = ? , "
                + "model = ? , "
                + "yearOfModel = ? , "
                + "price = ?  "
                + "WHERE carID = ?";

        try (
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(1, manufacturer);
            preparedStatement.setString(2, model);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, price);
            preparedStatement.setInt(5, inputID);

            preparedStatement.executeUpdate();
            System.out.println("Car was updated");


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void addNewCar() {
        System.out.println("Enter the manufacturer: ");
        String inputManufacturer = sc.nextLine().toUpperCase();
        System.out.println("Enter the model name: ");
        String inputModel = sc.nextLine().toUpperCase();
        System.out.println("Enter year of model: ");
        int inputYear = sc.nextInt();
        System.out.println("Enter the price: ");
        int inputPrice = sc.nextInt();
        insertNewCarToSQL(inputManufacturer, inputModel, inputYear, inputPrice);
        sc.nextLine();

    }

    private static void insertNewCarToSQL(String manufacturer, String model, int year, int price) {
        String sql = "INSERT INTO CarDealership(manufacturer, model, yearOfModel, price) VALUES(?,?,?,?)";

        try {
            Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, manufacturer);
            preparedStatement.setString(2, model);
            preparedStatement.setInt(3, year);
            preparedStatement.setInt(4, price);
            preparedStatement.executeUpdate();
            System.out.println("The new car was added.");

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void showAllItems() {
        System.out.println("ALL CARS");
        System.out.println("----------------------");
        String sql = "SELECT * FROM CarDealership";

        try {
            Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {


                System.out.println("ID: " + resultSet.getInt("carID") + "\t" +
                        "Manufacturer: " + resultSet.getString("manufacturer") +  "\t" +
                        "Model: " + resultSet.getString("model") + "\t" +
                        "Year: " + resultSet.getInt("yearOfModel") + "\t" +
                        "Price: " + resultSet.getInt("price"));
            }
        }

        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void showMenuOptions() {
        System.out.println("""
                MENU:\s
                ------------------------------
                 1. SHOW ALL CARS
                 2. ADD A CAR
                 3. UPDATE A CAR
                 4. DELETE A CAR
                 5. SEARCH FOR CARS BY MANUFACTURER
                 6. LIST AMOUNT OF CARS AND AVERAGE PRICE
                 0. Save and exit program""");

        System.out.println("------------------------------\nEnter a menu option and press enter:");
    }
}
