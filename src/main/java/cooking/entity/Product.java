package cooking.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_ProductInfo")
public class Product {

	// define fields
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="productId")
	private int productId;
	
	@Column(name="productName")
	private String productName;
	
	@Column(name="genre")
	private String genre;
	
	@Column(name="maker")
	private String maker;
	
	@Column(name="sellingPrice")
	private BigDecimal sellingPrice;
	
	@Column(name="productDetail")
	private String productDetail;
	
	// define constructors
	public Product() {
		
	}

	public Product(String productName, String genre, String maker, BigDecimal sellingPrice, String productDetail) {
		this.productName = productName;
		this.genre = genre;
		this.maker = maker;
		this.sellingPrice = sellingPrice;
		this.productDetail = productDetail;
	}

	// define getter/setter
	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getMaker() {
		return maker;
	}

	public void setMaker(String maker) {
		this.maker = maker;
	}

	public BigDecimal getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(BigDecimal sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}

	// define tostring
	@Override
	public String toString() {
		return "Product [productId=" + productId + ", productName=" + productName + ", genre=" + genre + ", maker="
				+ maker + ", sellingPrice=" + sellingPrice + ", productDetail=" + productDetail + "]";
	}
	
}
