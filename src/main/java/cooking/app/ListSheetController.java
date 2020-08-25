package cooking.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import cooking.entity.Product;
import cooking.service.ProductService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Controller
public class ListSheetController {

	@Autowired
	ResourceLoader resource;

	@Autowired
	ProductService productService;

	@PostMapping("/list-sheet")
	public String createSheet(HttpServletResponse response) {
		InputStream input;
		try {
			// PDFフォーマットファイルを指定する
			input = new FileInputStream(resource.getResource("classpath:reports/product-list.jrxml").getFile());

			if (input != null) {
				//コネクション取得
				Connection m_con = null;
				//ここは各自の環境設定
				String url = "jdbc:mysql://localhost:3306/wbr_inventory_control";
				String user = "testuser";
				String password = "testuser";

				//jdbcドライバをクラスパスに追加しておくこと
				Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
				m_con = DriverManager.getConnection(url, user, password);

				//PDFに埋め込まれた変数に値を設定するためのパラメータ
				HashMap<String, Object> params = new HashMap<String, Object>();

				// jrxmlをコンパイルする
				JasperReport jasperReport = JasperCompileManager.compileReport(input);

				// データソースとパラメータをコンパイルされた帳票に設定
				JasperPrint pdf = JasperFillManager.fillReport(jasperReport, params, m_con);

				// 帳票の出力
				byte[] output = JasperExportManager.exportReportToPdf(pdf);

				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment; filename=" + "product-list.pdf");
				response.setContentLength(output.length);

				OutputStream os = null;
				try {
					os = response.getOutputStream();
					os.write(output);
					os.flush();
					os.close();
				} catch (IOException e) {
					e.getStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//	@PostMapping("/list-sheet")
	//	public String createListSheet(HttpServletResponse response) {
	//		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
	//		//ヘッダーデータ作成
	//		HashMap<String, Object> params = new HashMap<String, Object>();
	//		params.put("title", "商品情報一覧");
	//		params.put("no_image", "../resources/static/images/no_image.png");
	//
	//		//フィールドデータ作成
	//		List<Product> fields = productService.findAll();
	//		params.put("count", fields.size());
	//		for (Product field : fields) {
	//			if (field.getProductImg() != null) {
	//				String image = field.getStringImg();
	//				params.put("image", image);
	//			}
	//		}
	//
	//		/**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/
	//		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　帳票出力部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
	//		//データを検索し帳票を出力
	//		byte[] output = orderListReport(params, fields);
	//		/**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/
	//
	//		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成データダウンロード部 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
	//		response.setContentType("application/octet-stream");
	//		response.setHeader("Content-Disposition", "attachment; filename=" + "product-list.pdf");
	//		response.setContentLength(output.length);
	//
	//		OutputStream os = null;
	//		try {
	//			os = response.getOutputStream();
	//			os.write(output);
	//			os.flush();
	//			os.close();
	//		} catch (IOException e) {
	//			e.getStackTrace();
	//		}
	//		/**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/
	//
	//		return null;
	//	}

	@PostMapping("/update-sheet")
	public String createUpdateSheet(HttpServletResponse response, @ModelAttribute("productID") int productID,
			Model model) {
		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
		//ヘッダーデータ作成
		HashMap<String, Object> params = new HashMap<String, Object>();
		//		params.put("title", "商品情報更新");
		//		params.put("no_image", "/cooking/src/main/resources/static/images/no_image.png");

		//フィールドデータ作成
		List<Product> fields = new ArrayList<Product>();
		fields.add(productService.findOne(productID));
		model.addAttribute("product", productService.findOne(productID));
		for (Product field : fields) {
			if (field.getProductImg() != null) {
				String image = field.getStringImg();
				params.put("image", image);
			}
			BufferedImage qrcode = barcode(productID, field.getProductName());
			params.put("qrcode", qrcode);
		}

		/**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/
		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　帳票出力部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
		//データを検索し帳票を出力
		byte[] output = orderUpdateReport(params, fields);
		/**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/

		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成データダウンロード部 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + "product-update.pdf");
		response.setContentLength(output.length);

		OutputStream os = null;
		try {
			os = response.getOutputStream();
			os.write(output);
			os.flush();
			os.close();
		} catch (IOException e) {
			e.getStackTrace();
		}
		/**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/

		return null;
	}

	//	/**
	//	 * ジャスパーレポートコンパイル。バイナリファイルを返却する。
	//	 * @param data
	//	 * @param response
	//	 * @return
	//	 */
	//	private byte[] orderListReport(HashMap<String, Object> param, List<Product> data) {
	//		InputStream input;
	//		try {
	//			//帳票ファイルを取得
	//			input = new FileInputStream(resource.getResource("classpath:reports/product-list2.jrxml").getFile());
	//			//リストをフィールドのデータソースに
	//			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
	//			//帳票をコンパイル
	//			JasperReport jasperReport = JasperCompileManager.compileReport(input);
	//
	//			JasperPrint jasperPrint;
	//			//パラメーターとフィールドデータを注入
	//			jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
	//			//帳票をByte形式で出力
	//			return JasperExportManager.exportReportToPdf(jasperPrint);
	//
	//		} catch (FileNotFoundException e) {
	//			// TODO 自動生成された catch ブロック
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO 自動生成された catch ブロック
	//			e.printStackTrace();
	//		} catch (JRException e) {
	//			// TODO 自動生成された catch ブロック
	//			e.printStackTrace();
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//
	//		return null;
	//
	//	}

	/**
	 * ジャスパーレポートコンパイル。バイナリファイルを返却する。
	 * @param data
	 * @param response
	 * @return
	 */
	private byte[] orderUpdateReport(HashMap<String, Object> param, List<Product> data) {
		InputStream input;
		try {
			//帳票ファイルを取得
			input = new FileInputStream(resource.getResource("classpath:reports/product-update.jrxml").getFile());
			//リストをフィールドのデータソースに
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
			//帳票をコンパイル
			JasperReport jasperReport = JasperCompileManager.compileReport(input);

			JasperPrint jasperPrint;
			//パラメーターとフィールドデータを注入
			jasperPrint = JasperFillManager.fillReport(jasperReport, param, dataSource);
			//帳票をByte形式で出力
			return JasperExportManager.exportReportToPdf(jasperPrint);

		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (JRException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public BufferedImage barcode(int productID, String productName) {
		String productId = String.valueOf(productID);
		String content = productId + " / " + productName;
		int width = 200;
		int height = 200;
		String output = "qrcode.png";
		String encoding = "UTF-8";
		ConcurrentHashMap<EncodeHintType, String> hints = new ConcurrentHashMap<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, encoding);

		try {
			QRCodeWriter qrWriter = new QRCodeWriter();

			//QRCodeWriter#encode()には以下の情報を渡す
			// (1)エンコード対象の文字列、バーコードに埋め込みたい情報
			// (2)出力するバーコードの書式
			// (3)イメージの幅
			// (4)イメージの高さ
			BitMatrix bitMatrix = qrWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

			BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);

			//エンコードで得られたイメージを画像ファイルに出力する
			ImageIO.write(image, "png", new File(output));
			return image;

		} catch (WriterException e) {
			System.err.println("[" + content + "] をエンコードするときに例外が発生.");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.err.println("[" + output + "] を出力するときに例外が発生.");
			e.printStackTrace();
			return null;
		}
	}
}
