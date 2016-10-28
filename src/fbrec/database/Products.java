package fbrec.database;
import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to the product database
 * @author Daniel
 */
public class Products {
    public static final String DB_NAME         = "";
    public static final String DB_USER         = "";
    public static final String DB_PW           = "";
    public static final String TABLE           = "";
    
    public static final String LABEL_BABY      = "Baby";
    public static final String LABEL_GIRLS     = "Mädchen";
    public static final String LABEL_WOMEN     = "Damen";
    public static final String LABEL_BOYS      = "Jungen";
    public static final String LABEL_MEN       = "Herren";
    public static final String LABEL_UNISEX    = "Unisex";
    public static final String LABEL_UNDEFINED = "null";
    
    public static final String ID_FIELD        = "ID";
    public static final String TITLE_FIELD     = "TITLE";
    public static final String TEXT_FIELD      = "DESCRIPTION";
    public static final String BRAND_FIELD     = "BRAND";
    public static final String PRICE_FIELD     = "PRICE";
    public static final String GENDER_AGE_FIELD= "GENDER_AGE";
    public static final String CATEGORY_FIELD  = "CATEGORIES";
    
    
    
    
    private static Connection   con         = null;
    private static Product      lastProduct = null;
       

    /**
     * Returns the product with given ID
     * @param ID - ProductID
     * @return Product Object with all relevant data from database
     * @throws DatabaseException 
     */
    public static Product getById(int ID) throws DatabaseException{
        String      sql;
        ResultSet   res;
        try{
            if(lastProduct == null || lastProduct.ID != ID){
                sql = "SELECT "
                       +ID_FIELD+", "
                       +TITLE_FIELD+", "
                       +TEXT_FIELD+", "
                       +GENDER_AGE_FIELD+", "
                       +BRAND_FIELD+", "
                       +PRICE_FIELD+", "
                       +CATEGORY_FIELD+" "
                     + "FROM "
                       +TABLE+" "
                     + "WHERE "
                       +ID_FIELD+"="+ID;
                res = query(sql);
                res.next();
                lastProduct = new Product(ID, 
                                          res.getString(TITLE_FIELD), 
                                          res.getString(TEXT_FIELD), 
                                          res.getString(GENDER_AGE_FIELD), 
                                          res.getString(BRAND_FIELD), 
                                          res.getString(PRICE_FIELD), 
                                          res.getString(CATEGORY_FIELD)
                                         );
            }
            return lastProduct;
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }
    
    public static List<Product> getAll(int offset, int limit) throws DatabaseException{
        String          sql;
        ResultSet       res;
        List<Product>   resultList = new ArrayList<Product>(limit);
        try{
            sql = "SELECT "
                     +ID_FIELD+", "
                     +TITLE_FIELD+", "
                     +TEXT_FIELD+", "
                     +GENDER_AGE_FIELD+", "
                     +BRAND_FIELD+", "
                     +PRICE_FIELD+", "
                     +CATEGORY_FIELD+" "
                  + "FROM "
                     +TABLE+            " "
                  + "LIMIT "+offset+", "+limit;
            res = query(sql);
            
            while (res.next()) {
                lastProduct = new Product(res.getInt(ID_FIELD), 
                                          res.getString(TITLE_FIELD), 
                                          res.getString(TEXT_FIELD), 
                                          res.getString(GENDER_AGE_FIELD), 
                                          res.getString(BRAND_FIELD), 
                                          res.getString(PRICE_FIELD), 
                                          res.getString(CATEGORY_FIELD)
                                         );
                resultList.add(lastProduct);
            }
            return resultList;
        }catch(Exception e){
            throw new DatabaseException(e);
        }
    }
    
    /**
     * Executes given SQL-Query on db
     * @param query
     * @return
     * @throws DatabaseException 
     */
    private static ResultSet query(String query) throws DatabaseException{
        try{
            if(con == null){
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                con = DriverManager.getConnection(DB_NAME, DB_USER, DB_PW);
            }
            return con.prepareStatement(query).executeQuery();
        }catch(Exception e){
            throw new DatabaseException(e);
        }
        
    }
    
    /**
     * Encapsulates product data from product db
     */
    public static class Product{
        public int ID;
        public String title;
        public String description;
        public String genderAge;
        public String brand;
        public String price;
        public String[] categories;

        public Product(int ID, String title, String description, String genderAge, String brand, String price, String categories) {
            this.ID         = ID;
            this.title      = title;
            this.description= description;
            this.genderAge  = genderAge;
            this.brand      = brand;
            this.price      = price;
            this.categories = (categories != null) ? categories.split("[|]"): null;
        }
    }
}
