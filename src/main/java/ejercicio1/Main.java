package ejercicio1;

import java.awt.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) throws SQLException {
        inicializar();

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new AgregarParticipante();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });
    }

    private static void inicializar() {
        try (var connection = DriverManager.getConnection(
                "jdbc:derby:memory:participantes;create=true",
                "app",
                "app")) {
            var stmt = connection.createStatement();
            createTableParticipantes(stmt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void createTableParticipantes(Statement stmt) throws SQLException {
        stmt.executeUpdate("CREATE TABLE participantes (id_participante INT NOT NULL "
                + " primary key generated always as identity (start with 1, increment by 1), "
                + " nombre varchar(255), telefono varchar(15), region varchar(50))");
    }
}

