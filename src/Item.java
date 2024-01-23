import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.UUID;

public class Item {

    // class attributes
    public ArrayList<String> tagList;
    private String name;
    private int id = UUID.randomUUID().hashCode();
    private Date datePublished;
    private Date dateAdded;
    private String authorFirst;
    private String authorLast;
    private String doiString;
    private String urlString;
    private String isbnString;

    /**
     * Constructor for Item
     * @param name
     * @param datePublished
     * @param dateAdded
     * @param authorFirst
     * @param authorLast
     * @param doiString
     * @param urlString
     * @param isbnString
     */
    public Item(String name, Date datePublished, Date dateAdded, String authorFirst, String authorLast, String doiString, String urlString, String isbnString) {
        this.name = name;
        this.datePublished = datePublished;
        this.dateAdded = dateAdded;
        this.authorFirst = authorFirst;
        this.authorLast = authorLast;
        this.doiString = doiString;
        this.urlString = urlString;
        this.isbnString = isbnString;
    }

    /**
     * Copy constructor for Item
     * @param other
     */
    public Item(Item other) {
        this.name = other.name;
        this.datePublished = other.datePublished;
        this.dateAdded = other.dateAdded;
        this.authorFirst = other.authorFirst;
        this.authorLast = other.authorLast;
        this.doiString = other.doiString;
        this.urlString = other.urlString;
        this.isbnString = other.isbnString;
        this.tagList = other.tagList;
    }

    /**
     * Default constructor for Item
     */
    public Item() {
        this.name = "";
        this.datePublished = null;
        this.dateAdded = null;
        this.authorFirst = "";
        this.authorLast = "";
        this.doiString = "";
        this.urlString = "";
        this.isbnString = "";
        this.tagList = new ArrayList<String>();
    }

    /**
     * Constructor for Item
     * @param name
     */
    public Item(String name) {
        this.name = name;
        this.datePublished = null;
        this.dateAdded = null;
        this.authorFirst = "";
        this.authorLast = "";
        this.doiString = "";
        this.urlString = "";
        this.isbnString = "";
        this.tagList = new ArrayList<String>();
    }

    // Getter and setter methods for all the fields.

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    public Date getDatePublished() {
        return this.datePublished;
    }

    public Date getDateAdded() {
        return this.dateAdded;
    }

    public String getAuthorFirst() {
        return this.authorFirst;
    }

    public String getAuthorLast() {
        return this.authorLast;
    }

    public String getDoiString() {
        return this.doiString;
    }

    public String getUrlString() {
        return this.urlString;
    }

    public String getIsbnString() {
        return this.isbnString;
    }

    public String setName(String name) {
        return name = this.name;
    }

    public int setId(int id) {
        return id = this.id;
    }

    public Date setDatePublished(Date datePublished) {
        return datePublished = this.datePublished;
    }

    public Date setDateAdded(Date setDateAdded) {
        return setDateAdded = this.dateAdded;
    }

    public String setAuthorFirst(String authorString) {
        return authorString = this.authorFirst;
    }

    public String setAuthorLast(String setAuthorLast) {
        return setAuthorLast = this.authorLast;
    }

    public String setDoiString(String setDoiString) {
        return setDoiString = this.doiString;
    }

    public String setUrlString(String setUrlString) {
        return setUrlString = this.urlString;
    }

    public String setIsbnString(String setIsbnString) {
        return setIsbnString = this.isbnString;
    }

    public ArrayList<String> getTags() {
        return tagList;
    }

    public void addTags(String tag) {
        this.tagList.add(tag); 

    }
    
    public void deleteTags(int i) {
        this.tagList.remove(i);
    }

    public static Item copy(Item other) {
        return new Item(other);
    }

    /**
     * Sets the info of the item based on the compare type
     * @param compareType
     * @param info
     */
    public void setInfo(LibraryComparator.Type compareType, String info) {
        switch (compareType) {
            case NAME:
                this.name = info;
                break;
            case ID: 
                this.id = Integer.parseInt(info);
                break;
            case DATEPUBLISHED:
                this.datePublished = new Date(info);
                break;
            case DATEADDED:
                this.dateAdded = new Date(info);
                break;
            case AUTHORFIRST:
                this.authorFirst = info;
                break;
            case AUTHORLAST: 
                this.authorLast = info;
                break;
            case DOISTRING:
                this.doiString = info;
                break;
            case URLSTRING:
                this.urlString = info;
                break;
            case ISBNSTRING:
                this.isbnString = info;
                break;
        }
    }

    /**
     * Gets the comparator type name
     * @param compareType
     * @return
     */
    public String getComparatorTypeName(LibraryComparator.Type compareType) {
        switch (compareType) {
            case NAME:
                return "Name";
            case ID: 
                return null;
            case DATEPUBLISHED:
                if (this.datePublished == null) {
                    return "";
                }
                return "Date Published";
            case DATEADDED:
                if (this.dateAdded == null) {
                    return "";
                }
                return "Date Added";
            case AUTHORFIRST:
                return "Author First";
            case AUTHORLAST:
                return "Author Last";
            case DOISTRING:
                if (this.doiString == null) {
                    return "";
                }
                return "DOI";
            case URLSTRING:
                if (this.urlString == null) {
                    return "";
                }
                return "URL";
            case ISBNSTRING:
                if (this.isbnString == null) {
                    return "";
                }
                return "ISBN";
            default:
                return "";
        }
    }

    /**
     * Gets the info of the item based on the compare type
     * @param compareType
     * @return
     */
    public String getInfo(LibraryComparator.Type compareType) {
        switch (compareType) {
            case NAME:
                return this.name;
            case ID: 
                return String.valueOf(this.id);
            case DATEPUBLISHED:
                return String.valueOf(this.datePublished);
            case DATEADDED:
                return String.valueOf(this.dateAdded);
            case AUTHORFIRST:
                return this.authorFirst;
            case AUTHORLAST: 
                return this.authorLast;
            case DOISTRING:
                return this.doiString;
            case URLSTRING:
                return this.urlString;
            case ISBNSTRING:
                return this.isbnString;
        }
        return "";
    }

}