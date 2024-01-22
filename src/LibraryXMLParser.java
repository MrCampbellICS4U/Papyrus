import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

class LibraryXMLParser {

    public static void writeLibrary(Library library) {
        String xmlString = "<Library>\n";
        xmlString += "\t<Name>" + library.getName() + "</Name>\n";
        xmlString += "\t<Description>" + library.getDescription() + "</Description>\n";
        xmlString += "\t<DateCreated>" + library.getDateCreated() + "</DateCreated>\n";
        xmlString += "\t<DateModified>" + library.getDateModified() + "</DateModified>\n";
        for (Item item : library) {
            xmlString += "\t<Item>\n";
            xmlString += "\t\t<Name>" + item.getName() + "</Name>\n";
            xmlString += "\t\t<DatePublished>" + item.getDatePublished() + "</DatePublished>\n";
            xmlString += "\t\t<DateAdded>" + item.getDateAdded() + "</DateAdded>\n";
            xmlString += "\t\t<AuthorFirst>" + item.getAuthorFirst() + "</AuthorFirst>\n";
            xmlString += "\t\t<AuthorLast>" + item.getAuthorLast() + "</AuthorLast>\n";
            xmlString += "\t\t<DoiString>" + item.getDoiString() + "</DoiString>\n";
            xmlString += "\t\t<UrlString>" + item.getUrlString() + "</UrlString>\n";
            xmlString += "\t\t<IsbnString>" + item.getIsbnString() + "</IsbnString>\n";
            xmlString += "\t</Item>\n";
        }
        xmlString += "</Library>\n";
        try {
            FileWriter writer = new FileWriter(library.getPath());
            System.out.println("Writing to file: " + library.getPath());
            BufferedWriter bWriter = new BufferedWriter(writer);
            bWriter.write(xmlString);
            bWriter.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static Library parseLibrary(String filePath, LibraryComparator comparator) {
        try {
            File xmlFile = new File(filePath);
            String libraryName = xmlFile.getName();
            libraryName = libraryName.substring(0, libraryName.lastIndexOf('.'));

            Library library = new Library(libraryName, comparator);

            library.setPath(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            String description = extractLibraryDescription(doc);

            NodeList nodeList = doc.getElementsByTagName("Item");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element element = (Element) nodeList.item(i);
                Item item = parseItem(element);
                library.add(item);
            }

            System.out.println("Library Description: " + description);

            return library;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String extractLibraryDescription(Document doc) {
        NodeList descList = doc.getElementsByTagName("Description");
        if (descList.getLength() > 0) {
            return descList.item(0).getTextContent();
        }
        return "";
    }

    private static Item parseItem(Element element) {
        String name = element.getElementsByTagName("Name").item(0).getTextContent();
        Date datePublished = parseDate(element.getElementsByTagName("DatePublished").item(0).getTextContent());
        Date dateAdded = parseDate(element.getElementsByTagName("DateAdded").item(0).getTextContent());
        String authorFirst = element.getElementsByTagName("AuthorFirst").item(0).getTextContent();
        String authorLast = element.getElementsByTagName("AuthorLast").item(0).getTextContent();
        String doiString = element.getElementsByTagName("DoiString").item(0).getTextContent();
        String urlString = element.getElementsByTagName("UrlString").item(0).getTextContent();
        String isbnString = element.getElementsByTagName("IsbnString").item(0).getTextContent();

        return new Item(name, datePublished, dateAdded, authorFirst, authorLast, doiString, urlString, isbnString);
    }


    private static Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}
