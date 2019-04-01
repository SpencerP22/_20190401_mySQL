import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        String password = getPassword();
        String state = getState();
        ArrayList<WeatherStation> list = new ArrayList<>();
        getStations(state, password, list);
        Collections.sort(list);
        print(list);

        System.out.println("Done");
    }

    public static void print(ArrayList<WeatherStation> list) {
        System.out.printf("------- Station count: %d -------\n", list.size());
        for(WeatherStation ws : list) {
            System.out.println(ws);
        }
    }

    public static void getStations(String state, String password, ArrayList<WeatherStation> list){
        String server = "noynaert.cs.missouriwestern.edu";
        String user = "act311";
        String database = "misc";

        String connectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", server, database, user, password);

        System.out.println("The connection string is " + connectionString);

        //set up the actual connection to the database
        try {
            Connection conn = DriverManager.getConnection(connectionString);
            String queryString = String.format("SELECT place, latitude, longitude FROM weather WHERE state LIKE '%s'", state);
            System.out.println("The query string is " + queryString);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(queryString);

            while(rs.next()) {
                String city = rs.getString("place");
                String latitude = rs.getString("latitude");
                String longitude = rs.getString("longitude");
                WeatherStation ws = new WeatherStation(city, latitude, longitude);
                list.add(ws);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection failed");
            System.exit(1);
        }

        System.out.println("Done with query");
    }

    public static String getState(){
        System.out.print("What is the state's two letter abbreviation?: ");
        Scanner input = new Scanner(System.in);
        String state = input.next();
        return state;
    }

    public static String getPassword(){
        System.out.print("What is the password?: ");
        Scanner input = new Scanner(System.in);
        String password = input.next();
        return password;
    }
}
