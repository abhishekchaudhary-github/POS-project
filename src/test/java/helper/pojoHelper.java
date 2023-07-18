package helper;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;

public class pojoHelper {
    public static BrandPojo makeBrandPojo(String brand, String category) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }
}
