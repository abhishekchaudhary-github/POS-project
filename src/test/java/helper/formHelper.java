package helper;

import com.increff.employee.model.BrandForm;

public class formHelper {
    public static BrandForm makeBrandForm(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }
}
