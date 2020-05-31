package shop;

import java.util.*;

public class Basket {

//    Collection<Product> items;
    private HashMap<String, Integer> items;
    private ShopDB db;

    public static void main(String[] args) {
        Basket b = new Basket();
        b.addItem("art1");
        System.out.println( b.getTotalString() );
        b.clearBasket();
        System.out.println( b.getTotalString() );
        // check that adding a null String causes no problems
        String pid = null;
        b.addItem(pid);
        System.out.println( b.getTotalString() );
    }

    public Basket() {
        db = ShopDB.getSingleton();
//        items = new ArrayList<Product>();
        items = new HashMap<>();
    }

    /**
     *
     * @return Collection of Product items that are stored in the basket
     *
     * Each item is a product object - need to be clear about that...
     *
     * When we come to list the Basket contents, it will be much more
     * convenient to have all the product details as items in this way
     * in order to calculate that product totals etc.
     *
     */
    public HashMap<String, Integer> getItems() {
        return items;
    }

    /**
     * empty the basket - the basket should contain no items after calling this method
     */
    public void clearBasket() {
        items.clear();
    }

    /**
     *
     *  Adds an item specified by its product code to the shopping basket
     *
     * @param pid - the product code
     */
    public void addItem(String pid) {
        if (pid != null) {
            items.put(pid, 1);
        }
    }

    public void updateItem(String pid, int quantity) {
        items.put(pid, quantity);
    }

    public void removeItem(String pid) {
        items.remove(pid);
    }

    /**
     *
     * @return the total value of items in the basket in pence
     */
    public int getTotal() {
        // iterate over the set of products...
        int sum = 0;
        for (Map.Entry<String, Integer> item : items.entrySet()) {
            Product p = db.getProduct(item.getKey());
            sum += p.price * item.getValue();
        }
        // return the total
        return sum;
    }

    /**
     *
     * @return the total value of items in the basket as
     * a pounds and pence String with two decimal places - hence
     * suitable for inclusion as a total in a web page
     */
    public String getTotalString() {
        double total = getTotal() / 100.00;
		return String.format("%.2f", total);
    }
}
