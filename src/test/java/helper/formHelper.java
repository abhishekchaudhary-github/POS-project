package helper;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.InventoryFormString;
import com.increff.employee.model.ProductFormString;

public class formHelper {
    public static BrandForm makeBrandForm(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    public static ProductFormString makeProductFormString(String barcode, String brand, String category,String mrp,String name) {
        ProductFormString productFormString = new ProductFormString();
        productFormString.setBarcode(barcode);
        productFormString.setBrand(brand);
        productFormString.setCategory(category);
        productFormString.setMrp(mrp);
        productFormString.setName(name);
        return productFormString;
    }

    public static InventoryFormString makeInventoryFormString(String barcode, String quantity) {
        InventoryFormString inventoryFormString = new InventoryFormString();
        inventoryFormString.setBarcode(barcode);
        inventoryFormString.setQuantity(quantity);
        return inventoryFormString;
    }
}
