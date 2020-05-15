package cooking.entity;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;


@Entity
@Table(name="ProductInfo")
public class Product {

	// define fields
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ProductID")
	private int productID;
	
	@Column(name="ProductName")
	@NotNull(message="{EMSG001}")
	@Size(max=25, message="{EMSG002}")
	private String productName;
	
	@Column(name="Genre")
	private String genre;
	
	@Column(name="Maker")
	@NotNull(message="{EMSG001}")
	@Size(max=20, message="{EMSG002}")
	private String maker;
	
	@Column(name="SellingPrice")
	@NotNull(message="{EMSG001}")
	@Min(value=1, message="{EMSG004}")
	@Max(value=100000000, message="{EMSG005}")
	private BigDecimal sellingPrice;
	
	@Column(name="ProductDetail")
	@Size(max=200, message="{EMSG002}")
	private String productDetail;
	
	@Column(name="ProductImg")
	private byte[] productImg;
	
	@Column(name="DeleteFlg")
	private String deleteFlg;
	
	@Column(name="InsertDate")
	private Timestamp insertDate;
	
	@Column(name="UpdateDate")
	private Timestamp updateDate;
	
	@Transient
    private String base64Img;
	
	@Transient
	private MultipartFile multipartFile;
	
	// define constructors
	public Product() {
		
	}

	public Product(int productID, String productName, String genre, String maker, BigDecimal sellingPrice,
			String productDetail, byte[] productImg, String deleteFlg, Timestamp insertDate,
			Timestamp updateDate) {
		this.productID = productID;
		this.productName = productName;
		this.genre = genre;
		this.maker = maker;
		this.sellingPrice = sellingPrice;
		this.productDetail = productDetail;
		this.productImg = productImg;
		this.deleteFlg = deleteFlg;
		this.insertDate = insertDate;
		this.updateDate = updateDate;
	}

	// define getter/setter
	public int getProductID() {
		return productID;
	}
	
	public void setProductID(int productID) {
		this.productID = productID;
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
		if (sellingPrice != null) {
			this.sellingPrice = sellingPrice.setScale(0, RoundingMode.HALF_UP);
			
		}
	}
	
	public String getProductDetail() {
		return productDetail;
	}
	
	public void setProductDetail(String productDetail) {
		this.productDetail = productDetail;
	}
	
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	
	public byte[] getProductImg() {
		return productImg;
	}
	
	public void setProductImg(byte[] productImg) throws IOException {
		this.productImg = productImg;
	}
	
	public String getDeleteFlg() {
		return deleteFlg;
	}
	
	public void setDeleteFlg(String deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
	
	public Timestamp getInsertDate() {
		return insertDate;
	}
	
	public void setInsertDate(Timestamp insertDate) {
		this.insertDate = insertDate;
	}
	
	public Timestamp getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Timestamp updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getBase64Img() {
        base64Img = Base64.getEncoder().encodeToString(this.productImg);
        return base64Img;
    }
 
    public void setBase64Img(String base64Img) {
        this.base64Img = base64Img;
    }

	// define tostring
	@Override
	public String toString() {
		return "Product [productID=" + productID + ", productName=" + productName + ", genre=" + genre + ", maker="
				+ maker + ", sellingPrice=" + sellingPrice + ", productDetail=" + productDetail + ", productImg="
				+ productImg + ", deleteFlg=" + deleteFlg + ", insertDate=" + insertDate + ", updateDate=" + updateDate
				+ "]";
	}


}
