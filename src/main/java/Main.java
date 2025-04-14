import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Guest guest1 = new Guest("John", "john@test.com", "1654561");
        Guest guest2 = new Guest("David", "david@test.com", "65106");
        Guest guest3 = new Guest("Garry", "garry@test.com", "184165");
        Guest guest4 = new Guest("Frenkie", "frenkie@test.com", "1812361");
        Guest guest5 = new Guest("Robert", "rob@test.com", "674565456");
        Guest guest6 = new Guest("Mark", "mark@test.com", "45646876");
        Guest guest7 = new Guest("Jason", "jason@test.com", "3140407");
        Guest guest8 = new Guest("George", "george@test.com", "47564");
        Guest guest9 = new Guest("Josh", "josh@test.com", "7897240");
        Guest guest10 = new Guest("Harry", "harry@test.com", "045047597");
        Guest guest11 = new Guest("Adrien", "adrien@test.com", "04467321");

        Event event1 = new Event("Black Tie Gala", "O2 Arena" , LocalDate.of(2025, 1, 18));
        Event event2 = new Event("Masquerade Ball", "CopperBox Arena" , LocalDate.of(2021, 5, 17));
        Event event3 = new Event("80s Retro Night", "Nobu" , LocalDate.of(2025, 7, 7));
        Event event4 = new Event("Masquerade Ball", "Wembley Arena", LocalDate.of(2022, 1, 31));

        Connection connection = MySQLConnect.connectToDatabase("src/main/resources/application.properties");

        List<Event> listOfEvents = new ArrayList<>();
        listOfEvents.add(event1);
        listOfEvents.add(event2);
        listOfEvents.add(event3);
        listOfEvents.add(event4);

        insertEventsDB(listOfEvents, connection);

        // provera eventova
        eventStatusCheck(listOfEvents, connection);

        List<Guest> allGuests = new ArrayList<>();
        Random random = new Random();
        allGuests.add(guest1);
        allGuests.add(guest2);
        allGuests.add(guest3);
        allGuests.add(guest4);
        allGuests.add(guest5);
        allGuests.add(guest6);
        allGuests.add(guest7);
        allGuests.add(guest8);
        allGuests.add(guest9);
        allGuests.add(guest10);
        allGuests.add(guest11);

        insertGuestsDB(allGuests, connection);

        fillEvents(allGuests, random, listOfEvents, connection);
        eventGuestList(listOfEvents, connection);
        insertingConfirmedGuests_DB(listOfEvents, allGuests, connection);
        insertingDeclinedGuests_DB(listOfEvents, allGuests, connection);
        insertingNoShownUpGuests_DB(listOfEvents, allGuests, connection);

//        findByEmail("jacob@test.com", connection, listOfEvents);
//        findByName("George", connection, listOfEvents);

//        allGuestInfo(connection);
//        mostReliable(connection);
//        frequentNoShow(connection);
        listEvents(connection);
//        lowAttendance(connection);

        String searchingTheme = "Masquerade Ball";
//        themeLoyalty(searchingTheme, connection);
    }

    private static void insertingConfirmedGuests_DB(List<Event> allEvents, List<Guest> allGuests, Connection connection){
        for(Event event: allEvents){
            if(!event.isCanceled()){
                int eventId = 0;

                // query za nalazenje event id u listi
                String sqlEvent = "SELECT id FROM event WHERE theme = ? AND location = ? AND date = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sqlEvent)) {
                    stmt.setString(1, event.getTheme());
                    stmt.setString(2, event.getLocation());
                    stmt.setString(3, event.getDate().toString());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            eventId = rs.getInt("id");
//                            System.out.println("Event ID: " + eventId);
                        }
                        else {
                            System.out.println("Event not found.");
                        }
                    }

                }
                catch (SQLException e) {
                    e.printStackTrace();
                }


                // query za nalazenje id gosta u listi potvrdjenih za event
                for(Guest guest: allGuests){
                    int guestId = 0;
                    if(event.getConfirmed().contains(guest)){
                        String sqlGuest = "SELECT id FROM guest WHERE name = ? AND email = ? AND number = ?";

                        try (PreparedStatement stmt = connection.prepareStatement(sqlGuest)) {
                            stmt.setString(1, guest.getName());
                            stmt.setString(2, guest.getEmail());
                            stmt.setString(3, guest.getNumber());

                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    guestId = rs.getInt("id");
//                                    System.out.println("Guest ID: " + guestId);
                                }
                                else {
                                    System.out.println("Guest not found.");
                                }
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }



                        // query za dodavanje reda u tabeli confirmed koji se sastoji od eventID i guestID
                        String sql = "INSERT INTO confirmed (event_id, guest_id) VALUES (?, ?)";

                        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                            stmt.setInt(1, eventId);
                            stmt.setInt(2, guestId);

                            int rowsInserted = stmt.executeUpdate();

                            if (rowsInserted > 0) {
//                                System.out.println("Guest with id " + guestId + " has been successfully added for the event with the id " + eventId + " into the table confirmed.");
                            }
                            else {
                                System.out.println("Insert failed. No rows affected.");
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
//        System.out.println("------------------------");
    }

    private static void insertingDeclinedGuests_DB(List<Event> allEvents, List<Guest> allGuests, Connection connection){
        for(Event event: allEvents){
            if(!event.isCanceled()){
                int eventId = 0;

                // query za nalazenje event id u listi
                String sqlEvent = "SELECT id FROM event WHERE theme = ? AND location = ? AND date = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sqlEvent)) {
                    stmt.setString(1, event.getTheme());
                    stmt.setString(2, event.getLocation());
                    stmt.setString(3, event.getDate().toString());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            eventId = rs.getInt("id");
//                            System.out.println("Event ID: " + eventId);
                        }
                        else {
                            System.out.println("Event not found.");
                        }
                    }

                }
                catch (SQLException e) {
                    e.printStackTrace();
                }


                // query za nalazenje id gosta u listi potvrdjenih za event
                for(Guest guest: allGuests){
                    int guestId = 0;
                    if(event.getDeclined().contains(guest)){
                        String sqlGuest = "SELECT id FROM guest WHERE name = ? AND email = ? AND number = ?";

                        try (PreparedStatement stmt = connection.prepareStatement(sqlGuest)) {
                            stmt.setString(1, guest.getName());
                            stmt.setString(2, guest.getEmail());
                            stmt.setString(3, guest.getNumber());

                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    guestId = rs.getInt("id");
//                                    System.out.println("Guest ID: " + guestId);
                                }
                                else {
                                    System.out.println("Guest not found.");
                                }
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }



                        // query za dodavanje reda u tabeli declined koji se sastoji od eventID i guestID
                        String sql = "INSERT INTO declined (event_id, guest_id) VALUES (?, ?)";

                        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                            stmt.setInt(1, eventId);
                            stmt.setInt(2, guestId);

                            int rowsInserted = stmt.executeUpdate();

                            if (rowsInserted > 0) {
//                                System.out.println("Guest with id " + guestId + " has been successfully added for the event with the id " + eventId + " into the table declined.");
                            }
                            else {
                                System.out.println("Insert failed. No rows affected.");
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
//        System.out.println("------------------------");
    }

    private static void insertingNoShownUpGuests_DB(List<Event> allEvents, List<Guest> allGuests, Connection connection){
        for(Event event: allEvents){
            if(!event.isCanceled()){
                int eventId = 0;

                // query za nalazenje event id u listi
                String sqlEvent = "SELECT id FROM event WHERE theme = ? AND location = ? AND date = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sqlEvent)) {
                    stmt.setString(1, event.getTheme());
                    stmt.setString(2, event.getLocation());
                    stmt.setString(3, event.getDate().toString());

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            eventId = rs.getInt("id");
//                            System.out.println("Event ID: " + eventId);
                        }
                        else {
                            System.out.println("Event not found.");
                        }
                    }

                }
                catch (SQLException e) {
                    e.printStackTrace();
                }


                // query za nalazenje id gosta u listi potvrdjenih za event
                for(Guest guest: allGuests){
                    int guestId = 0;
                    if(event.getConfDidntShow().contains(guest)){
                        String sqlGuest = "SELECT id FROM guest WHERE name = ? AND email = ? AND number = ?";

                        try (PreparedStatement stmt = connection.prepareStatement(sqlGuest)) {
                            stmt.setString(1, guest.getName());
                            stmt.setString(2, guest.getEmail());
                            stmt.setString(3, guest.getNumber());

                            try (ResultSet rs = stmt.executeQuery()) {
                                if (rs.next()) {
                                    guestId = rs.getInt("id");
//                                    System.out.println("Guest ID: " + guestId);
                                }
                                else {
                                    System.out.println("Guest not found.");
                                }
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }



                        // query za dodavanje reda u tabeli notshownup koji se sastoji od eventID i guestID
                        String sql = "INSERT INTO notshownup (event_id, guest_id) VALUES (?, ?)";

                        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                            stmt.setInt(1, eventId);
                            stmt.setInt(2, guestId);

                            int rowsInserted = stmt.executeUpdate();

                            if (rowsInserted > 0) {
//                                System.out.println("Guest with id " + guestId + " has been successfully added for the event with the id " + eventId + " into the table notshownup.");
                            }
                            else {
                                System.out.println("Insert failed. No rows affected.");
                            }
                        }
                        catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
//        System.out.println("------------------------");
    }

    private static void insertEventsDB(List<Event> listOfEvents, Connection connection) {
        for(Event event: listOfEvents){
            String sql = "INSERT INTO event (theme, location, date) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, event.getTheme());
                stmt.setString(2, event.getLocation());
                stmt.setString(3, event.getDate().toString());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println(event + " has been added successfully to the database.");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertGuestsDB(List<Guest> allGuests, Connection connection) {
        for(Guest guest: allGuests){
            String sql = "INSERT INTO guest (name, email, number) VALUES (?, ?, ?)";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, guest.getName());
                stmt.setString(2, guest.getEmail());
                stmt.setString(3, guest.getNumber());

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Guest " + guest.getName() + " has been added successfully to the database.");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void themeLoyalty(String searchingTheme, Connection connection) {
        String sql = "SELECT g.id, g.name, g.email, g.number " +
                "FROM guest g " +
                "WHERE g.id IN (" +
                "    SELECT c.guest_id " +
                "    FROM confirmed c " +
                "    JOIN event e ON c.event_id = e.id " +
                "    WHERE e.theme = ? AND e.completed = 1 " +
                "    GROUP BY c.guest_id " +
                "    HAVING COUNT(DISTINCT c.event_id) = (" +
                "        SELECT COUNT(*) FROM event WHERE theme = ? AND completed = 1))" +
                "AND g.id NOT IN (" +
                "    SELECT d.guest_id FROM declined d " +
                "    JOIN event e ON d.event_id = e.id " +
                "    WHERE e.theme = ?)" +
                "AND g.id NOT IN (" +
                "    SELECT n.guest_id FROM notShownUp n " +
                "    JOIN event e ON n.event_id = e.id " +
                "    WHERE e.theme = ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, searchingTheme);
            stmt.setString(2, searchingTheme);
            stmt.setString(3, searchingTheme);
            stmt.setString(4, searchingTheme);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Guests who attended every event with the theme: " + searchingTheme);

                while (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String number = rs.getString("number");

                    System.out.println("Guest: " + name + ", email: " + email + ", number: " + number + ", has attended every event with the theme: " + searchingTheme);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("-----------------------");
    }

    private static void fillEvents(List<Guest> allGuests, Random random, List<Event> listOfEvents, Connection connection) {
        for(Event event: listOfEvents){
            int eventSize = random.nextInt(allGuests.size()) + 1;

            for(int i = 0; i < eventSize; i++){
                boolean found = false;
                Guest tmpGuest = allGuests.get(random.nextInt(allGuests.size()));

                if(event.getInvited().size() == 0){
                    event.addInvited(tmpGuest, connection);
                    continue;
                }

                for(Guest eventGuest: event.getInvited()){
                    if(tmpGuest == eventGuest) {
                        found = true;
                        break;
                    }
                }

                if(!found){
                    event.addInvited(tmpGuest, connection);
                }
            }
        }
        System.out.println();
    }

    private static void eventGuestList(List<Event> listOfEvents, Connection connection) {
        for(Event event: listOfEvents){
            boolean cancelResult = false;
            String sql = "SELECT canceled FROM event WHERE theme = ? AND location = ? AND date = ?";

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, event.getTheme());
                stmt.setString(2, event.getLocation());
                stmt.setString(3, event.getDate().toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                         cancelResult = rs.getBoolean("canceled");
                    }
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

            if(cancelResult){
                System.out.println();
                System.out.println("No guest list for " + event);
                System.out.println("-------------------");
                continue;
            }

            System.out.println("Invited guests for " + event);
            for(Guest guest : event.getInvited()){
                System.out.println(guest);
            }
            System.out.println();

            System.out.println("Guests who confirmed and didn't show up to " + event);
            for(Guest guest: event.getConfDidntShow()){
                System.out.println(guest);
            }
            System.out.println();

            System.out.println("Guests who declined to go to " + event);
            for(Guest guest: event.getDeclined()){
                System.out.println(guest);
            }

            System.out.println("--------------------------");
        }
    }

    private static void eventStatusCheck(List<Event> listOfEvents, Connection connection) {
        LocalDate currentDate = LocalDate.now();
        for(Event event: listOfEvents){
            event.checkStatus(currentDate);

            if(event.isCompleted()){
                String sql = "UPDATE event SET completed = ?, canceled = ?, upcoming = ?" +
                        " WHERE theme = ? AND date = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, 1);
                    stmt.setInt(2, 0);
                    stmt.setInt(3, 0);
                    stmt.setString(4, event.getTheme());
                    stmt.setString(5, event.getDate().toString());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println(event + " status has been updated in the database to completed.");
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(event.isCanceled()){
                String sql = "UPDATE event SET completed = ?, canceled = ?, upcoming = ?" +
                        " WHERE theme = ? AND date = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, 0);
                    stmt.setInt(2, 1);
                    stmt.setInt(3, 0);
                    stmt.setString(4, event.getTheme());
                    stmt.setString(5, event.getDate().toString());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println(event + " status has been updated in the database to canceled.");
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            else if(event.isUpcoming()){
                String sql = "UPDATE event SET completed = ?, canceled = ?, upcoming = ?" +
                        " WHERE theme = ? AND date = ?";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, 0);
                    stmt.setInt(2, 0);
                    stmt.setInt(3, 1);
                    stmt.setString(4, event.getTheme());
                    stmt.setString(5, event.getDate().toString());

                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println(event + " status has been updated in the database to upcoming.");
                    }
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void lowAttendance(Connection connection) {
        String sql = "SELECT theme, location, date FROM event " +
                "WHERE id IN ( " +
                "    SELECT event_id " +
                "    FROM confirmed " +
                "    GROUP BY event_id " +
                "    HAVING COUNT(guest_id) < 5 " +
                ") " +
                "AND upcoming = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Upcoming Events with Less than 5 Confirmed Guests:");

            while (rs.next()) {
                String theme = rs.getString("theme");
                String location = rs.getString("location");
                Date eventDate = rs.getDate("date");

                System.out.println("Theme: " + theme + ", location: " + location + ", date: " + eventDate);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("----------------------------");
    }

    private static void listEvents(Connection connection) {
        System.out.println("All the events:");
        collectAllEvents(connection);
        System.out.println();

        System.out.println("Completed events:");
        collectCompletedEvents(connection);
        System.out.println();

        System.out.println("Upcoming events:");
        collectUpcomingEvents(connection);
        System.out.println();

        System.out.println("Cancelled events:");
        collectCanceledEvents(connection);
        System.out.println("----------------");
    }

    private static void collectAllEvents(Connection connection){
        String sql = "SELECT theme, location, date FROM event";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String theme = rs.getString("theme");
                String location = rs.getString("location");
                Date date = rs.getDate("date");
                System.out.println("Event: " + theme + " | Location: " + location + " | Date: " + date);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void collectCompletedEvents(Connection connection) {
        String sql = "SELECT theme, location, date FROM event WHERE completed = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String theme = rs.getString("theme");
                String location = rs.getString("location");
                Date date = rs.getDate("date");
                System.out.println("Completed Event: " + theme + " | Location: " + location + " | Date: " + date);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void collectCanceledEvents(Connection connection) {
        String sql = "SELECT theme, location, date FROM event WHERE canceled = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String theme = rs.getString("theme");
                String location = rs.getString("location");
                Date date = rs.getDate("date");
                System.out.println("Canceled Event: " + theme + " | Location: " + location + " | Date: " + date);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void collectUpcomingEvents(Connection connection) {
        String sql = "SELECT theme, location, date FROM event WHERE upcoming = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String theme = rs.getString("theme");
                String location = rs.getString("location");
                Date date = rs.getDate("date");
                System.out.println("Upcoming Event: " + theme + " | Location: " + location + " | Date: " + date);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void frequentNoShow(Connection connection) {
        String sql = "SELECT name, confirmed, notShownUp FROM guest WHERE confirmed >= 3 AND notShownUp >= 2";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Guests who have confirmed at least 3 events and didn't show up for at least 2:");

            while (rs.next()) {
                String name = rs.getString("name");
                int confirmed = rs.getInt("confirmed");
                int notShownUp = rs.getInt("notShownUp");

                System.out.println("Name: " + name + " has confirmed " + confirmed + " events and didn't show up to " + notShownUp + " of them");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		finally{
			System.out.println("----------------");
		}
    }

    public static void mostReliable(Connection connection){
        String sql = "SELECT name, attended, confirmed FROM guest " +
                "ORDER BY attended DESC, confirmed DESC LIMIT 5";

//        String sql = "SELECT name, attended, confirmed FROM guest " +
//                "ORDER BY CASE WHEN attended = 0 THEN 1 ELSE 0 END, " +
//                "attended DESC, confirmed DESC " +
//                "LIMIT 5";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Top 5 Guests by Attended and Confirmed:");

            while (rs.next()) {
                String name = rs.getString("name");
                int attended = rs.getInt("attended");
                int confirmed = rs.getInt("confirmed");

                System.out.println(name + " has confirmed " + confirmed + " and attended " + attended + " events");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		finally{
			System.out.println("----------------");
		}
    }

    public static void findByName(String name, Connection connection, List<Event> allEvents){
        boolean found = false;
        for(Event event: allEvents){
            for(Guest guest: event.getInvited()){
                if(guest.getName().equals(name)){
                    found = true;
                    String sql = "SELECT * FROM guest WHERE name = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, name);

                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                String guestName = rs.getString("name");
                                String email = rs.getString("email");
                                String number = rs.getString("number");

                                System.out.println("Name: " + guestName + ", email: " + email + ", number: " + number +
                                        ", was invited to the event: " + event.getTheme() + ", location: " + event.getLocation() +
                                        ", on: " + event.getDate().toString());
                            }
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(!found){
            System.out.println("Guest with the name " + name + " wasn't found on any invitation list");
        }

        System.out.println("----------------");
    }

    public static void findByEmail(String email, Connection connection, List<Event> allEvents){
        boolean found = false;
        for(Event event: allEvents){
            for(Guest guest: event.getInvited()){
                if(guest.getEmail().equals(email)){
                    found = true;
                    String sql = "SELECT * FROM guest WHERE email = ?";

                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setString(1, email);

                        try (ResultSet rs = stmt.executeQuery()) {
                            if (rs.next()) {
                                String name = rs.getString("name");
                                String guestEmail = rs.getString("email");
                                String number = rs.getString("number");

                                System.out.println("Name: " + name + ", email: " + guestEmail + ", number: " + number +
                                        ", was invited to the event: " + event.getTheme() + ", location: " + event.getLocation() +
                                        ", on: " + event.getDate().toString());
                            }
                        }
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(!found){
            System.out.println("Guest with the email " + email + " wasn't found on any invitation list");
        }

        System.out.println("----------------");
    }

    public static void allGuestInfo(Connection connection){
        String sql = "SELECT name, attended, confirmed, notShownUp, declined FROM guest";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String name = rs.getString("name");
                int attended = rs.getInt("attended");
                int confirmed = rs.getInt("confirmed");
                int notShownUp = rs.getInt("notShownUp");
                int declined = rs.getInt("declined");

                System.out.println("Guest " + name + " has confirmed " + confirmed +
                        " events, has actually attended " + attended + " events, has not shown up for "
                        + notShownUp + " events, and declined " + declined + " events");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
		finally{
			System.out.println("----------------");
		}
    }
}