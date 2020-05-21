package cooking.repository;

import java.sql.Timestamp;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cooking.entity.Product;

@Mapper
public interface ProductMapper {

	List<Product> getProductInfoList();

	void createProduct(Product product);

	Product findById(int productID);

	int updateProduct(Product product);

	int deleteProduct(@Param("productID") int productID, @Param("updateDate") Timestamp updateDate);
}
