import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class LibraryBibtexParser {

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

    private static String convertItemToBibtexEntry(Item item) {
        return String.format("@article{%s,\n  author = {%s},\n  title = {%s},\n  year = {%s},\n}",
                item.getId(),
                formatAuthors(item.getAuthorFirst(), item.getAuthorLast()), 
                item.getName(),
                item.getDatePublished() == null ? "N/A" : item.getDatePublished().getYear() // ya idfk how to do this
                // item.getJournalName()
        );
    }

    private static String formatAuthors(String authorFirst, String authorLast) {
        return authorLast + ", " + authorFirst;
    }
}
