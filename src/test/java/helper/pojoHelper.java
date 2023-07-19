package helper;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;

public class pojoHelper {
    public static BrandPojo makeBrandPojo(String brand, String category) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    public static ProductPojo makeProductPojo(String barcode, Integer brand_category, Double mrp, String name ) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setBarcode(barcode);
        productPojo.setBrand_category(brand_category);
        productPojo.setMrp(mrp);
        productPojo.setName(name);
        return productPojo;
    }

    public static InventoryPojo makeInventoryPojo(Integer productId, Integer quantity ) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(quantity);
        return inventoryPojo;
    }

}
