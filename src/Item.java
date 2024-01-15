import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;

public class Item {

    public ArrayList<String> tags;
    
    private String name;
    private int id = 100000;
    private Date datePublished;
    private Date dateAdded;
    private String authorFirst;
    private String authorLast;
    private String doiString;
    private String urlString;
    private String isbnString;

    LibraryComparator.Type compareType;

    //public HashMap<Item, LibraryComparator.Type> compareMap;


    public Item(String name, Date datePublished, Date dateAdded, String authorFirst, String authorLast, String doiString, String urlString, String isbnString) {
        this.name = name;
        this.datePublished = datePublished;
        this.dateAdded = dateAdded;
        this.authorFirst = authorFirst;
        this.authorLast = authorLast;
        this.doiString = doiString;
        this.urlString = urlString;
        this.isbnString = isbnString;
        this.tags = new ArrayList<String>();

    }

    public Item(Item other) {
        this.name = other.name;
        this.datePublished = other.datePublished;
        this.dateAdded = other.dateAdded;
        this.authorFirst = other.authorFirst;
        this.authorLast = other.authorLast;
        this.doiString = other.doiString;
        this.urlString = other.urlString;
        this.isbnString = other.isbnString;
        this.tags = other.tags;
    }

    public String getInfo() {
        switch (compareType) {
            case :
                return this.name;
            
        }
        return this.name;
    }

}
