import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class LibraryBibtexParser {

    /**
     * Writes a library to a Bibtex file
     * @param library
     * @param filePath
     */
    public static void writeLibrary(Library library, String filePath) {
        StringBuilder bibtexContent = new StringBuilder();

        for (Item item : library) {
            bibtexContent.append(convertItemToBibtexEntry(item));
            bibtexContent.append("\n\n");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath ));
            writer.write(bibtexContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Converts an item to a Bibtex entry
     * @param item
     * @return
     */
    private static String convertItemToBibtexEntry(Item item) {
        return String.format("@article{%s,\n  author = {%s},\n  title = {%s},\n  year = {%s},\n}",
                item.getId(),
                formatAuthors(item.getAuthorFirst(), item.getAuthorLast()), 
                item.getName(),
                item.getDatePublished() == null ? "N/A" : item.getDatePublished().getYear()
                // item.getJournalName()
        );
    }

    /**
     * Parses a Bibtex file into a Library object
     * @param String filePath
     * @param LibraryComparator.Type
     * @return Library
     */
    public static Library parseLibrary(String filePath, LibraryComparator.Type type) {
        Library library = new Library("Bibtex Library", new LibraryComparator(type));
        StringBuilder entryBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("@")) {
                    if (entryBuilder.length() > 0) {
                        Item item = parseBibtexEntry(entryBuilder.toString());
                        library.add(item);
                        entryBuilder = new StringBuilder();
                    }
                }
                entryBuilder.append(line).append("\n");
            }
            if (entryBuilder.length() > 0) { // Add last entry
                Item item = parseBibtexEntry(entryBuilder.toString());
                library.add(item);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return library;
    }
    

    private static Item parseBibtexEntry(String entry) {
        HashMap<String, String> fields = new HashMap<>();
        String[] lines = entry.split("\n");
        for (String line : lines) {
            if (line.contains("=")) {
                String[] parts = line.split("=", 2);
                String key = parts[0].trim();
                String value = parts[1].trim().replaceAll("[{}]", ""); // Remove braces
                fields.put(key, value);
            }
        }
        // Extracting fields and creating Item object
        String name = fields.getOrDefault("title", "");
        String datePublished = fields.getOrDefault("year", "");
        String dateAdded = ""; // Current date or some default value
        String authorFirst = ""; // Split authors to get first name
        String authorLast = ""; // Split authors to get last name
        String doiString = fields.getOrDefault("doi", "");
        String urlString = fields.getOrDefault("url", "");
        String isbnString = fields.getOrDefault("isbn", "");
        return new Item(name, datePublished, dateAdded, authorFirst, authorLast, doiString, urlString, isbnString);
    }
    

    /**
     * Formats the author names for Bibtex
     * @param authorFirst
     * @param authorLast
     * @return
     */
    private static String formatAuthors(String authorFirst, String authorLast) {
        return authorLast + ", " + authorFirst;
    }
}
