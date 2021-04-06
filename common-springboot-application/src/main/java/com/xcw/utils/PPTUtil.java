package com.xcw.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.sl.usermodel.PictureData;
import org.apache.poi.sl.usermodel.TextParagraph;
import org.apache.poi.xslf.usermodel.*;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @class: PPTUtil
 * @author: ChengweiXing
 * @description: TODO
 **/
@Slf4j
public class PPTUtil {

    /**
     * 添加图片
     * @param slide
     * @param picturePath 图片地址
     * @param ppt
     * @param location 图片在ppt中的位置
     */
    private static void createPicture(XSLFSlide slide, String picturePath, XMLSlideShow ppt,Rectangle location) {
        try {
            byte[] pictureData = IOUtils.toByteArray(new FileInputStream(picturePath));
            XSLFPictureData pictureIndex = ppt.addPicture(pictureData, PictureData.PictureType.PNG);
            XSLFPictureShape pictureShape = slide.createPicture(pictureIndex);
            pictureShape.setAnchor(location);
        } catch (IOException e) {
            log.error("插入图片失败："+e.getMessage());
        }
    }

    /**
     * 添加文本
     * @param slide
     * @param text
     * @param location
     */
    public static void createText(XSLFSlide slide,String text,Rectangle location){
        XSLFTextBox textBox = slide.createTextBox();
        XSLFTextParagraph paragraph = textBox.addNewTextParagraph();
        paragraph.setTextAlign(TextParagraph.TextAlign.LEFT);
        //paragraph.addNewTextRun().setText(text);
        textBox.setAnchor(location);
        textBox.setText(text);
    }


    /**
     *
     * @param slide
     * @param picCreateTime
     */
    public static void createTime(XSLFSlide slide, String picCreateTime){
        //标题文本框
        XSLFTextBox xslfTextBox = slide.createTextBox();
        xslfTextBox.setAnchor(new Rectangle(400, 460, 300, 80));
        xslfTextBox.setFlipHorizontal(true);
        //段落
        XSLFTextParagraph paragraph0 = xslfTextBox.addNewTextParagraph();
        paragraph0.setTextAlign(TextParagraph.TextAlign.LEFT);
        XSLFTextRun xslfTextRun = paragraph0.addNewTextRun();
        xslfTextRun.setFontSize(18d);
        //宋体 (正文)
        xslfTextRun.setFontFamily("\u5b8b\u4f53 (\u6b63\u6587)");
        String text = "123";
        xslfTextRun.setText(String.format(text, picCreateTime));
    }

    /**
     * 给幻灯片添加标题
     * @param title
     * @param slide 指定幻灯片
     */
    public static void createTitle(XSLFSlide slide,String title){
        //标题文本框
        XSLFTextBox xslfTextBox = slide.createTextBox();
        xslfTextBox.setAnchor(new Rectangle(10, 25, 700, 85));
        xslfTextBox.setFlipHorizontal(true);
        //段落
        XSLFTextParagraph paragraph0 = xslfTextBox.addNewTextParagraph();
        paragraph0.setTextAlign(TextParagraph.TextAlign.CENTER);
        XSLFTextRun xslfTextRun = paragraph0.addNewTextRun();
        xslfTextRun.setFontSize(44d);
        //黑体
        xslfTextRun.setFontFamily("\u9ed1\u4f53");
        xslfTextRun.setBold(true);
        xslfTextRun.setText(title);
    }

    public static void main(String[] args) throws Exception {

        XMLSlideShow ppt = new XMLSlideShow();

        XSLFSlide slide = ppt.createSlide();//创建幻灯片
        createTitle(slide, "hello");
        createPicture(slide,"c:/oldG/bg.png",ppt,new Rectangle(125, 100, 467, 350));
        ppt.write(new FileOutputStream("c:/oldG/res.pptx"));
        log.info("导出ppt成功");
    }
}
