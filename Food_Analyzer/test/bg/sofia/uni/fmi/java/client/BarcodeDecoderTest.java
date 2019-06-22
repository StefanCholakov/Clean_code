package bg.sofia.uni.fmi.java.client;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.Test;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import bg.sofia.uni.fmi.java.exceptions.BarcodeException;

public class BarcodeDecoderTest {

	private static final String QR_CODE_IMAGE_PATH = "resources\\QRTestCode.png";

	private static final int QR_CODE_WIDTH = 350;
	private static final int QR_CODE_HEIGHT = 350;

	@Test(expected = BarcodeException.class)
	public void exceptionIsThrownForInvaliFile() throws BarcodeException, WriterException, IOException {
		BarcodeDecoder.decodeQRCode(new File("invalid_path"));
	}

	@Test
	public void barcodeWasDecodedProperly() throws BarcodeException, WriterException, IOException {
		generateQRCodeImage("070038611295", QR_CODE_IMAGE_PATH);
		String decoded = BarcodeDecoder.decodeQRCode(new File(QR_CODE_IMAGE_PATH));
		assertEquals("070038611295", decoded);
	}

	private void generateQRCodeImage(String text, String filePath) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_CODE_WIDTH, QR_CODE_HEIGHT);

		Path path = FileSystems.getDefault().getPath(filePath);
		MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
	}
}
