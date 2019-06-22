package bg.sofia.uni.fmi.java.client;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import bg.sofia.uni.fmi.java.exceptions.BarcodeException;

public class BarcodeDecoder {

	public static String decodeQRCode(File image) throws BarcodeException {
		try {
			BufferedImage bufferedImage = ImageIO.read(image);
			LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			String resultCode = "";
			Result result = new MultiFormatReader().decode(bitmap);
			resultCode = result.getText();

			return resultCode;
		} catch (IOException | NotFoundException e) {
			throw new BarcodeException("An error occured while decoding the barcode", e);
		}
	}
}