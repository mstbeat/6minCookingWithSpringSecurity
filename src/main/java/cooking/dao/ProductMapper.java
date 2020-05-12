package cooking.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import cooking.entity.Product;
import cooking.util.MyBatisUtil;

@Repository
public class ProductMapper {

	public List<Product> getAllProducts(){
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		List<Product> productList = session.selectList("getAllProducts");
		session.commit();
		session.close();
		return productList;
	}
	
	public void createProduct(Product product) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		session.insert("insertProduct", product);
		session.commit();
		session.close();
	}
	
	public Product findById(int productID) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		Product product = (Product)session.selectOne("findById", productID);
		session.commit();
		session.close();
		return product;
	}
	
	public void updateProduct(Product product) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		session.update("updateProduct", product);
		session.commit();
		session.close();
	}
	
	public void deleteProduct(int productID) {
		SqlSession session = MyBatisUtil.getSqlSessionFactory().openSession();
		session.delete("deleteProduct", productID);
		session.commit();
		session.close();
	}
}
