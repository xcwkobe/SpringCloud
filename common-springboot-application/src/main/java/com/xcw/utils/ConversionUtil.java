package com.xcw.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xslf.usermodel.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @class: ConversionUtil
 * @author: ChengweiXing
 * @description: 转换工具
 **/
@Slf4j
public class ConversionUtil {

    /**
     * pptx转换为png图片
     * @param ppt
     * @return
     */
    public static List<File> pptxToPng(File ppt) {
        List<File> pngFileList = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        FileInputStream is = null;
        // 将ppt文件转换成每一帧的图片
        XMLSlideShow pptx = null;

        try {
            ZipSecureFile.setMinInflateRatio(-1.0d);
            is = new FileInputStream(ppt);
            pptx = new XMLSlideShow(is);
            int idx = 1;

            Dimension pageSize = pptx.getPageSize();
            double image_rate = 1.0;
            int imageWidth = (int) Math.floor(image_rate * pageSize.getWidth());
            int imageHeight = (int) Math.floor(image_rate * pageSize.getHeight());

            for (XSLFSlide xslfSlide : pptx.getSlides()) {
                BufferedImage img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = img.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                // clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle2D.Float(0, 0, imageWidth, imageHeight));
                graphics.scale(image_rate, image_rate);

                //防止中文乱码
                for (XSLFShape shape : xslfSlide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        XSLFTextShape xslfTextShape = (XSLFTextShape) shape;
                        for (XSLFTextParagraph xslfTextParagraph : xslfTextShape) {
                            for (XSLFTextRun xslfTextRun : xslfTextParagraph) {
                                xslfTextRun.setFontFamily("宋体");
                            }
                        }
                    }
                }

                FileOutputStream out = null;
                try {
                    //slide转换为png
                    xslfSlide.draw(graphics);
                    File pngFile = new File(ppt.getPath().replace(".pptx", String.format("-%04d.png", idx++)));
                    out = new FileOutputStream(pngFile);
                    ImageIO.write(img, "png", out);
                    pngFileList.add(pngFile);
                } catch (Exception e) {
                    log.error("pptx2Png exception", e);
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();
                        }

                        if (graphics != null) {
                            graphics.dispose();
                        }

                        if (img != null) {
                            img.flush();
                        }
                    } catch (IOException e) {
                        log.error("pptx2Png close exception", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("pptx2Png exception", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }

                if (pptx != null) {
                    pptx.close();
                }
            } catch (Exception e) {
                log.error("pptx2Png exception", e);
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("pptxToPng耗时：{}", endTime - startTime);
        return pngFileList;
    }

    /**
     * 将图片转换为pdf
     * @param pngFiles 上面方法转换的图片集合
     * @param pdfFilePath 生成pdf的指定路径
     * @return
     */
    public static File pngToPdf(List<File> pngFiles, String pdfFilePath) {
        Document document = new Document();
        long startTime = System.currentTimeMillis();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            document.open();

            pngFiles.forEach(pngFile -> {
                try {
                    Image png = Image.getInstance(pngFile.getCanonicalPath());
                    png.scalePercent(70);
                    document.add(png);
                } catch (Exception e) {
                    log.error("png2Pdf exception", e);
                }
            });
            document.close();
            return new File(pdfFilePath);
        } catch (Exception e) {
            log.error(String.format("png2Pdf %s exception", pdfFilePath), e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            long endTime = System.currentTimeMillis();
            log.info("pngToPdf耗时：{}", endTime - startTime);
        }

        return null;
    }

    /**
     * pdf转换为一个长图
     * @param pdfFile
     * @param outPath
     */
    public static void pdfToOneImage(String pdfFile, String outPath) {
        long start=System.currentTimeMillis();
        try {
            InputStream is = new FileInputStream(pdfFile);
            PDDocument pdf = PDDocument.load(is);
            int actSize  = pdf.getNumberOfPages();
            List<BufferedImage> piclist = new ArrayList<>();
            for (int i = 0; i < actSize; i++) {
                BufferedImage image = new PDFRenderer(pdf).renderImageWithDPI(i,130, ImageType.RGB);
                piclist.add(image);
            }
            yPic(piclist, outPath);
            is.close();
        } catch (IOException e) {
            log.error("生成长图失败："+e.getMessage());
        }finally {
            long end=System.currentTimeMillis();
            log.info("pdfToOneImage耗时：{}",(end-start));
        }
    }

    /**
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同
     * @param piclist  文件流数组
     * @param outPath  输出路径
     */
    public static void yPic(List<BufferedImage> piclist, String outPath) {// 纵向处理图片
        if (piclist == null || piclist.size() <= 0) {
            log.error("图片数组为空");
            return;
        }
        try {
            int height = 0, // 总高度
                    width = 0, // 总宽度
                    _height = 0, // 临时的高度 , 或保存偏移高度
                    __height = 0, // 临时的高度，主要保存每个高度
                    picNum = piclist.size();// 图片的数量
            int[] heightArray = new int[picNum]; // 保存每个文件的高度
            BufferedImage buffer = null; // 保存图片流
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB
            int[] _imgRGB; // 保存一张图片中的RGB数据
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);
                heightArray[i] = _height = buffer.getHeight();// 图片高度
                if (i == 0) {
                    width = buffer.getWidth();// 图片宽度
                }
                height += _height; // 获取总高度
                _imgRGB = new int[width * _height];// 从图片中读取RGB
                _imgRGB = buffer.getRGB(0, 0, width, _height, _imgRGB, 0, width);
                imgRGB.add(_imgRGB);
            }
            _height = 0; // 设置偏移高度为0
            // 生成新图片
            BufferedImage imageResult = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i < picNum; i++) {
                __height = heightArray[i];
                if (i != 0) _height += __height; // 计算偏移高度
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i), 0, width); // 写入流中
            }
            File outFile = new File(outPath);
            ImageIO.write(imageResult, "jpg", outFile);// 写图片
        } catch (Exception e) {
            log.error("生成长图失败："+e.getMessage());
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
//        List<File> pngs = pptxToPng(new File("c:/oldG/res.pptx"));
//        File file = pngToPdf(pngs, "c:/oldG/res.pdf");
//        System.out.println(file.getAbsolutePath());
        pdfToOneImage("c:/oldG/res.pdf","c:/oldG/res.jpg");
    }
}
