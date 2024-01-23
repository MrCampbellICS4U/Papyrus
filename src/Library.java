import java.util.TreeSet;
import java.util.Comparator;
import java.util.Set;
import java.util.Date;

public class Library extends TreeSet<Item> {

    // class attributes
    public Set<Tags> tags;
    public String name = "Library1";
    public String path = "Library1.ppxml";
    public Date dateCreated = new Date();
    public Date dateModified = new Date();
    private Item selectedItem = null;

    public String description = "This is a library";

    /**
     * gets the description of the library
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * sets the description of the library
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * sets the name of the library
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * sets the path of the library
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * gets the path of the library
     * @return path
     */
    public String getPath() {
        return this.path;
    }

    /**
     * gets the date the library was created
     * @return dateCreated
     */
    public Date getDateCreated() {
        return this.dateCreated;
    }

    /**
     * gets the date the library was modified
     * @return dateModified
     */
    public Date getDateModified() {
        return this.dateModified;
    }

    /**
     * Constructor for Library
     */
    public Library(String name, LibraryComparator comparator) {
        super(comparator);
        this.name = name;
    }

    /**
     * Constructor for Library
     */
    public Library(String name, Library other, LibraryComparator comparator) {
        super(comparator);
        this.name = name;
        for (Item item : other) {
            this.add(item);
        }
    }

    /**
     * sorting method for library items
     * @param comparator
     * @return sortedLibrary
     */
    public Library sort(LibraryComparator.Type comparator) {
        Library sortedLibrary = new Library(this.name, this, new LibraryComparator(comparator));
        for (Item item : this) {
            sortedLibrary.add(item);
        }
        return sortedLibrary;
    }

    /**
     * removes an item from the library
     * @param item
     * @return true if item is removed, false if item is not in library
     */
    @Override
    public boolean remove(Object item) {
        return super.remove(item);
    }

    /**
     * gets the name of the library
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * gets the items in the library
     * @return items
     */
    public TreeSet<Item> getItems() {
        return this;
    }

    /**
     * gets the selected item in the library
     * @return selectedItem
     */
    public Item getSelectedItem() {
        return this.selectedItem;
    }

    /**
     * sets the selected item in the library
     * @param item
     */
    public void setSelectedItem(Item item) {
        for (Item i : this) {
            if (i == item) {
                this.selectedItem = item;
                return;
            }
        }
    }

    /**
     * edits an item in the library
     * @param item
     * @param newItem
     * @return
     */
    public Item editItem(Item item, Item newItem) {
        this.remove(item);
        this.add(newItem);
        return newItem;
    }

    /**
     * gets an item in the library
     * @param name
     * @return item
     */
    public Item getItem(String name) {
        for (Item item : this) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * checks if an item is in the library
     * @param name
     * @return true if item is in library, false if item is not in library
     */
    public boolean hasItem(String name) {
        return this.getItem(name) != null;
    }

    // public boolean hasItem(Item item) {
    //     return this.hasItem(item.getName());
    // }

    // public Item getItem(int id) {
    //     for (Item item : this) {
    //         if (item.getId() == id) {
    //             return item;
    //         }
    //     }
    //     return null;
    // }    
        
    /**
     * gets the tags in the library
     * @return tags
     */
    @Override
    public String toString() {
        String str = "";
        for (Item item : this) {
            str += item.toString() + "\n";
        }
        return str;
    }
   
}

/**
 * Comparator for Library
 */
class LibraryComparator implements Comparator<Item> {

    // Type enum
    public static enum Type {
        NAME,
        ID,
        DATEPUBLISHED,
        DATEADDED,
        AUTHORFIRST,
        AUTHORLAST, 
        DOISTRING,
        URLSTRING,
        ISBNSTRING,   
    }    

    private Type sortBy;

    /**
     * Constructor for LibraryComparator
     * @param sortBy
     */
    public LibraryComparator(Type sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * compares two items in the library
     * @param item1
     * @param item2
     * @return 0 if items are equal, -1 if item1 is less than item2, 1 if item1 is greater than item2
     */
    @Override
    public int compare(Item item1, Item item2) {
        if (item1 == null || item2 == null) {
            return (item1 == null) ? ((item2 == null) ? 0 : -1) : 1;
        }

        switch (this.sortBy) {
            case NAME:
                int itemcomparison = item1.getName().compareTo(item2.getName());
                if (itemcomparison == 0) {
                    return item1.getId() > item2.getId() ? 1 : -1;
                }
                return itemcomparison;
            case ID:
                return item1.getId() > item2.getId() ? 1 : -1;

            case DATEPUBLISHED:
                return item1.getDatePublished().compareTo(item2.getDatePublished());
                
            case DATEADDED:
                return item1.getDateAdded().compareTo(item2.getDateAdded());

            case AUTHORFIRST:
                return item1.getAuthorFirst().compareTo(item2.getAuthorFirst());
            
            case AUTHORLAST:
                return item1.getAuthorLast().compareTo(item2.getAuthorLast());

            case DOISTRING:
                return item1.getDoiString().compareTo(item2.getDoiString());
            
            case URLSTRING:
                return item1.getUrlString().compareTo(item2.getUrlString());

            case ISBNSTRING:
                return item1.getIsbnString().compareTo(item2.getIsbnString());

            default:
                return 0;
        }
    }
}
