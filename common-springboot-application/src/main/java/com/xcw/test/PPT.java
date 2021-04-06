package com.xcw.test;

import com.spire.presentation.*;
import com.spire.presentation.drawing.BackgroundType;
import com.spire.presentation.drawing.FillFormatType;
import com.spire.presentation.drawing.IImageData;
import com.spire.presentation.drawing.PictureFillType;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @class: PPT
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
public class PPT {

    /**
     * 设置ppt模板背景和logo
     * @param ppt
     * @param bgPath
     * @param logoPath
     */
    public static void setBackground(Presentation ppt,String bgPath,String logoPath) {
        try {
            //设置幻灯片大小
            ppt.getSlideSize().setType(SlideSizeType.SCREEN_16_X_9);

            //获取第一张母版
            IMasterSlide masterSlide = ppt.getMasters().get(0);

            //设置母版背景
            BufferedImage image = ImageIO.read(new FileInputStream(bgPath));
            IImageData imageData = ppt.getImages().append(image);
            masterSlide.getSlideBackground().setType(BackgroundType.CUSTOM);
            masterSlide.getSlideBackground().getFill().setFillType(FillFormatType.PICTURE);
            masterSlide.getSlideBackground().getFill().getPictureFill().setFillType(PictureFillType.STRETCH);
            masterSlide.getSlideBackground().getFill().getPictureFill().getPicture().setEmbedImage(imageData);

            //添加logo到母版
            image = ImageIO.read(new FileInputStream(logoPath));
            imageData = ppt.getImages().append(image);
            IEmbedImage imageShape = masterSlide.getShapes().appendEmbedImage(ShapeType.RECTANGLE,imageData,new Rectangle2D.Float((float) ppt.getSlideSize().getSize().getWidth()-240,40,60,60));
            imageShape.getLine().setFillType(FillFormatType.NONE);

        }catch (Exception e){
            log.error("生成ppt模板失败");
        }

    }

    /**
     * PPT转换为PDF
     * @param ppt
     * @param pdfPath
     */
    public static void convertToPDF(Presentation ppt,String pdfPath){
        try {
            ppt.saveToFile(new FileOutputStream(pdfPath), FileFormat.PDF);
            ppt.dispose();
        } catch (FileNotFoundException e) {
            log.error("转换PDF失败");
        }
    }

    /**
     * 转换为图片
     * @param ppt
     * @param dirPath
     */
    public static void convertToImages(Presentation ppt,String dirPath){
        try {
            //Save PPT document to images
            for (int i = 0; i < ppt.getSlides().getCount(); i++) {
                BufferedImage image = ppt.getSlides().get(i).saveAsImage();
                String fileName = dirPath + String.format("ToImage-%1$s.png", i);
                ImageIO.write(image, "PNG",new File(fileName));
            }
        }catch (Exception e){
            log.error("生成图片失败");
        }
    }

    public static void insertPara(ISlide slide, String content, Rectangle rectangle){
        try {
            //80, 120, 550, 200
            //获取第一张幻灯片，添加指定大小和位置的矩形文本框
            IAutoShape tb = slide.getShapes().appendShape(ShapeType.RECTANGLE,rectangle);

            //设置文本框边框样式
            tb.getFill().setFillType(FillFormatType.NONE);
            tb.getLine().setFillType(FillFormatType.NONE);
            //tb.getLine().setWidth(2.5);

            //添加文本到文本框，并格式化文本
            //tb.setHeight()
            tb.appendTextFrame(content);
            PortionEx textRange = tb.getTextFrame().getTextRange();
            //textRange.setLineSpacing(5);
            textRange.getFill().setFillType(FillFormatType.SOLID);
            textRange.getFill().getSolidColor().setColor(Color.black);
            textRange.setFontHeight(30);
            textRange.setLatinFont(new TextFont("宋体"));
        }catch (Exception e){
            log.error("插入段落失败");
        }
    }

    public static void insertImage(Presentation ppt, ISlide slide, String imagePath){
        try {
            Rectangle2D rect = new Rectangle2D.Double(ppt.getSlideSize().getSize().getWidth() / 2 - 380, 140, 240, 240);
            IEmbedImage image = slide.getShapes().appendEmbedImage(ShapeType.RECTANGLE, imagePath, rect);
            image.getLine().setFillType(FillFormatType.NONE);
        }catch (Exception e){
            log.error("插入图片失败");
        }

    }

    public static void main(String[] args) throws Exception {
        //        Presentation ppt = new Presentation();
//        //设置模板
//        setBackground(ppt,"c:/oldG/bg.png","c:/oldG/jd.jpg");
//        //添加三张幻灯片
//        ISlide s0 = ppt.getSlides().get(0);
//        ISlide s1 = ppt.getSlides().append();
//        ISlide s2 = ppt.getSlides().append();
//
//        insertPara(s0,"京东xx方案\n提案人：xxx",new Rectangle((int) (ppt.getSlideSize().getSize().getWidth()/2), 120, 550, 200));
//        insertPara(s1,"商品参数：xxsasda\n商品价格：2222",new Rectangle(300, 120, 550, 200));
//        insertPara(s2,"商品参数：xxsasda\n商品价格：2222",new Rectangle(300, 120, 550, 200));
//
//        insertImage(ppt,s1,"c:/oldG/p1.png");
//        insertImage(ppt,s2,"c:/oldG/p2.png");
//
//        //保存ppt，同时保存为pdf
//        ppt.saveToFile("c:/oldG/res.pptx", FileFormat.PPTX_2013);
//        convertToPDF(ppt,"c:/oldG/1.pdf");
//        ppt.dispose();
    }
}
