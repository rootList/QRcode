/**
 * 
 */
package com.qr.tool.QRCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;


/**
 * @author chentao
 *
 */
public class QRTest {
    
    //二维码存放文件夹
    private static final String directory = "keyNosTest";
    
    private static final int WIDTH = 500;
    
    private static final int HEIGHT = 550;
    
    private static final int TEXTHEIGHT = 50;
    
    @Test
    public void createQr() {
        String data = "二维码内容";
        String text = "二维码";
        File logoFile = new File("logo.png");
        File qrFile = new File("ewm.png");
        createQrFile(data, text, logoFile, qrFile, WIDTH, HEIGHT, TEXTHEIGHT);
    }
    
    /**
     * 生成二维码文件
     * */
    private void createQrFile(String data, String text, File logoFile,
            File qrFile, int width, int height, int textHeight) {
        BufferedImage qrimage = QRCodeTool.createQRCodeWithLogo(data, width,
                height - textHeight, logoFile);
        
        BufferedImage txtimage = QRCodeTool.addMark(text, null, 1, "微软雅黑", 
                50, width, textHeight);
        
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);  
        
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);  
        
        graphics.drawImage(qrimage, 0, 0, null);
        graphics.drawImage(txtimage, 0, height - textHeight - 15, null);
        
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        try {
            ImageIO.write(image, "png", qrFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
