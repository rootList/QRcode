package com.qr.tool.QRCode;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64OutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
/**
 * @author chentao
 * 
 * */
public class QRCodeTool {

    public static final String QRCODE_DEFAULT_CHARSET = "UTF-8";

    public static final int QRCODE_DEFAULT_HEIGHT = 150;

    public static final int QRCODE_DEFAULT_WIDTH = 150;

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 
     * @param targetFilePath ：生成后的目标图片路径
     * @param markContent :要加的文字
     * @param markContentColor :文字颜色
     * @param qualNum :质量数字
     * @param fontType :字体类型
     * @param fontSize :字体大小
     * @return
     */
    public static BufferedImage addMark(String markContent, Color markContentColor, float qualNum,
            String fontType, int fontSize, int imageWidth, int imageHeight) {

        BufferedImage bImage =
                new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        // 构建2D画笔
        Graphics2D g = bImage.createGraphics();
        /* 设置2D画笔的画出的文字颜色 */
        g.setColor(markContentColor);
        /* 设置2D画笔的画出的文字背景色 */
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, imageWidth, imageHeight);
        g.setColor(Color.BLACK);
        // g.setBackground(Color.WHITE);
        /* 画出图片 */
        g.drawImage(bImage, 0, 0, null);
        /* --------对要显示的文字进行处理-------------- */
        AttributedString ats = new AttributedString(markContent);
        Font font = new Font(fontType, Font.PLAIN, fontSize);
        g.setFont(font);
        /* 消除java.awt.Font字体的锯齿 */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // font = g.getFont().deriveFont(30.0f);
        ats.addAttribute(TextAttribute.FONT, font, 0, markContent.length());
        AttributedCharacterIterator iter = ats.getIterator();

        /* 添加水印的文字和设置水印文字出现的内容 ----位置 */
        g.drawString(iter, imageWidth / 2 - imageWidth / 4, imageHeight - 5);
        /* --------对要显示的文字进行处理-------------- */
        g.dispose();// 画笔结束

        return bImage;
    }

    /**
     * 
     * @param souchFilePath ：源图片路径
     * @param targetFilePath ：生成后的目标图片路径
     * @param markContent :要加的文字
     * @param markContentColor :文字颜色
     * @param qualNum :质量数字
     * @param fontType :字体类型
     * @param fontSize :字体大小
     * @return
     */
    public static void createMark(String souchFilePath, String targetFilePath, String markContent,
            Color markContentColor, float qualNum, String fontType, int fontSize, int w, int h,
            Color color) {
        markContentColor = color;
        /* 构建要处理的源图片 */
        ImageIcon imageIcon = new ImageIcon(souchFilePath);
        /* 获取要处理的图片 */
        Image image = imageIcon.getImage();
        // Image可以获得图片的属性信息
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        // 为画出与源图片的相同大小的图片（可以自己定义）
        BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 构建2D画笔
        Graphics2D g = bImage.createGraphics();
        /* 设置2D画笔的画出的文字颜色 */
        g.setColor(markContentColor);
        /* 设置2D画笔的画出的文字背景色 */
        g.setBackground(Color.white);
        /* 画出图片 */
        g.drawImage(image, 0, 0, null);
        /* --------对要显示的文字进行处理-------------- */
        AttributedString ats = new AttributedString(markContent);
        Font font = new Font(fontType, Font.BOLD, fontSize);
        g.setFont(font);
        /* 消除java.awt.Font字体的锯齿 */
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /* 消除java.awt.Font字体的锯齿 */
        // font = g.getFont().deriveFont(30.0f);
        ats.addAttribute(TextAttribute.FONT, font, 0, markContent.length());
        AttributedCharacterIterator iter = ats.getIterator();
        /* 添加水印的文字和设置水印文字出现的内容 ----位置 */
        g.drawString(iter, width - w, height - h);
        /* --------对要显示的文字进行处理-------------- */
        g.dispose();// 画笔结束
        try {
            // 输出 文件 到指定的路径
            FileOutputStream out = new FileOutputStream(targetFilePath);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bImage);
            param.setQuality(qualNum, true);
            encoder.encode(bImage, param);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create qrcode with default settings
     *
     * 
     * @param data
     * @return
     */
    public static BufferedImage createQRCode(String data) {
        return createQRCode(data, QRCODE_DEFAULT_WIDTH, QRCODE_DEFAULT_HEIGHT);
    }

    /**
     * Create qrcode with default charset
     *
     * 
     * @param data
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createQRCode(String data, int width, int height) {
        return createQRCode(data, QRCODE_DEFAULT_CHARSET, width, height);
    }

    /**
     * Create qrcode with specified charset
     *
     * @param data
     * @param charset
     * @param width
     * @param height
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BufferedImage createQRCode(String data, String charset, int width, int height) {
        Map hint = new HashMap();
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hint.put(EncodeHintType.CHARACTER_SET, charset);

        return createQRCode(data, charset, hint, width, height);
    }

    /**
     * Create qrcode with specified hint
     *
     * 
     * @param data
     * @param charset
     * @param hint
     * @param width
     * @param height
     * @return
     */
    public static BufferedImage createQRCode(String data, String charset,
            Map<EncodeHintType, ?> hint, int width, int height) {
        BitMatrix matrix;
        try {
            matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset),
                    BarcodeFormat.QR_CODE, width, height, hint);
            return toBufferedImage(matrix);
        } catch (WriterException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    /**
     * Create qrcode with default settings and logo
     *
     * 
     * @param data
     * @param logoFile
     * @return
     */
    public static BufferedImage createQRCodeWithLogo(String data, File logoFile) {
        return createQRCodeWithLogo(data, QRCODE_DEFAULT_WIDTH, QRCODE_DEFAULT_HEIGHT, logoFile);
    }

    /**
     * Create qrcode with default charset and logo
     *
     * 
     * @param data
     * @param width
     * @param height
     * @param logoFile
     * @return
     */
    public static BufferedImage createQRCodeWithLogo(String data, int width, int height,
            File logoFile) {
        return createQRCodeWithLogo(data, QRCODE_DEFAULT_CHARSET, width, height, logoFile);
    }

    /**
     * Create qrcode with specified charset and logo
     *
     * 
     * @param data
     * @param charset
     * @param width
     * @param height
     * @param logoFile
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static BufferedImage createQRCodeWithLogo(String data, String charset, int width,
            int height, File logoFile) {
        Map hint = new HashMap();
        hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hint.put(EncodeHintType.CHARACTER_SET, charset);
        hint.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
        hint.put(EncodeHintType.MARGIN, 2);

        return createQRCodeWithLogo(data, charset, hint, width, height, logoFile);
    }

    /**
     * Create qrcode with specified hint and logo
     *
     * 
     * @param data
     * @param charset
     * @param hint
     * @param width
     * @param height
     * @param logoFile
     * @return
     */
    public static BufferedImage createQRCodeWithLogo(String data, String charset,
            Map<EncodeHintType, ?> hint, int width, int height, File logoFile) {
        try {
            BufferedImage qrcode = createQRCode(data, charset, hint, width, height);
            BufferedImage logo = ImageIO.read(logoFile);
            
            int deltaHeight = height - 110;
            int deltaWidth = width - 110;

            BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) combined.getGraphics();
           // g.drawImage(qrcode, 0, 0, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            g.drawImage(logo, (int) Math.round(deltaWidth / 2), 
                    (int) Math.round(deltaHeight / 2),  110, 110, null);
            return combined;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 把文字生成图片
     * 
     * @param content 文字内容
     * @param imageWidth 图片宽度
     * @param imageHeight 图片高度
     * @param textSize  文字大小
     * 
     */
    public static BufferedImage graphicsGeneration(String content, int imageWidth, int imageHeight,
            int textSize) {

        BufferedImage image =
                new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("微软雅黑", Font.PLAIN, textSize));
        graphics.drawString(content, imageWidth / 2 - imageWidth / 4, imageHeight - 5);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.dispose();

        return image;
    }

    /**
     * 更改图片大小
     * 
     */
    public static BufferedImage updateSize(BufferedImage image, double rate) {

        AffineTransform transform = new AffineTransform();

        int w = image.getWidth();
        int h = image.getHeight();
        int nw = (int) (w * rate);
        int nh = (int) (h * rate);

        transform.setToScale(rate, rate);
        AffineTransformOp ato = new AffineTransformOp(transform, null);
        BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_RGB);
        ato.filter(image, bid);

        return bid;
    }


    /**
     * Return base64 for image
     *
     * 
     * @param image
     * @return
     */
    public static String getImageBase64String(BufferedImage image) {
        String result = null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            OutputStream b64 = new Base64OutputStream(os);
            ImageIO.write(image, "png", b64);
            result = os.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return result;
    }

    /**
     * Decode the base64Image data to image
     *
     * @param base64ImageString
     * @param file
     */
    public static void convertBase64StringToImage(String base64ImageString, File file) {
        FileOutputStream os;
        try {
            Base64 d = new Base64();
            byte[] bs = d.decode(base64ImageString);
            os = new FileOutputStream(file.getAbsolutePath());
            os.write(bs);
            os.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}

