package predavane.predavanje_15_1_25;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {


        while (true) {
            System.out.println("Odaberi jednu opciju");
            System.out.println("Unesi 1 ako zelis unjeti novi grad");
            System.out.println("Unesi 2 ako zelis izmjeniti postojeci grad");
            System.out.println("Unesi 3 ako zelis ukloniti postojeci grad");
            System.out.println("Unesi 4 ako zelis prikaz svih gradova sortiranih po nazivu");
            System.out.println("Unesi 5 ako zelis izaci iz programa");
            System.out.println("------------------------------------");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();

                switch (num) {
                    case 1 -> dodajNoviGrad();
                    case 2 -> izmjeniPostojeciGrad();
                    case 3 -> brisanjePostojecegGrada();
                    case 4 -> sviGradviSortiraniPoNazivuASC();
                    case 5 -> {
                        System.out.println("Izlaz");
                        return;
                    }
                    default -> System.out.println("Krivi unos pokusaj ponovo");

                }
            } else {
                System.out.println("Molimo unesite valjan broj.");
                scanner.nextLine();
            }
            System.out.println("------------------------------------");
        }

    }

    private static void dodajNoviGrad() {
        System.out.println("Unesite ime grada koji zelite dodati");

        String noviGrad = scanner.nextLine();

        String query = "INSERT INTO Grad (Naziv) VALUES (?)";

        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, noviGrad);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Unijeli ste: " + noviGrad);
                sviGradviSortiraniPoNazivuASC();
            } else {
                System.out.println("Dodavanje grada nije uspjelo.");
            }
        } catch (SQLException e) {
            System.out.println("Exception : " + e.getMessage());
        }

    }

    private static void izmjeniPostojeciGrad() {
        sviGradviSortiraniPoNazivuASC();

        System.out.println("Unesite ID grada kojem zelite izmjeniti ime");

        int idGrada = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Unesite novo ime grada ");

        String noviGrad = scanner.nextLine();

        String query = "UPDATE Grad SET Naziv = ? where IDGrad = ?";

        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, noviGrad);
            preparedStatement.setInt(2, idGrada);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Izmjenili ste grad sa ID-om : " + idGrada + " novo ime grada je : " + noviGrad);
                sviGradviSortiraniPoNazivuASC();
            } else {
                System.out.println("Izmjena grada nije uspjela.");
            }
        } catch (SQLException e) {
            System.out.println("Exception : " + e.getMessage());
        }

    }

    private static void brisanjePostojecegGrada() {
        System.out.println("Unesite ime ID grada koji zelite ukloniti sa liste");

        int idGrada = scanner.nextInt();

        String query = "DELETE FROM Grad WHERE IDGrad = ?";

        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, idGrada);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Uklonili ste: " + idGrada);
                sviGradviSortiraniPoNazivuASC();
            } else {
                System.out.println("Brisanje grada nije uspjelo.");
            }
        } catch (SQLException e) {
            System.out.println("Exception : " + e.getMessage());
        }
    }


    private static void sviGradviSortiraniPoNazivuASC() {

        String query = "SELECT * FROM Grad order by Naziv ASC";


        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                System.out.println("ID :  " + rs.getInt("IDGrad") + " , Naziv  " + rs.getString("Naziv"));
            }

        } catch (SQLException e) {
            System.out.println("Exception : " + e.getMessage());
        }

    }
}
