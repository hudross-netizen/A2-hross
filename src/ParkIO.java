import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Saves a park to a text file and loads it back again.
 * All knowledge of the file format lives here, so the model classes
 * know nothing about commas, tags or line order - swapping the format
 * would change this class alone.
 *
 * Format: one record per line, comma separated, first field a tag.
 *   STAFF,id,name,age,position
 *   VISITOR,id,name,age,ticketType
 *   ATTRACTION,kind,id,name,operatorId,maxPerCycle,extraValue,cyclesRun
 *   QUEUE,attractionId,visitorId;visitorId;...
 *   HISTORY,attractionId,visitorId;visitorId;...
 * Staff and visitors are written once and referred to by id.
 * Groups are written in dependency order so every reference in a later
 * line has already been rebuilt when that line is read.
 * Names and positions are assumed to contain no commas or semicolons.
 */
public class ParkIO {

    /**
     * Writes the whole park to a file: its attractions, their operators,
     * everyone still waiting, and everyone already served.
     * @param park the park to back up
     * @param file the destination file
     * @throws ParkStorageException if the file cannot be written
     */
    public static void save(ThemePark park, File file) throws ParkStorageException {
        ArrayList<Attraction> all = park.getAllAttractions();
        Map<Integer, Staff> staffById = new HashMap<Integer, Staff>();
        Map<Integer, Visitor> visitorById = new HashMap<Integer, Visitor>();

        for (int i = 0; i < all.size(); i++) {
            Attraction attraction = all.get(i);
            if (attraction.getOperator() != null) {
                staffById.put(attraction.getOperator().getId(), attraction.getOperator());
            }
            ArrayList<Visitor> waiting = attraction.getWaitingVisitors();
            for (int j = 0; j < waiting.size(); j++) {
                visitorById.put(waiting.get(j).getId(), waiting.get(j));
            }
            ArrayList<Visitor> history = attraction.getVisitHistory();
            for (int j = 0; j < history.size(); j++) {
                visitorById.put(history.get(j).getId(), history.get(j));
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            Iterator<Integer> staffKeys = staffById.keySet().iterator();
            while (staffKeys.hasNext()) {
                Staff member = staffById.get(staffKeys.next());
                writer.write("STAFF," + member.getId() + "," + member.getName() + ","
                        + member.getAge() + "," + member.getPosition());
                writer.newLine();
            }

            Iterator<Integer> visitorKeys = visitorById.keySet().iterator();
            while (visitorKeys.hasNext()) {
                Visitor visitor = visitorById.get(visitorKeys.next());
                writer.write("VISITOR," + visitor.getId() + "," + visitor.getName() + ","
                        + visitor.getAge() + "," + visitor.getTicketType());
                writer.newLine();
            }

            for (int i = 0; i < all.size(); i++) {
                writer.write(attractionLine(all.get(i)));
                writer.newLine();
            }

            for (int i = 0; i < all.size(); i++) {
                Attraction attraction = all.get(i);
                writer.write("QUEUE," + attraction.getId() + ","
                        + idList(attraction.getWaitingVisitors()));
                writer.newLine();
                writer.write("HISTORY," + attraction.getId() + ","
                        + idList(attraction.getVisitHistory()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ParkStorageException("Could not write the backup to "
                    + file.getName() + ": " + e.getMessage());
        }

        System.out.println("Backup saved: " + all.size() + " attractions, "
                + staffById.size() + " staff and " + visitorById.size()
                + " visitors written to " + file.getName() + ".");
    }

    /**
     * Builds the ATTRACTION line for one attraction, including the one
     * extra value that its kind carries.
     * @param attraction the attraction to describe
     * @return the finished line of text
     */
    private static String attractionLine(Attraction attraction) {
        String kind = "UNKNOWN";
        int extra = 0;
        if (attraction instanceof Ride) {
            kind = "RIDE";
            extra = ((Ride) attraction).getMinHeightCm();
        } else if (attraction instanceof Show) {
            kind = "SHOW";
            extra = ((Show) attraction).getDurationMinutes();
        }
        String operatorId = "NONE";
        if (attraction.getOperator() != null) {
            operatorId = "" + attraction.getOperator().getId();
        }
        return "ATTRACTION," + kind + "," + attraction.getId() + "," + attraction.getName()
                + "," + operatorId + "," + attraction.getMaxPerCycle() + "," + extra
                + "," + attraction.getCyclesRun();
    }

    /**
     * Joins visitor ids with semicolons so a whole list fits inside a
     * single comma separated field.
     * @param visitors the visitors to list
     * @return the ids separated by semicolons, or an empty string
     */
    private static String idList(ArrayList<Visitor> visitors) {
        String result = "";
        for (int i = 0; i < visitors.size(); i++) {
            result = result + visitors.get(i).getId();
            if (i < visitors.size() - 1) {
                result = result + ";";
            }
        }
        return result;
    }

    /**
     * Reads a park back from a file. A line that cannot be understood is
     * reported and skipped so that one bad record does not cost the rest
     * of the park's data; problems with the file itself are thrown.
     * @param file the backup file to read
     * @param parkName the name to give the restored park
     * @return the restored park
     * @throws ParkStorageException if the file is missing, unreadable,
     *         or fails part way through reading
     */
    public static ThemePark load(File file, String parkName) throws ParkStorageException {
        if (!file.exists()) {
            throw new ParkStorageException("Backup file " + file.getName() + " does not exist.");
        }
        if (!file.canRead()) {
            throw new ParkStorageException("Backup file " + file.getName() + " cannot be read.");
        }

        ThemePark park = new ThemePark(parkName);
        Map<Integer, Staff> staffById = new HashMap<Integer, Staff>();
        Map<Integer, Visitor> visitorById = new HashMap<Integer, Visitor>();
        ArrayList<String> problems = new ArrayList<String>();
        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                lineNumber++;
                try {
                    readRecord(line, park, staffById, visitorById);
                } catch (RuntimeException e) {
                    problems.add("  line " + lineNumber + " skipped - " + e.getMessage());
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new ParkStorageException("Failed while reading "
                    + file.getName() + ": " + e.getMessage());
        }

        System.out.println("Restore complete: " + park.getAttractionCount()
                + " attractions loaded from " + file.getName() + ".");
        if (!problems.isEmpty()) {
            System.out.println("WARNING: " + problems.size() + " line(s) could not be read:");
            for (int i = 0; i < problems.size(); i++) {
                System.out.println(problems.get(i));
            }
        }
        return park;
    }

    /**
     * Rebuilds one record from a line of the file.
     * @param line the raw line
     * @param park the park being rebuilt
     * @param staffById staff rebuilt so far, keyed by id
     * @param visitorById visitors rebuilt so far, keyed by id
     */
    private static void readRecord(String line, ThemePark park,
            Map<Integer, Staff> staffById, Map<Integer, Visitor> visitorById) {
        if (line.trim().isEmpty()) {
            return;
        }
        String[] parts = line.split(",", -1);
        String tag = parts[0];

        if (tag.equals("STAFF")) {
            requireFields(parts, 5, "STAFF");
            int id = Integer.parseInt(parts[1]);
            staffById.put(id, new Staff(id, parts[2], Integer.parseInt(parts[3]), parts[4]));

        } else if (tag.equals("VISITOR")) {
            requireFields(parts, 5, "VISITOR");
            int id = Integer.parseInt(parts[1]);
            visitorById.put(id, new Visitor(id, parts[2], Integer.parseInt(parts[3]), parts[4]));

        } else if (tag.equals("ATTRACTION")) {
            requireFields(parts, 8, "ATTRACTION");
            Staff operator = null;
            if (!parts[4].equals("NONE")) {
                operator = staffById.get(Integer.parseInt(parts[4]));
                if (operator == null) {
                    throw new IllegalArgumentException("no staff member with id " + parts[4]);
                }
            }
            int maxPerCycle = Integer.parseInt(parts[5]);
            int extra = Integer.parseInt(parts[6]);
            int cycles = Integer.parseInt(parts[7]);
            Attraction attraction;
            if (parts[1].equals("RIDE")) {
                attraction = new Ride(parts[2], parts[3], operator, maxPerCycle, extra);
            } else if (parts[1].equals("SHOW")) {
                attraction = new Show(parts[2], parts[3], operator, maxPerCycle, extra);
            } else {
                throw new IllegalArgumentException("unknown attraction kind " + parts[1]);
            }
            attraction.restoreCycles(cycles);
            park.registerAttraction(attraction);

        } else if (tag.equals("QUEUE") || tag.equals("HISTORY")) {
            requireFields(parts, 3, tag);
            Attraction attraction = park.getAttraction(parts[1]);
            if (attraction == null) {
                throw new IllegalArgumentException("no attraction with id " + parts[1]);
            }
            if (parts[2].isEmpty()) {
                return;
            }
            String[] ids = parts[2].split(";", -1);
            for (int i = 0; i < ids.length; i++) {
                Visitor visitor = visitorById.get(Integer.parseInt(ids[i]));
                if (visitor == null) {
                    throw new IllegalArgumentException("no visitor with id " + ids[i]);
                }
                if (tag.equals("QUEUE")) {
                    attraction.restoreWaitingVisitor(visitor);
                } else {
                    attraction.restoreVisit(visitor);
                }
            }

        } else {
            throw new IllegalArgumentException("unknown record tag " + tag);
        }
    }

    /**
     * Checks that a line has the number of fields its tag requires.
     * @param parts the split line
     * @param expected how many fields the record needs
     * @param tag the record type, for the message
     */
    private static void requireFields(String[] parts, int expected, String tag) {
        if (parts.length != expected) {
            throw new IllegalArgumentException("a " + tag + " record needs " + expected
                    + " fields but had " + parts.length);
        }
    }

    /**
     * Writes the given lines to a file, used here to create a
     * deliberately corrupt file for the demonstration.
     * @param file the file to write
     * @param lines the lines to write
     * @throws ParkStorageException if the file cannot be written
     */
    public static void writeLines(File file, ArrayList<String> lines) throws ParkStorageException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ParkStorageException("Could not write " + file.getName() + ": " + e.getMessage());
        }
    }
}