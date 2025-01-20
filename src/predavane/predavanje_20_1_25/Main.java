package predavane.predavanje_20_1_25;

import predavane.predavanje_16_1_25.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        Connection connection = DatabaseService.createConnection();

        connection.setAutoCommit(false);

//        try {
//
//            String query1 = "INSERT INTO Drzava (Naziv) VALUES ('Srbija')";
//            PreparedStatement statement1 = connection.prepareStatement(query1);
//
//            String query2 = "INSERT INTO Drzava (Id, Naziv) VALUES (123, 'Srbija')";
//            PreparedStatement statement2 = connection.prepareStatement(query2);
//
//            statement1.executeUpdate();
//            statement2.executeUpdate();
//
//            connection.commit();
//
//        } catch (SQLException e) {
//            System.out.println(e);
//            connection.rollback();
//        }

//        connection.close();

        while (true) {
            System.out.println("Odaberi jednu opciju");
            System.out.println("Unesi 1 ako zelis izbrisati racun i povezane stavke");
            System.out.println("Unesi 2 ako zelis azurirati zalihu proizvoda i generiranti racun");
            System.out.println("Unesi 3 ako zelis izaci iz programa");
            System.out.println("------------------------------------");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();
                scanner.nextLine();

                switch (num) {
                    case 1 -> brisanjeRacunaIPovezanihStavki(connection);
                    case 2 -> azuriranjeZalihaProizvodaIGeneriranjeRacuna(connection);
                    case 3 -> {
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

    private static void brisanjeRacunaIPovezanihStavki(Connection connection) throws SQLException {
        System.out.println("Unesi ID racuna koji zelite izbrisati");
        if (scanner.hasNextInt()) {
            int idRacun = scanner.nextInt();
            String query1 = "DELETE FROM Stavka where RacunID = ?";
            String query2 = "DELETE FROM Racun where IDRacun = ?";

            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);

                preparedStatement1.setInt(1, idRacun);

                preparedStatement2.setInt(1, idRacun);

                preparedStatement1.execute();
                preparedStatement2.execute();

                connection.commit();

                System.out.println("Brisanje racuna i stavki sa ID = " + idRacun);


            } catch (SQLException e) {
                System.out.println("Exception = " + e.getMessage());
                connection.rollback();
            }


        } else {
            System.out.println("Niste unjeli valjan broj");
            scanner.nextLine();
        }

    }

    private static void azuriranjeZalihaProizvodaIGeneriranjeRacuna(Connection connection) throws SQLException {

        System.out.println("Unesi broj racuna");

        String brojRacuna = scanner.nextLine();


        System.out.println("Unesi ID kupca");
        if (scanner.hasNextInt()) {
            int kupacID = scanner.nextInt();

            String query1 = "INSERT into Racun (DatumIzdavanja,BrojRacuna,KupacID) VALUES (SYSDATETIME(), ?,?)";
            String query2 = "select * from Racun WHERE BrojRacuna = ?";
            String query3 = "INSERT into Stavka (RacunID,Kolicina,ProizvodID,CijenaPoKomadu,PopustUPostocima,UkupnaCijena) VALUES (?,1,776,2024.994,0.00,2024.994000);";
            String query4 = "UPDATE Proizvod SET MinimalnaKolicinaNaSkladistu = MinimalnaKolicinaNaSkladistu - 1 WHERE IDProizvod = 776";
            String query5 = "SELECT MinimalnaKolicinaNaSkladistu FROM Proizvod WHERE IDProizvod = 776";

            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, brojRacuna);
                preparedStatement1.setInt(2, kupacID);
                preparedStatement1.executeUpdate();

                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
                preparedStatement2.setString(1, brojRacuna);
                ResultSet rs = preparedStatement2.executeQuery();

                int racunID;
                if (rs.next()) {
                    racunID = rs.getInt("IDRacun");
                } else {
                    throw new SQLException("Racun nije pronaden");
                }

                PreparedStatement preparedStatement3 = connection.prepareStatement(query3);
                preparedStatement3.setInt(1, racunID);
                preparedStatement3.executeUpdate();

                PreparedStatement preparedStatement4 = connection.prepareStatement(query4);
                preparedStatement4.executeUpdate();

                PreparedStatement preparedStatement5 = connection.prepareStatement(query5);
                rs = preparedStatement5.executeQuery();

                int minimalnaKolicina = rs.getInt("MinimalnaKolicinaNaSkladistu");
                if (minimalnaKolicina < 0) {
                    throw new SQLException("Minimalna kolicina je manja od 0");
                }


                connection.commit();
                System.out.println("Dodano uspjesno");

            } catch (SQLException e) {
                System.out.println("Exception = " + e.getMessage());
                connection.rollback();
            }
        } else {
            System.out.println("Niste unjeli valjan broj");
            scanner.nextLine();
        }


    }
}
