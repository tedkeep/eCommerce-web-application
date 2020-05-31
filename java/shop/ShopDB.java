
package shop;

import java.sql.*;
import java.util.*;

public class ShopDB {

    Connection con;
    static int nOrders = 0;
    static ShopDB singleton;

    public static void main(String[] args) throws Exception  {
        // simple method to test that ShopDB works
        System.out.println("Got this far...");
        ShopDB db = new ShopDB();
        System.out.println("created shop db");
        shop.Basket basket = new shop.Basket();
        System.out.println("created the basket");

        System.out.println("Testing getAllProducts");
        Collection c = db.getAllProducts();
        for (Iterator i = c.iterator(); i.hasNext() ; ) {
            Product p = (Product) i.next();
            System.out.println( p );
        }
        System.out.println("Testing getProduct(pid)");
        Product product = db.getProduct("art1");
        System.out.println( product );

        System.out.println("Testing order: ");
        basket.addItem( product.PID);
        System.out.println("added an item");
        db.order( basket , "Simon" );
        System.out.println("order done");

    }

    public ShopDB() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            System.out.println("loaded class");
            con = DriverManager.getConnection("jdbc:hsqldb:file:\\tomcat\\webapps\\ass2\\shopdb", "sa", "");
            System.out.println("created con");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    public static ShopDB getSingleton() {
        if (singleton == null) {
            singleton = new ShopDB();
        }
        return singleton;
    }

    public ResultSet getProducts() {
        try {
            Statement s = con.createStatement();
            System.out.println("Created statement");
            ResultSet rs = s.executeQuery("Select * from Product");
            System.out.println("Returning result set...");
            return rs;
        }
        catch(Exception e) {
            System.out.println( "Exception in getProducts(): " + e );
            return null;
        }
    }

    public Collection<Product> getProductsByArtist(String artist) {
        String query = "SELECT * FROM Product WHERE LOWER(Artist)= ?";
        LinkedList<Product> list = new LinkedList<Product>();
        artist = artist.toLowerCase();
        try {
            Statement s = con.prepareStatement(query);
            ((PreparedStatement) s).setString(1, artist);
            ResultSet rs = ((PreparedStatement) s).executeQuery();
            list = addProducts(rs,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private LinkedList<Product> addProducts(ResultSet rs, LinkedList<Product> list) throws Exception{
        while (rs.next()) {
            Product product = new Product(
                    rs.getString("PID"),
                    rs.getString("Artist"),
                    rs.getString("Title"),
                    rs.getString("Description"),
                    rs.getInt("price"),
                    rs.getString("thumbnail"),
                    rs.getString("fullimage")
            );
            list.add(product);
        }
        return list;
    }

    public Collection<Product> getProductsByTitle(String title) {
        String query = "SELECT * FROM Product WHERE LOWER(Title) LIKE ?";
        title = "%" + title + "%";
        LinkedList<Product> list = new LinkedList<Product>();
        title = title.toLowerCase();
        try {
            Statement s = con.prepareStatement(query);
            ((PreparedStatement) s).setString(1, title);
            ResultSet rs = ((PreparedStatement) s).executeQuery();
            list = addProducts(rs,list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Collection<Product> getAllProducts() {
        return getProductCollection("Select * from Product");
    }

    public Product getProduct(String pid) {
        try {
            // re-use the getProductCollection method
            // even though we only expect to get a single Product Object
            String query = "Select * from Product where PID = '" + pid + "'";
            Collection<Product> c = getProductCollection( query );
            Iterator<Product> i = c.iterator();
            return i.next();
        }
        catch(Exception e) {
            // unable to find the product matching that pid
            return null;
        }
    }

    public Collection<Product> getProductCollection(String query) {
        LinkedList<Product> list = new LinkedList<Product>();
        try {
            Statement s = con.createStatement();

            ResultSet rs = s.executeQuery(query);
            list = addProducts(rs,list);
        }
        catch(Exception e) {
            System.out.println( "Exception in getProducts(): " + e );
            return null;
        }
        return list;
    }

    public void order(Basket basket , String customer) {
        try {
            // create a unique order id
            String orderId = System.currentTimeMillis() + ":" + nOrders++;

            // iterate over the basket of contents ...

//            Iterator<Product> i = basket.getItems().iterator();
//            while (i.hasNext()) {
//                Product product = i.next();
//                // and place the order for each one
//                order( con, product, orderId, customer );
//            }
            HashMap<String, Integer> items = basket.getItems();
            for (Map.Entry<String, Integer> prod : items.entrySet()) {
                Product p = getProduct(prod.getKey());
                order( con, p, orderId, customer, prod.getValue());
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    private void order(Connection con, Product p, String orderId, String customer, int quantity) throws Exception {
//        String query = "INSERT INTO orders VALUES ('" + p.PID + "','" + orderId + "','" + customer + "', 1 , " + p.price + ";";
        String query = "INSERT INTO orders VALUES (?, ?, ?, ?, ?)";
        Statement statement = con.prepareStatement(query);
        ((PreparedStatement) statement).setString(1,p.PID);
        ((PreparedStatement) statement).setString(2,orderId);
        ((PreparedStatement) statement).setString(3,customer);
        ((PreparedStatement) statement).setDouble(4,quantity);
        ((PreparedStatement) statement).setDouble(5,p.price);
        ((PreparedStatement) statement).executeUpdate();
    }


}
