package cooking.app;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
	public String createListSheet(HttpServletResponse response) {
		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
        //ヘッダーデータ作成
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("title", "商品情報一覧");
        params.put("no_image", "../resources/static/images/no_image.png");

        //フィールドデータ作成
        List<Product> fields = productService.findAll();
        params.put("count", fields.size());
        for (Product field : fields) {
        	if (field.getProductImg() != null) {
        		String image = field.getStringImg();
        		params.put("image", image);
        	}
        };

        /**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/
        /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　帳票出力部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
        //データを検索し帳票を出力
        byte[] output  = orderListReport(params, fields);
        /**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/

        /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成データダウンロード部 ▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
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
        /**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/


        return null;
    }
	
	@PostMapping("/update-sheet")
	public String createUpdateSheet(HttpServletResponse response, @ModelAttribute("productID") int productID) {
		/**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　データ作成部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
        //ヘッダーデータ作成
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("title", "商品情報更新");
        params.put("no_image", "/cooking/src/main/resources/static/images/no_image.png");

        //フィールドデータ作成
        List<Product> fields = new ArrayList<Product>();
        fields.add(productService.findOne(productID));
        for (Product field : fields) {
        	if (field.getProductImg() != null) {
        		String image = field.getStringImg();
        		params.put("image", image);
        	}
        };

        /**▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲　データ作成部　▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲**/
        /**▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼　帳票出力部　▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼▼**/
        //データを検索し帳票を出力
        byte[] output  = orderUpdateReport(params, fields);
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
	
	/**
	 * ジャスパーレポートコンパイル。バイナリファイルを返却する。
	 * @param data
	 * @param response
	 * @return
	 */
	private byte[] orderListReport(HashMap<String, Object> param, List<Product> data) {
		InputStream input;
		try {
			//帳票ファイルを取得
			input = new FileInputStream(resource.getResource("classpath:reports/product-list.jrxml").getFile());
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
}
