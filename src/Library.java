import java.util.TreeSet;
import java.util.Comparator;
import java.util.Set;
import java.util.Date;

public class Library extends TreeSet<Item> {
    public Set<Tags> tags;
    public String name;
    public Date dateCreated;
    public Date dateModified;

    public Library(String name, LibraryComparator comparator) {
        super(comparator);
        this.name = name;
    }

    public Library(String name, Library other, LibraryComparator comparator) {
        super(comparator);
        this.name = name;
        for (Item item : other) {
            this.add(item);
        }
    }

    @Override 
    public boolean add(Item item) {
        return super.add(item);
    }

    @Override
    public boolean remove(Object item) {
        return super.remove(item);
    }

    public Item editItem(Item item, Item newItem) {
        this.remove(item);
        this.add(newItem);
        return newItem;
    }

    public Item getItem(String name) {
        for (Item item : this) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }

    public Item getItem(Item item) {
        return this.getItem(item.getName());
    }

    public boolean hasItem(String name) {
        return this.getItem(name) != null;
    }

    public boolean hasItem(Item item) {
        return this.hasItem(item.getName());
    }

    public Item getItem(int id) {
        for (Item item : this) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }    
    
    @Override
    public String toString() {
        String str = "";
        for (Item item : this) {
            str += item.toString() + "\n";
        }
        return str;
    }
}

class LibraryComparator implements Comparator<Item> {

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

    public LibraryComparator(Type sortBy) {
        this.sortBy = sortBy;
    }

    @Override
    public int compare(Item item1, Item item2) {
        switch (this.sortBy) {
            case NAME:
                return item1.getName().compareTo(item2.getName());
            case ID:
                return item1.getId() - item2.getId();
            default:
                return 0;
        }
    }
}